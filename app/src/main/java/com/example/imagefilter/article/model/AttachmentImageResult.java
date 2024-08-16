package com.example.imagefilter.article.model;

public class AttachmentImageResult implements BaseDialogResult {
    private final String url;

    public AttachmentImageResult(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
