package cn.noload.nas.domain;


public class DownloadCheck {

    private Boolean multAble;
    private String md5;
    private Long contentLength;
    private String fileName;

    public DownloadCheck() {}

    public DownloadCheck(Boolean multAble, String md5, Long contentLength, String fileName) {
        this.multAble = multAble;
        this.md5 = md5;
        this.contentLength = contentLength;
        this.fileName = fileName;
    }

    public static DownloadCheck error() {
        DownloadCheck error = new DownloadCheck();
        error.multAble = false;
        return error;
    }

    public Boolean getMultAble() {
        return multAble;
    }

    public DownloadCheck setMultAble(Boolean multAble) {
        this.multAble = multAble;
        return this;
    }

    public String getMd5() {
        return md5;
    }

    public DownloadCheck setMd5(String md5) {
        this.md5 = md5;
        return this;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public DownloadCheck setContentLength(Long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public DownloadCheck setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
}
