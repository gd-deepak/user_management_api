package project.doc.dmc_security_api.config;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
    public SecurityConfig() {
    }

    @Bean
    public PasswordService passwordService() {
        return new DefaultPasswordService();
    }
}
