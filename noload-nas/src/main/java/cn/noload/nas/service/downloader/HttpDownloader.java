package cn.noload.nas.service.downloader;

import cn.noload.nas.domain.DownloadCheck;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class HttpDownloader extends Downloader {


    public HttpDownloader(
        /*
        @Qualifier("proxy-download")
        RestTemplate proxyRestTemplate,
        */
        @Qualifier("vanillaRestTemplate")
        RestTemplate normalRestTemplate,
        HttpHeaders idmHeaders) {
//        super(proxyRestTemplate, normalRestTemplate, idmHeaders);
        super(null, normalRestTemplate, idmHeaders);
    }

    public void download() {

    }

    private static RestTemplate proxy() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8888));
        requestFactory.setProxy(proxy);
        return new RestTemplate(requestFactory);
    }

    @Override
    public DownloadCheck concurrentAble(String url, boolean needProxy) {
        RestTemplate restTemplate = needProxy ? proxyRestTemplate : normalRestTemplate;
        HttpEntity<String> requestEntity = new HttpEntity<>(null, idmHeaders);
        ResponseEntity<String> response;
        // 第一遍发送 HEAD 请求, 获取 Content-Length MD5 file-name 等参数
        try {
            response = restTemplate.exchange(URI.create(url), HttpMethod.HEAD, requestEntity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return DownloadCheck.error();
        }

        HttpHeaders responseHeaders = response.getHeaders();
        if(responseHeaders.getContentLength() > 0) {
            // 响应头包含 Content-Length, 尝试请求第一次, 仍然是 HEAD 请求
            HttpEntity<String> firstRequestEntity = new HttpEntity<>(null, firstRangeHeaders());
            ResponseEntity<String> firstResponse = null;
            try {
                firstResponse = restTemplate.exchange(URI.create(url), HttpMethod.HEAD, firstRequestEntity, String.class);
                HttpHeaders firstHeaders = firstResponse.getHeaders();
                String fileName = null;

                if(firstHeaders.containsKey("Content-Disposition")
                    && ((String) firstHeaders.getFirst("Content-Disposition")).contains("filename")) {
                    String disposition = firstHeaders.getFirst("Content-Disposition");
                    fileName = disposition.replaceAll(".*filename=", "").replaceAll("\"", "");
                } else {
                    String path = url.split("\\?")[0];
                    fileName = path.split("/")[path.split("/").length - 1];
                }
                String md5 = firstHeaders.getFirst("Content-MD5");
                Base64.getDecoder().decode(md5);
                return new DownloadCheck()
                    .setContentLength(firstHeaders.getContentLength())
                    .setFileName(fileName)
                    .setMd5(firstHeaders.getFirst("Content-MD5"));
            } catch (Exception e) {
                // 不支持分段
                return new DownloadCheck()
                    .setMultAble(false);
            }
        }
        return new DownloadCheck().setMultAble(false);
    }

    //
//    public static void main(String[] args) throws IOException {
////        RestTemplate restTemplate = proxy();
//        RestTemplate restTemplate = new RestTemplate();
//// Optional Accept header
//        RequestCallback requestCallback = request -> request.getHeaders()
//            .setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
//        RequestCallback headCallBack = request -> request.getHeaders()
//            .setAccept(Arrays.asList(MediaType.ALL));
////        RequestCallback headCallBack = request -> {};
//        ResponseExtractor<Void> headExtractor = response -> {
//            System.out.println(response);
//            return null;
//        };
//// Streams the response instead of loading it all in memory
//        ResponseExtractor<Void> responseExtractor = response -> {
//            // Here I write the response to a file but do what you like
//            Path path = Paths.get("J:\\iso\\file.iso");
//            Files.copy(response.getBody(), path);
//            return null;
//        };
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Accept", "*/*");
//        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Trident/7.0; rv:11.0) like Gecko");
//        headers.set("Accept-Language", "zh-CN");
//        headers.set("Accept-Encoding", "identity");
//        headers.set("Accept-Charset", "*");
//        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(
//            URI.create("https://download2.vmware.com/software/esx/67/VMware-VMvisor-Installer-6.7.0-8169922.x86_64.iso?HashKey=157addc93d1b47e46b4d74cfb61dbc27&params=%7B%22custnumber%22%3A%22dHRwcHBkcCVlag%3D%3D%22%2C%22languagecode%22%3A%22cn%22%2C%22source%22%3A%22EVALS%22%2C%22downloadtype%22%3A%22manual%22%2C%22downloaduuid%22%3A%220b6ddac8-6c09-4eb4-b556-47e9225fe3f4%22%7D&AuthKey=1536407649_0bbadfb271bc1e10780ae52af5f94e37"),
//            HttpMethod.HEAD, requestEntity, String.class);
//        System.out.println(response);
//
////        restTemplate.execute(URI.create("https://download2.vmware.com/software/esx/67/VMware-VMvisor-Installer-6.7.0-8169922.x86_64.iso?HashKey=4b169c480a46fc859c053178ed31c747&params=%7B%22custnumber%22%3A%22dHRwcHBkcCVlag%3D%3D%22%2C%22languagecode%22%3A%22cn%22%2C%22source%22%3A%22EVALS%22%2C%22downloadtype%22%3A%22manual%22%2C%22downloaduuid%22%3A%2222c3d68b-c2fc-4940-98b9-17b2b1ee312a%22%7D&AuthKey=1536328017_afe0cb138a3a1feaee8b8d2615e52d56"), HttpMethod.GET, requestCallback, responseExtractor);
////        restTemplate.execute("https://download2.vmware.com/software/esx/67/VMware-VMvisor-Installer-6.7.0-8169922.x86_64.iso?HashKey=f9c4076a601167b811ec5294949709bb&params=%7B%22custnumber%22%3A%22dHRwcHBkcCVlag%3D%3D%22%2C%22languagecode%22%3A%22cn%22%2C%22source%22%3A%22EVALS%22%2C%22downloadtype%22%3A%22manual%22%2C%22downloaduuid%22%3A%22dac0b7c2-91cd-4aba-b81f-adbac2c3f8a7%22%7D&AuthKey=1536405118_58e454e0a3d62eaa1e2e72b59fdadc98",
////            HttpMethod.HEAD, headCallBack, headExtractor);
////        HttpHeaders headers = restTemplate.headForHeaders("https://download2.vmware.com/software/esx/67/VMware-VMvisor-Installer-6.7.0-8169922.x86_64.iso?HashKey=9a3450baef5188da5aff8aecbbefa440&params=%7B%22custnumber%22%3A%22dHRwcHBkcCVlag%3D%3D%22%2C%22languagecode%22%3A%22cn%22%2C%22source%22%3A%22EVALS%22%2C%22downloadtype%22%3A%22manual%22%2C%22downloaduuid%22%3A%22b28d3d81-ebe8-415d-99b7-dad4c3f096c4%22%7D&AuthKey=1536332607_d127f261d96aa1c07e0b83fa525c0b06");
////        System.out.println(headers);
//    }
}
