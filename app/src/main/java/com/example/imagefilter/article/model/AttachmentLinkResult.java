package com.example.imagefilter.article.model;

public class AttachmentLinkResult implements BaseDialogResult {
    private final String url;
    private final String text;

    public AttachmentLinkResult(String url, String text) {
        this.url = url;
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public String getText() {
        return text;
    }
}