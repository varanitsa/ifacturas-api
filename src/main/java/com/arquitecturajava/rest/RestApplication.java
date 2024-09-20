package com.arquitecturajava.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import io.swagger.v3.oas.annotations.servers.Server;



@OpenAPIDefinition(
		servers = {
				@Server(url = "http://localhost:8080", description = "HTTP Server"),
				@Server(url = "https://localhost:8443", description = "HTTPS Server")
		}
)
@SpringBootApplication




public class RestApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApplication.class, args);
	}


	@Bean
	@Profile("http")
	public TomcatServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		factory.addAdditionalTomcatConnectors(httpConnector());
		return factory;
	}

	private Connector httpConnector() {
		Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
		connector.setScheme("http");
		connector.setPort(8080);
		connector.setSecure(false);
		connector.setRedirectPort(8443);
		return connector;
	}
}

