package com.irfankhoirul.apps.imageretrieval;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.irfankhoirul.apps.imageretrieval.data.Data;
import com.irfankhoirul.apps.imageretrieval.model.Image;
import com.irfankhoirul.apps.imageretrieval.util.BitmapUtils;
import com.irfankhoirul.apps.imageretrieval.util.CameraUtils;
import com.irfankhoirul.apps.imageretrieval.util.ImageFilePathUtils;
import com.irfankhoirul.apps.imageretrieval.util.StatisticUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE = 1;
    private static final int SELECT_CAMERA_CAPTURE = 2;

//    private static String URL = "https://pixabay.com/static/uploads/photo/2016/01/19/14/58/rocky-mountains-1149108_960_720.jpg";

    @BindView(R.id.imgSource)
    ImageView imgSource;
    @BindView(R.id.btLoadData)
    Button btLoadData;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btTestData)
    Button btTestData;
    @BindView(R.id.imgDataTest)
    ImageView imgDataTest;

    private List<Integer> reds = new ArrayList<>();
    private List<Integer> greens = new ArrayList<>();
    private List<Integer> blues = new ArrayList<>();

    private List<String> imageURLs;
    private int counter = 0;

    private String URL;

    private RealmConfiguration realmConfiguration;
    private Realm realm;

    private File file;
    private String path;
    private Bitmap bitmapOri;

    private Image dataTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
        realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);

        // Get a Realm instance for this thread
        realm = Realm.getDefaultInstance();

//        RealmResults<Image> results = realm.where(Image.class).findAll();
//        realm.beginTransaction();
//        results.deleteAllFromRealm();
//        realm.commitTransaction();

