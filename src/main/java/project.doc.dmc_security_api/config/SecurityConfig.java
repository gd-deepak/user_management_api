package project.doc.dmc_security_api.config;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@Configuration
@Import(SecurityAutoConfiguration.class)
public class SecurityConfig {
    public SecurityConfig() {}

    @Bean
    public PasswordService passwordService() {
        return new DefaultPasswordService();
    }

    @Bean
    public RestTemplate sslApiRestTemplate() {return new RestTemplate();}

}