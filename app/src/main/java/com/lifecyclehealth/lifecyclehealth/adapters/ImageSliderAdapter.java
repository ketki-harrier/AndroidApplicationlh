package com.lifecyclehealth.lifecyclehealth.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.lifecyclehealth.lifecyclehealth.activities.Carousal;
import com.squareup.picasso.Picasso;



import java.util.ArrayList;
import java.util.List;

public class ImageSliderAdapter extends PagerAdapter {
    private List<Object> imageUrls, buttonActions;
    private final List<Target> targets = new ArrayList<>();
    Context context;
    //private Carousal context;

    public ImageSliderAdapter(List<Object> imageUrls, Context context) {
        this.imageUrls = imageUrls;
     //   this.buttonActions = buttonActions;
       this.context = context;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Target target = new Target() {
            @Override
            public void onStart() {

            }

            @Override
            public void onStop() {

            }

            @Override
            public void onDestroy() {

            }

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {

            }

            @Override
            public void onResourceReady(Object resource, Transition transition) {

            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }

            @Override
            public void getSize(SizeReadyCallback cb) {

            }

            @Override
            public void removeCallback(SizeReadyCallback cb) {

            }

            @Override
            public void setRequest(@Nullable Request request) {

            }

            @Nullable
            @Override
            public Request getRequest() {
                return null;
            }

         /*   @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imageView.setImageBitmap(bitmap);
                targets.remove(this);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                targets.remove(this);
            }


            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }*/
        };
        targets.add(target);
        imageView.setTag(target);
        if (imageUrls != null && imageUrls.get(position) != null) {
            container.addView(imageView);

            Glide.with(context)
                    .asBitmap()
                    .load(imageUrls.get(position).toString())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            imageView.setImageBitmap(resource);
                        }
                    });
   /*         Picasso.with(context).load(imageUrls.get(position).toString())
                    : imageUrls.get(position)).resize(480, 620)
                    .into(target);*/
        }


         SimpleTarget targetnew = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                // do something with the bitmap
                // set it to an ImageView
                imageView.setImageBitmap(bitmap);
            }
        };
        return imageView;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}