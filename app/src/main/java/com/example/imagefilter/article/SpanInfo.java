package com.example.imagefilter.article;

public class SpanInfo {
    private int start;
    private int end;
    private boolean bold;
    private boolean italic;
    private boolean underline;
    private boolean isLink;
    private String link;

    public SpanInfo(int start, int end, boolean bold, boolean italic, boolean underline, boolean isLink, String link) {
        this.start = start;
        this.end = end;
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
        this.isLink = isLink;
        this.link = link;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    public boolean isLink() {
        return isLink;
    }

    public void setLink(boolean link) {
        isLink = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isNormal(){
        return !(isLink && bold && underline && italic);
    }

    @Override
    public SpanInfo clone(){
        return new SpanInfo(start, end, bold, italic, underline, isLink, link);
    }
}
