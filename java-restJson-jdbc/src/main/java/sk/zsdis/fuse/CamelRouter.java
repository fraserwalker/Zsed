package sk.zsdis.fuse;

import org.apache.camel.builder.NoErrorHandlerBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import sk.zsdis.fuse.model.Request;
import sk.zsdis.fuse.model.Result;

@Component
public class CamelRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        errorHandler(new NoErrorHandlerBuilder());
        JacksonDataFormat jacksonDataFormat = new JacksonDataFormat(Request.class);
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json);

        rest("/")
                .post("/")
                .consumes(MimeTypeUtils.APPLICATION_JSON_VALUE)
                .produces(MimeTypeUtils.APPLICATION_JSON_VALUE)
                .type(Request.class)
                .route().routeId("query-json-only")
                .to("log:" + CamelRouter.class.getCanonicalName() + "?level=WARN")
                .to("direct:response").outputType(Result.class);

        from("direct:response").routeId("response")
                .transform().constant(new Result());

    }

}