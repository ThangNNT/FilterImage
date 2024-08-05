package com.example.imagefilter;
public class FilterItem {
    private float saturation;
    private float brightness;
    private float warmth;
    private float contrast;

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    FilterItem(String name, float saturation, float brightness, float warmth, float contrast){
        this.name = name;
        this.saturation = saturation;
        this.brightness = brightness;
        this.warmth = warmth;
        this.contrast = contrast;
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public float getWarmth() {
        return warmth;
    }

    public void setWarmth(float warmth) {
        this.warmth = warmth;
    }

    public float getContrast() {
        return contrast;
    }

    public void setContrast(float contrast) {
        this.contrast = contrast;
    }

    public String getName() {
        return name;
    }
}