/*------------------------------------------------------------------------------------------------*/
/*
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
                        getImageElement(bitmap);
                    }

                    @Override
                    public void onError() {

                    }
                });
*/
    }

    private Image getImageElement(Bitmap bitmap, String url) {
        reds.clear();
        greens.clear();
        blues.clear();

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
        StatisticUtil greenStatistics = new StatisticUtil(greens);
        StatisticUtil blueStatistics = new StatisticUtil(blues);

        Image tmpImage = new Image();
        tmpImage.setUrl(url);
        tmpImage.setMeanRed(redStatistics.getMean());
        tmpImage.setMedianRed(redStatistics.getMedian());
        tmpImage.setStdRed(redStatistics.getStdDev());

        tmpImage.setMeanGreen(greenStatistics.getMean());
        tmpImage.setMedianGreen(greenStatistics.getMedian());
        tmpImage.setStdGreen(greenStatistics.getStdDev());

        tmpImage.setMeanBlue(blueStatistics.getMean());
        tmpImage.setMedianBlue(blueStatistics.getMedian());
        tmpImage.setStdBlue(blueStatistics.getStdDev());

        if (url == null) {
            btTestData.setVisibility(View.VISIBLE);
        }

        return tmpImage;
    }

    @OnClick(R.id.btLoadData)
    public void btLoadData() {
        progressBar.setVisibility(View.VISIBLE);
        imageURLs = Data.imageUrlList;
        Log.v("ImageUrlListSize", String.valueOf(imageURLs.size()));

        loadImage();
    }

    private void loadImage() {
        Log.v("LoadImage", String.valueOf(counter));
        imgSource.setImageDrawable(null);
        URL = imageURLs.get(counter);
        progressBar.setVisibility(View.VISIBLE);
        Picasso.with(this)
                .load(URL)
                .placeholder(R.drawable.blue_circle)
                .resize(512, 512)
//                .centerCrop()
                .into(imgSource, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                        Bitmap bitmap = ((BitmapDrawable) imgSource.getDrawable()).getBitmap();
                        Image tmpImage = getImageElement(bitmap, URL);
                        Log.v("ImageElement", tmpImage.toString());

                        // Persist your data in a transaction
                        realm.beginTransaction();
                        try {
                            Image image = realm.copyToRealm(tmpImage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        realm.commitTransaction();

                        RealmResults<Image> dbImage = realm.where(Image.class).findAll();
                        Log.v("DBImageSize", String.valueOf(dbImage.size()));

                        if (counter < imageURLs.size() - 1) {
                            counter++;
                            loadImage();
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });

    }

    @OnClick(R.id.imgDataTest)
    public void imgimgDataTestSource() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Get photo with:");
        builder.setItems(R.array.get_photo_method, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:     // camera
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        try {
                            file = ImageFilePathUtils.createImageFile(".jpg");
                            path = file.getAbsolutePath();
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        } catch (IOException e) {
                            e.printStackTrace();
                            file = null;
                            path = null;
                        }

                        startActivityForResult(takePictureIntent, SELECT_CAMERA_CAPTURE);
                        break;
                    case 1:     // gallery
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, SELECT_IMAGE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressBar.setVisibility(View.VISIBLE);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE) {
                if (data != null) {
                    path = ImageFilePathUtils.getPath(this, data.getData());
                    Log.v("cek_path", path);
                    bitmapOri = CameraUtils.rotateImageIfRequired(path, BitmapFactory.decodeFile(path));
//                    imgDataTest.setImageBitmap(bitmapOri);

                    final Uri uri = Uri.fromFile(new File(path));
                    Picasso.with(this)
                            .load(uri)
                            .placeholder(R.drawable.blue_circle)
                            .resize(512, 512)
//                          .centerCrop()
                            .into(imgDataTest, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap bitmap = ((BitmapDrawable) imgDataTest.getDrawable()).getBitmap();
                                    dataTest = getImageElement(bitmap, uri.toString());
                                    Log.v("DataTestElement", dataTest.toString());
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {

                                }
                            });

                }
            } else if (requestCode == SELECT_CAMERA_CAPTURE) {
                Log.v("Data", data.getData().toString());
                if (path != null) {
                    Log.v("cek_path", path);
                    BitmapUtils.galleryAddPic(this, file);
                    bitmapOri = CameraUtils.rotateImageIfRequired(path, BitmapFactory.decodeFile(path));
//                    dataTest = getImageElement(bitmapOri, null);
//                    imgDataTest.setImageBitmap(bitmapOri);
                    final Uri uri = Uri.fromFile(new File(path));
                    Picasso.with(this)
                            .load(uri)
                            .placeholder(R.drawable.blue_circle)
                            .resize(512, 512)
//                          .centerCrop()
                            .into(imgDataTest, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap bitmap = ((BitmapDrawable) imgDataTest.getDrawable()).getBitmap();
                                    dataTest = getImageElement(bitmap, uri.toString());
                                    Log.v("DataTestElement", dataTest.toString());
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {

                                }
                            });
                }
            }
        } else {
            file = null;
            path = null;
            progressBar.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btTestData)
    public void btTestData() {
        List<Image> tmpData = new ArrayList<>();

        RealmResults<Image> imageData = realm.where(Image.class).findAll();
        for (int i = 0; i < imageData.size(); i++) {
            Log.v("RealData[" + i + "]", imageData.get(i).toString());
        }

        for (int i = 0; i < imageData.size(); i++) {
            Image tmpImage = new Image();
            tmpImage.setUrl(imageData.get(i).getUrl());
            tmpImage.setMeanRed(imageData.get(i).getMeanRed());
            tmpImage.setMedianRed(imageData.get(i).getMedianRed());
            tmpImage.setStdRed(imageData.get(i).getStdRed());

            tmpImage.setMeanGreen(imageData.get(i).getMeanGreen());
            tmpImage.setMedianGreen(imageData.get(i).getMedianGreen());
            tmpImage.setStdGreen(imageData.get(i).getStdGreen());

            tmpImage.setMeanBlue(imageData.get(i).getMeanBlue());
            tmpImage.setMedianBlue(imageData.get(i).getMedianBlue());
            tmpImage.setStdBlue(imageData.get(i).getStdBlue());

            tmpData.add(tmpImage);
        }

        for (int i = 0; i < tmpData.size(); i++) {
            double distance = Math.sqrt(
                    Math.pow((tmpData.get(i).getMeanRed() - dataTest.getMeanRed()), 2) +
                            Math.pow((tmpData.get(i).getMeanGreen() - dataTest.getMeanGreen()), 2) +
                            Math.pow((tmpData.get(i).getMeanBlue() - dataTest.getMeanBlue()), 2) +
                            Math.pow((tmpData.get(i).getStdRed() - dataTest.getStdRed()), 2) +
                            Math.pow((tmpData.get(i).getStdGreen() - dataTest.getStdGreen()), 2) +
                            Math.pow((tmpData.get(i).getStdBlue() - dataTest.getStdBlue()), 2) +
                            Math.pow((tmpData.get(i).getMedianRed() - dataTest.getMedianRed()), 2) +
                            Math.pow((tmpData.get(i).getMedianGreen() - dataTest.getMedianGreen()), 2) +
                            Math.pow((tmpData.get(i).getMedianBlue() - dataTest.getMedianBlue()), 2));

            tmpData.get(i).setDistance(distance);
        }

        Collections.sort(tmpData, new Comparator<Image>() {
            @Override
            public int compare(Image p1, Image p2) {
                double result = (p1.getDistance() - p2.getDistance());
                if (result < 0)
                    return -1;
                else if (result == 0)
                    return 0;
                else
                    return 1;
            }
        });


        ArrayList<String> strImageList = new ArrayList<>();
        for (int i = 0; i < tmpData.size(); i++) {
            Log.v("Data[" + i + "]", tmpData.get(i).getUrl() + " | " + tmpData.get(i).getDistance());

            String tmpStrImage = new Gson().toJson(tmpData.get(i));
            strImageList.add(tmpStrImage);
        }

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("dataTest", new Gson().toJson(dataTest));
        intent.putStringArrayListExtra("images", strImageList);
        startActivity(intent);
    }
}