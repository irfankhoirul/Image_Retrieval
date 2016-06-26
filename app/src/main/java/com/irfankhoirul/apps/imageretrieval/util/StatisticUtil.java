package com.irfankhoirul.apps.imageretrieval.util;

import java.util.Collections;
import java.util.List;

/**
 * Created by Irfan Khoirul on 6/26/2016.
 */
public class StatisticUtil {
    private List<Integer> data;
    int size;

    public StatisticUtil(List<Integer> data) {
        this.data = data;
        size = data.size();
    }

    public double getMean() {
        double sum = 0.0;
        for (double a : data)
            sum += a;
        return sum / size;
    }

    public double getVariance() {
        double mean = getMean();
        double temp = 0;
        for (double a : data)
            temp += (mean - a) * (mean - a);
        return temp / size;
    }

    public double getStdDev() {
        return Math.sqrt(getVariance());
    }

    public double getMedian() {
        Collections.sort(data);

        if (data.size() % 2 == 0) {
            return (data.get((data.size() / 2) - 1) + data.get(data.size() / 2)) / 2.0;
        } else {
            return data.get(data.size() / 2);
        }
    }
}