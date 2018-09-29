package cn.noload.nas.service.downloader;

import cn.noload.nas.domain.DownloadCheck;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Base64;

public abstract class Downloader {

    protected static long _1GB = 1 << 30;

    protected long contentLength;
    protected String fileName;
    protected String MD5;

    protected String url;

    protected final RestTemplate proxyRestTemplate;
    protected final RestTemplate normalRestTemplate;
    protected final HttpHeaders idmHeaders;


    public Downloader(
        @Qualifier("proxy-download")
        RestTemplate proxyRestTemplate,
        @Qualifier("vanillaRestTemplate")
        RestTemplate normalRestTemplate,
        @Qualifier("idm-headers")
            HttpHeaders idmHeaders
    ) {
        this.proxyRestTemplate = proxyRestTemplate;
        this.normalRestTemplate = normalRestTemplate;
        this.idmHeaders = idmHeaders;
    }

    /**
     * 生成第一次尝试获取的请求
     * */
    protected HttpHeaders firstRangeHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Trident/7.0; rv:11.0) like Gecko");
        headers.set("Accept-Language", "zh-CN");
        headers.set("Accept-Encoding", "identity");
        headers.set("Accept-Charset", "*");
        headers.set("Range", "bytes=0-1");
        return headers;
    }

    public abstract DownloadCheck concurrentAble(String url, boolean needProxy);
}
