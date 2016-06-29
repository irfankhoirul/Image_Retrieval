package com.irfankhoirul.apps.imageretrieval;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.irfankhoirul.apps.imageretrieval.adapter.ImageAdapter;
import com.irfankhoirul.apps.imageretrieval.model.Image;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultActivity extends AppCompatActivity {

    @BindView(R.id.rvImageResult)
    RecyclerView rvImageResult;

    ImageAdapter imageAdapter;

    ArrayList<String> strImageList = new ArrayList<>();
    List<Image> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);


        strImageList = getIntent().getStringArrayListExtra("images");
        for(int i=0; i<strImageList.size(); i++){
            Image tmpImage = new Gson().fromJson(strImageList.get(i), Image.class);
            images.add(tmpImage);
        }

        imageAdapter = new ImageAdapter(images, this);
        rvImageResult.setItemAnimator(new DefaultItemAnimator());
        rvImageResult.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvImageResult.setLayoutManager(layoutManager);
        rvImageResult.setAdapter(imageAdapter);

    }
}
