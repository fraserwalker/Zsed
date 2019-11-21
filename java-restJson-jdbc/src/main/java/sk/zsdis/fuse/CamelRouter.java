package sk.zsdis.fuse;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import sk.zsdis.fuse.model.Request;
import sk.zsdis.fuse.model.Result;
import sk.zsdis.fuse.model.TryAgainLater;
import sk.zsdis.fuse.processor.JdbcProcessor;

@Component
public class CamelRouter extends RouteBuilder {

    private JdbcProcessor jdbcProcessor;

    @Bean
    JdbcProcessor jdbcProcessor() {
        if (jdbcProcessor == null) {
            this.jdbcProcessor = new JdbcProcessor();
        }
        return jdbcProcessor;
    }

    @Override
    public void configure() throws Exception {
        // Set up behaviour of exception handler
        onException(Exception.class).log(LoggingLevel.ERROR, "Exception Encountered").handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.BAD_REQUEST))
                .transform().constant(new TryAgainLater());
        // Define formats for marshalling and unmarshalling
        JacksonDataFormat resultFormat = new ListJacksonDataFormat(Result.class);

        // Configure components
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json);

        // Define routes
        rest("/")
                .post("/")
                .consumes(MimeTypeUtils.APPLICATION_JSON_VALUE)
                .produces(MimeTypeUtils.APPLICATION_JSON_VALUE)
                .type(Request.class)
                .route().routeId("java-restJson-jdbc")
                .log("Received Message : ${body}")
                .to("direct:dbquery");

        from("direct:dbquery").routeId("dbquery")
                .doTry()
                .to("jdbcProcessor")
                .log(LoggingLevel.INFO, "SQL Query: ${body}")
                .to("jdbc:datasource")
                .log(LoggingLevel.INFO, "JDBC Result: ${body}")
                .to("direct:marshal")
                .doCatch(Exception.class)
                .to("direct:specificException");

        from("direct:marshal").routeId("marshal")
                .marshal().json(JsonLibrary.Jackson)
                .log(LoggingLevel.INFO, "Marshalled to: ${body}")
                .to("direct:unmarshal");

        from("direct:unmarshal").routeId("unmarshal")
                .unmarshal(resultFormat)
                .log(LoggingLevel.INFO, "Unmarshalled to: ${body}");

        from("direct:specificException").routeId("specificException")
                .log("Handling Exception")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.BAD_REQUEST))
                .transform().constant(new TryAgainLater());
    }

}