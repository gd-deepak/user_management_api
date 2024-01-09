package project.doc.dmc_security_api.dao;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Stopwatch;
import lombok.Generated;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import project.doc.dmc_security_api.exceptions.JwtValidationException;

@Component
public class OktaValidation {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(OktaValidation.class);
    private RestTemplate restTemplate;
    @Value("${okta.issuer}")
    private String oktaIssuer;
    private HttpHeaders headers = new HttpHeaders();

    @Autowired
    public OktaValidation(@Qualifier("sslApiRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ObjectNode validate(String jwtToken) throws JwtValidationException {
        if (StringUtils.isNoneBlank(jwtToken)) {
            this.headers.setBearerAuth(jwtToken);
        }

        HttpEntity<String> entity = new HttpEntity(this.headers);
        Stopwatch watch = Stopwatch.createStarted();

        try {
            ResponseEntity<ObjectNode> response = this.restTemplate.exchange(this.oktaIssuer, HttpMethod.GET, entity, ObjectNode.class, new Object[0]);
            watch.stop();
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else if (response.getStatusCode().is4xxClientError()) {
                throw new JwtValidationException("Failed to retrieve user information from Okta: " + response.getStatusCode());
            } else if (response.getStatusCode().is5xxServerError()) {
                throw new JwtValidationException("Okta server error: " + response.getStatusCode());
            } else {
                throw new JwtValidationException("Unexpected response from Okta: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new JwtValidationException("Failed to communicate with Okta: " + e.getMessage());
        } catch (HttpServerErrorException var6) {
            throw new JwtValidationException("Okta server error: " + var6.getMessage());
        } catch (RestClientException e) {
            throw new JwtValidationException("Failed to communicate with Okta: " + e.getMessage());
        }
    }
}

