package com.swein.shsceneform3dcode.framework.util.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

public class SHGlide {

    public static SHGlide instance = new SHGlide();
    private SHGlide() {}

    public void setImageBitmap(Context context, Bitmap bitmap, ImageView imageView, @Nullable Drawable placeHolder, int width, int height, float rate, float thumbnailSize) {

        RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap().load(bitmap).transition(withCrossFade());
        if(placeHolder != null) {
            requestBuilder = requestBuilder.placeholder(placeHolder);
        }

        if(width != 0 && height != 0) {

            if(rate != 0) {
                width = (int)((float)width * rate);
                height = (int)((float)height * rate);
            }

            requestBuilder = requestBuilder.override(width, height);
        }

        if(thumbnailSize != 0) {
            requestBuilder = requestBuilder.thumbnail(thumbnailSize);
        }

        requestBuilder.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
    }

    public void setCornerImageBitmap(Context context, Bitmap bitmap, ImageView imageView, @Nullable Drawable placeHolder, int width, int height, float rate, float thumbnailSize, float dp) {

        RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap().load(bitmap).transition(withCrossFade()).transform(new GlideRoundTransform(dp));
        if(placeHolder != null) {
            requestBuilder = requestBuilder.placeholder(placeHolder);
        }

        if(width != 0 && height != 0) {

            if(rate != 0) {
                width = (int)((float)width * rate);
                height = (int)((float)height * rate);
            }

            requestBuilder = requestBuilder.override(width, height);
        }

        if(thumbnailSize != 0) {
            requestBuilder = requestBuilder.thumbnail(thumbnailSize);
        }

        requestBuilder.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView);
    }

    public void setCornerImageBitmap(Context context, String url, ImageView imageView, @Nullable Drawable placeHolder, int width, int height, float rate, float thumbnailSize, float dp) {

        RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap().load(url).transition(withCrossFade()).transform(new GlideRoundTransform(dp));
        if(placeHolder != null) {
            requestBuilder = requestBuilder.placeholder(placeHolder);
        }

        if(width != 0 && height != 0) {

            if(rate != 0) {
                width = (int)((float)width * rate);
                height = (int)((float)height * rate);
            }

            requestBuilder = requestBuilder.override(width, height);
        }

        if(thumbnailSize != 0) {
            requestBuilder = requestBuilder.thumbnail(thumbnailSize);
        }

        requestBuilder.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView);
    }

    public void setImageBitmap(Context context, String url, ImageView imageView, @Nullable Drawable placeHolder, int width, int height, float rate, float thumbnailSize) {

        RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap().load(url).transition(withCrossFade());
        if(placeHolder != null) {
            requestBuilder = requestBuilder.placeholder(placeHolder);
        }

        if(width != 0 && height != 0) {

            if(rate != 0) {
                width = (int)((float)width * rate);
                height = (int)((float)height * rate);
            }

            requestBuilder = requestBuilder.override(width, height);
        }

        if(thumbnailSize != 0) {
            requestBuilder = requestBuilder.thumbnail(thumbnailSize);
        }

        requestBuilder.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView);

    }

    public void setImageBitmap(Context context, Uri uri, ImageView imageView, @Nullable Drawable placeHolder, int width, int height, float rate, float thumbnailSize) {

        RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap().load(uri).transition(withCrossFade());
        if(placeHolder != null) {
            requestBuilder = requestBuilder.placeholder(placeHolder);
        }

        if(width != 0 && height != 0) {

            if(rate != 0) {
                width = (int)((float)width * rate);
                height = (int)((float)height * rate);
            }

            requestBuilder = requestBuilder.override(width, height);
        }

        if(thumbnailSize != 0) {
            requestBuilder = requestBuilder.thumbnail(thumbnailSize);
        }

        requestBuilder.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView);

    }

    public void setImageBitmap(Context context, int imageResource, ImageView imageView, @Nullable Drawable placeHolder, int width, int height, float rate, float thumbnailSize) {

        RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap().load(imageResource).transition(withCrossFade());
        if(placeHolder != null) {
            requestBuilder = requestBuilder.placeholder(placeHolder);
        }

        if(width != 0 && height != 0) {

            if(rate != 0) {
                width = (int)((float)width * rate);
                height = (int)((float)height * rate);
            }

            requestBuilder = requestBuilder.override(width, height);
        }

        if(thumbnailSize != 0) {
            requestBuilder = requestBuilder.thumbnail(thumbnailSize);
        }

        requestBuilder.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView);

    }

    public void setRoundedImageBitmap(Context context, int imageResource, ImageView imageView, @Nullable Drawable placeHolder, int width, int height, float rate, float thumbnailSize) {

        RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap().load(imageResource).transition(withCrossFade());
        if(placeHolder != null) {
            requestBuilder = requestBuilder.placeholder(placeHolder);
        }

        if(width != 0 && height != 0) {

            if(rate != 0) {
                width = (int)((float)width * rate);
                height = (int)((float)height * rate);
            }

            requestBuilder = requestBuilder.override(width, height);
        }

        if(thumbnailSize != 0) {
            requestBuilder = requestBuilder.thumbnail(thumbnailSize);
        }

        requestBuilder.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(RequestOptions.circleCropTransform()).into(imageView);

    }

    public void setRoundedImageBitmap(Context context, String url, ImageView imageView, @Nullable Drawable placeHolder, int width, int height, float rate, float thumbnailSize) {

        RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap().load(url).transition(withCrossFade());
        if(placeHolder != null) {
            requestBuilder = requestBuilder.placeholder(placeHolder);
        }

        if(width != 0 && height != 0) {

            if(rate != 0) {
                width = (int)((float)width * rate);
                height = (int)((float)height * rate);
            }

            requestBuilder = requestBuilder.override(width, height);
        }

        if(thumbnailSize != 0) {
            requestBuilder = requestBuilder.thumbnail(thumbnailSize);
        }

        requestBuilder.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(RequestOptions.circleCropTransform()).into(imageView);

    }

    public void setRoundedImageBitmap(Context context, Bitmap bitmap, ImageView imageView, @Nullable Drawable placeHolder, int width, int height, float rate, float thumbnailSize) {

        RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap().load(bitmap).transition(withCrossFade());
        if(placeHolder != null) {
            requestBuilder = requestBuilder.placeholder(placeHolder);
        }

        if(width != 0 && height != 0) {

            if(rate != 0) {
                width = (int)((float)width * rate);
                height = (int)((float)height * rate);
            }

            requestBuilder = requestBuilder.override(width, height);
        }

        if(thumbnailSize != 0) {
            requestBuilder = requestBuilder.thumbnail(thumbnailSize);
        }

        requestBuilder.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(RequestOptions.circleCropTransform()).into(imageView);

    }

    public void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }
}
