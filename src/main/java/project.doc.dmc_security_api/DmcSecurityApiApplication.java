package project.doc.dmc_security_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import project.doc.dmc_security_api.config.SslApiRestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class DmcSecurityApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(DmcSecurityApiApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DmcSecurityApiApplication.class);
	}

	@Bean(name = "sslApiRestTemplate")
	public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
		return new SslApiRestTemplate().getRestTemplate(builder);
	}

}
