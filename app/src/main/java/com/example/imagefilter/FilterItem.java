package com.example.imagefilter;

/**
 * Please check ImageFilterView document below for more information <br/>
 * <a href="https://developer.android.com/reference/androidx/constraintlayout/utils/widget/ImageFilterView">docs</a>
 */
public class FilterItem {
    private float saturation;
    private float brightness;
    private float warmth;
    private float contrast;

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    /**
     *
     * @param name Name of filter style
     * @param saturation From 0.0 to 2.0 (0 = grayscale, 1 = original, 2 = hyper saturated)
     * @param brightness From 0.0 to 2.0 (0 = black, 1 = original, 2 = twice as bright)
     * @param warmth This adjust the apparent color temperature of the image. 1=neutral, 2=warm, 0.5=cold
     * @param contrast From 0.0 to 2.0 (1 = unchanged, 0 = gray, 2 = high contrast)
     */
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
