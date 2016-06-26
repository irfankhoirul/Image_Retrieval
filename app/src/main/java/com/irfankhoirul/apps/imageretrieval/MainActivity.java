package com.irfankhoirul.apps.imageretrieval;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.irfankhoirul.apps.imageretrieval.util.StatisticUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static String URL = "https://pixabay.com/static/uploads/photo/2016/01/19/14/58/rocky-mountains-1149108_960_720.jpg";

    @BindView(R.id.imgSource)
    ImageView imgSource;

    private List<Integer> reds = new ArrayList<>();
    private List<Integer> greens = new ArrayList<>();
    private List<Integer> blues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Firebase rootRef = new Firebase("https://docs-examples.firebaseio.com/web/data");


        Log.v("Timestamp-LoadingImage", String.valueOf(new Date().getTime()));
        Picasso.with(this)
                .load(URL)
                .resize(512, 512)
//                .centerCrop()
                .into(imgSource, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.v("Timestamp-Processing", String.valueOf(new Date().getTime()));
                        Bitmap bitmap = ((BitmapDrawable) imgSource.getDrawable()).getBitmap();
                        getColot(bitmap);
                    }

                    @Override
                    public void onError() {

                    }
                });


    }

    private void getColot(Bitmap bitmap) {
        Log.v("ImageSize", "Width[" + bitmap.getWidth() + "]Height[" + bitmap.getHeight() + "]");
        for (int i = 0; i < bitmap.getHeight(); i++) {
            for (int j = 0; j < bitmap.getWidth(); j++) {
                int red = Color.red(bitmap.getPixel(i, j));
                int green = Color.green(bitmap.getPixel(i, j));
                int blue = Color.blue(bitmap.getPixel(i, j));

                reds.add(red);
                greens.add(green);
                blues.add(blue);
            }
        }

        StatisticUtil redStatistics = new StatisticUtil(reds);
        Log.v("Red-Mean", String.valueOf(redStatistics.getMean()));
        Log.v("Red-Median", String.valueOf(redStatistics.getMedian()));
        Log.v("Red-StDev", String.valueOf(redStatistics.getStdDev()));

        StatisticUtil greenStatistics = new StatisticUtil(greens);
        Log.v("Green-Mean", String.valueOf(greenStatistics.getMean()));
        Log.v("Green-Median", String.valueOf(greenStatistics.getMedian()));
        Log.v("Green-StDev", String.valueOf(greenStatistics.getStdDev()));

        StatisticUtil blueStatistics = new StatisticUtil(blues);
        Log.v("Blue-Mean", String.valueOf(blueStatistics.getMean()));
        Log.v("Blue-Median", String.valueOf(blueStatistics.getMedian()));
        Log.v("Blue-StDev", String.valueOf(blueStatistics.getStdDev()));

        Log.v("Timestamp-Complete", String.valueOf(new Date().getTime()));
    }
}