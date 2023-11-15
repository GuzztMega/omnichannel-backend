package br.com.guzzmega.omnichannel;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
		title = "Omnichannel Open API",
		description = "This documentation will guide you through registering customers and sending messages on multiplatform channels. Developed by Guzzmega.",
		version = "1.0.0"))
@SpringBootApplication
public class OmnichannelApplication {
	public static void main(String[] args) {
		SpringApplication.run(OmnichannelApplication.class, args);
	}
}
