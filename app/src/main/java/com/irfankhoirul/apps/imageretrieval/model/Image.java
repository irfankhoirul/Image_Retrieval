package com.irfankhoirul.apps.imageretrieval.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Irfan Khoirul on 6/25/2016.
 */
public class Image extends RealmObject {
    @PrimaryKey
    private String url;
    private double meanRed;
    private double meanGreen;
    private double meanBlue;
    private double stdRed;
    private double stdGreen;
    private double stdBlue;
    private double medianRed;
    private double medianGreen;
    private double medianBlue;

    private double distance;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getMeanRed() {
        return meanRed;
    }

    public void setMeanRed(double meanRed) {
        this.meanRed = meanRed;
    }

    public double getMeanGreen() {
        return meanGreen;
    }

    public void setMeanGreen(double meanGreen) {
        this.meanGreen = meanGreen;
    }

    public double getMeanBlue() {
        return meanBlue;
    }

    public void setMeanBlue(double meanBlue) {
        this.meanBlue = meanBlue;
    }

    public double getStdRed() {
        return stdRed;
    }

    public void setStdRed(double stdRed) {
        this.stdRed = stdRed;
    }

    public double getStdGreen() {
        return stdGreen;
    }

    public void setStdGreen(double stdGreen) {
        this.stdGreen = stdGreen;
    }

    public double getStdBlue() {
        return stdBlue;
    }

    public void setStdBlue(double stdBlue) {
        this.stdBlue = stdBlue;
    }

    public double getMedianRed() {
        return medianRed;
    }

    public void setMedianRed(double medianRed) {
        this.medianRed = medianRed;
    }

    public double getMedianGreen() {
        return medianGreen;
    }

    public void setMedianGreen(double medianGreen) {
        this.medianGreen = medianGreen;
    }

    public double getMedianBlue() {
        return medianBlue;
    }

    public void setMedianBlue(double medianBlue) {
        this.medianBlue = medianBlue;
    }

    @Override
    public String toString() {
        String str = "";
        str += "URL : " + url + "\n";
        str += "meanRed : " + meanRed + "\n";
        str += "meanGreen : " + meanGreen + "\n";
        str += "meanBlue : " + meanBlue + "\n";
        str += "stdRed : " + stdRed + "\n";
        str += "stdGreen : " + stdGreen + "\n";
        str += "stdBlue : " + stdBlue + "\n";

        str += "medianRed : " + medianRed + "\n";
        str += "medianGreen : " + medianGreen + "\n";
        str += "medianBlue : " + medianBlue + "\n\n";

        return str;

    }
}