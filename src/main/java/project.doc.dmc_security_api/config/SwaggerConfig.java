package project.doc.dmc_security_api.config;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    public SwaggerConfig() {
    }

    @Bean
    public Docket DmcSecurityApi(TypeResolver resolver) {
        return (new Docket(DocumentationType.SWAGGER_2)).select().apis(RequestHandlerSelectors.basePackage("project.doc.dmc_security_api.config.controller")).paths(PathSelectors.regex("/.*")).build().useDefaultResponseMessages(false).apiInfo(this.apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("DMC Security Api", "Build a jwt claim set containing the roles and scopes for authorization", "", "", new Contact(" ", " ", " "), "", "", new ArrayList());
    }
}