package cn.noload.nas.web.rest.downloader;

import cn.noload.nas.service.downloader.HttpDownloader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DownloaderResource {

    private final HttpDownloader httpDownloader;

    public DownloaderResource(HttpDownloader httpDownloader) {
        this.httpDownloader = httpDownloader;
    }

    @GetMapping("/downloader/check")
    public ResponseEntity checkDownload(
        @RequestParam(value = "url", required = true) String url,
        @RequestParam(value = "needProxy", required = true) Boolean needProxy
    ) {
        return ResponseEntity.ok(httpDownloader.concurrentAble(url, needProxy));
    }
}
