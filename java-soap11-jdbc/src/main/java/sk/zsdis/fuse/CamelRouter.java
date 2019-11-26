package sk.zsdis.fuse;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;

@Component
public class CamelRouter extends RouteBuilder {

    private String uri = "cxf:bean:queryEndpoint";
    private CxfEndpoint queryEndpointSingleton;

    @Autowired
    CamelContext camelContext;

    @Bean
    CxfEndpoint queryEndpoint() {
        if (queryEndpointSingleton == null) {
            queryEndpointSingleton = new CxfEndpoint();
            queryEndpointSingleton.setAddress("/query");
            queryEndpointSingleton.setBeanId("queryEndpoint");
            queryEndpointSingleton.setServiceClass(QueryEndpointService.class);
            queryEndpointSingleton.setCamelContext(camelContext);
            queryEndpointSingleton.setWsdlURL("META-INF/wsdl/service.wsdl");
            queryEndpointSingleton.setEndpointName(QName.valueOf("QueryEndpoint"));
            queryEndpointSingleton.setServiceName(QName.valueOf("QueryEndpointService"));
        }
        return queryEndpointSingleton;
    }

    @Override
    public void configure() throws Exception {

        // Set up behaviour of exception handler
//        onException(Exception.class).log(LoggingLevel.ERROR, "Exception Encountered").handled(true)
//                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.BAD_REQUEST))
//                .transform().constant(new TryAgainLater());
        from(uri).to("log:Test");
    }

}