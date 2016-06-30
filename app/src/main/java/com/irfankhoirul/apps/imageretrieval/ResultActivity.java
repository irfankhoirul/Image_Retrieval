package com.irfankhoirul.apps.imageretrieval;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.irfankhoirul.apps.imageretrieval.adapter.ImageAdapter;
import com.irfankhoirul.apps.imageretrieval.model.Image;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultActivity extends AppCompatActivity {

    @BindView(R.id.rvImageResult)
    RecyclerView rvImageResult;
    @BindView(R.id.imgDataTest)
    ImageView imgDataTest;
    @BindView(R.id.svContainer)
    ScrollView svContainer;

    ImageAdapter imageAdapter;

    ArrayList<String> strImageList = new ArrayList<>();
    List<Image> images = new ArrayList<>();

    Image dataTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Glitzee Search Result");
        strImageList = getIntent().getStringArrayListExtra("images");
        for(int i=0; i<strImageList.size(); i++){
            Image tmpImage = new Gson().fromJson(strImageList.get(i), Image.class);
            images.add(tmpImage);
        }

        dataTest = new Gson().fromJson(getIntent().getStringExtra("dataTest"), Image.class);
        Log.v("DataTestUrl", dataTest.getUrl());
        Picasso.with(this)
                .load(Uri.parse(dataTest.getUrl()))
                .placeholder(R.drawable.blue_circle)
                .resize(512, 512)
//                          .centerCrop()
                .into(imgDataTest);

        imageAdapter = new ImageAdapter(images, this);
        rvImageResult.setItemAnimator(new DefaultItemAnimator());
        rvImageResult.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvImageResult.setLayoutManager(layoutManager);
        rvImageResult.setAdapter(imageAdapter);

        svContainer.smoothScrollTo(0, 0);

    }
}
