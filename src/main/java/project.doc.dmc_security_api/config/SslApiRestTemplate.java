package project.doc.dmc_security_api.config;


import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class SslApiRestTemplate {
    public SslApiRestTemplate() {
    }

    public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();
        CloseableHttpClient client = HttpClients.custom().setSSLContext(this.getSSLDetails()).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(client);
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(requestFactory));
        return restTemplate;
    }

    private SSLContext getSSLDetails() {
        TrustStrategy acceptingTrustStrategy = (chain, authType) -> {
            return true;
        };

        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial((KeyStore)null, acceptingTrustStrategy).build();
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException var4) {
            throw new RuntimeException(String.format("Active agency code is not provided or invalid in jwt", var4.getLocalizedMessage()));
        }
    }
}
