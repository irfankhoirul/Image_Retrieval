package com.irfankhoirul.apps.imageretrieval.model;

/**
 * Created by Irfan Khoirul on 6/25/2016.
 */
public class Image {
    private String name;
    private String link;
    private double[] mean; // 0 : red, 1 : green, 2 : blue
    private double[] std; // 0 : red, 1 : green, 2 : blue
    private double[] median; // 0 : red, 1 : green, 2 : blue

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public double[] getMean() {
        return mean;
    }

    public void setMean(double[] mean) {
        this.mean = mean;
    }

    public double[] getStd() {
        return std;
    }

    public void setStd(double[] std) {
        this.std = std;
    }

    public double[] getMedian() {
        return median;
    }

    public void setMedian(double[] median) {
        this.median = median;
    }
}