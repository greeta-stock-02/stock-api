package net.greeta.stock.gateway.config;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.ssl.SSLContexts;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.ssl.TrustStrategy;
import feign.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public Client feignClient()
    {
        Client trustSSLSockets = new Client.Default(getSSLSocketFactory(), new NoopHostnameVerifier());
        return trustSSLSockets;
    }

    private SSLSocketFactory getSSLSocketFactory() {
        try {
            TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            };

            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            return sslContext.getSocketFactory();
        } catch (Exception exception) {
        }
        return null;
    }
}