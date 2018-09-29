package cn.noload.nas.config;


import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DownloaderConfiguration {

    private final ApplicationProperties.SocksProxy socksProxy;

    public DownloaderConfiguration(
        ApplicationProperties applicationProperties
    ) {
        this.socksProxy = applicationProperties.getSocksProxy();
    }

    /**
     * 配置 socks 代理的 RestTemplate
     * */
    /*
    @Bean
    @Qualifier("proxy-download")
    public RestTemplate download() {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
            new AuthScope(socksProxy.getHost(), socksProxy.getPort()),
            new UsernamePasswordCredentials(null, socksProxy.getPassword()));

        HttpHost myProxy = new HttpHost(socksProxy.getHost(), socksProxy.getPort());
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();

        clientBuilder.setProxy(myProxy).setDefaultCredentialsProvider(credsProvider).disableCookieManagement();

        HttpClient httpClient = clientBuilder.build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);

        return new RestTemplate(factory);
    }
    */


    /**
     * 仿照 IDM 设置头
     * */
    @Bean
    @Qualifier("idm-headers")
    public HttpHeaders imitationIDMHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Trident/7.0; rv:11.0) like Gecko");
        headers.set("Accept-Language", "zh-CN");
        headers.set("Accept-Encoding", "identity");
        headers.set("Accept-Charset", "*");
        return headers;
    }
}
