package com.irfankhoirul.apps.imageretrieval.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Irfan Khoirul on 6/29/2016.
 */
public class BitmapUtils {
    private Bitmap bitmap;

    public BitmapUtils(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * Encode Bitmap to File
     *
     * @param quality
     * @return
     */
    public static void encode(AppCompatActivity activity, File file, Bitmap newBitmap, int quality) {
        OutputStream os;
        try {
            os = new FileOutputStream(file);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("BitmapUtils", "Error writing bitmap", e);
        }
        galleryAddPic(activity, file);
    }

    public static Bitmap centerCropping(Bitmap bitmap, int widthReduced, int heightReduced) {
        widthReduced = widthReduced < 0 ? 0 : widthReduced;
        heightReduced = heightReduced < 0 ? 0 : heightReduced;

        Bitmap bitmapCropped = Bitmap.createBitmap(bitmap,
                widthReduced / 2, heightReduced / 2,
                bitmap.getWidth() - widthReduced, bitmap.getHeight() - heightReduced);
        return bitmapCropped;
    }

    public static Bitmap setPic(String path, int width, int height) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        Log.v("setPic", "photoW: " + photoW +
                "||photoH: " + photoH +
                "||mimeType: " + bmOptions.outMimeType);

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((width > 0) || (height > 0)) {
            scaleFactor = Math.min(photoW / width, photoH / height);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        // pertama kali image dimasukkan ke bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        return CameraUtils.rotateImageIfRequired(path, bitmap);
    }

    public static void galleryAddPic(AppCompatActivity activity, File file) {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        activity.sendBroadcast(mediaScanIntent);
    }

    public static void saveBitmapToFile(AppCompatActivity activity, Bitmap bitmap, String fileName, int quality) {
        try {
            File file = ImageFilePathUtils.createImageFile(fileName);
            encode(activity, file, bitmap, quality);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resize Bitmap by width
     *
     * @param width
     * @return
     */
    public Bitmap resize(int width) {
        final int bitmapWidth = bitmap.getWidth();
        final int bitmapHeight = bitmap.getHeight();
        if (bitmapWidth > width) {
            final float height = (float) width / (float) bitmapWidth * (float) bitmapHeight;
            Log.v("CHECK SIZE", width + "|" + height + "||" + bitmapWidth + "|" + bitmapHeight);
            final Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, width, Math.round(height), true);
            return newBitmap;
        } else {
            return bitmap;
        }
    }
}
