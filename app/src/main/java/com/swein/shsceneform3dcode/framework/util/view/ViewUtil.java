package com.swein.shsceneform3dcode.framework.util.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.swein.shsceneform3dcode.R;

public class ViewUtil {

    public static View inflateView(Context context, int resource, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(resource, viewGroup);
    }

    public static void viewFromBottom(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.view_from_bottom);
        view.startAnimation(animation);
    }

    public static void viewOutBottom(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.view_out_bottom);
        view.startAnimation(animation);
    }

    public static void viewFromBottomQuickly(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.view_from_bottom_quickly);
        view.startAnimation(animation);
    }

    public static void viewOutBottomQuickly(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.view_out_bottom_quickly);
        view.startAnimation(animation);
    }


    public static void viewFromTop(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.view_from_top);
        view.startAnimation(animation);
    }

    public static void viewOutTop(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.view_out_top);
        view.startAnimation(animation);
    }

}
