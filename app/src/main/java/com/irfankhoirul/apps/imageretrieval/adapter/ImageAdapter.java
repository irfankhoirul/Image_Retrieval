package com.irfankhoirul.apps.imageretrieval.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.irfankhoirul.apps.imageretrieval.R;
import com.irfankhoirul.apps.imageretrieval.model.Image;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Irfan Khoirul on 6/29/2016.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;

    private List<Image> images;

    public ImageAdapter(List<Image> images, Context context) {
        this.context = context;
        this.images = images;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Image item = images.get(position);
        Picasso.with(context)
                .load(item.getUrl())
                .resize(512, 512)
//                .centerCrop()
                .into(holder.imgItem);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgItem)
        ImageView imgItem;

        public ImageViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
