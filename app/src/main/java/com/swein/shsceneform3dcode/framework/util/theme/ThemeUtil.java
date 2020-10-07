package com.swein.shsceneform3dcode.framework.util.theme;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

public class ThemeUtil {

    /**
     * hide system top status bar and bottom navigation button bar
     */
    public static void hideSystemUi(Activity activity) {
        //隐藏虚拟按键
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * show system top status bar and bottom navigation button bar
     */
    public static void showSystemUi(Activity activity) {
        //显示虚拟键盘
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            //低版本sdk
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.VISIBLE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public static void setSystemBottomNavigationBarColor(Activity context, int colorResourceId) {
        try {
            context.getWindow().setNavigationBarColor(ContextCompat.getColor(context, colorResourceId));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * must > API 19
     * put this before setContentView()
     *
     * and add
     * android:fitsSystemWindows="true"
     * to your root layout of xml file
     *
     * @param activity activity
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void transparencyBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * must > API 19
     * put this before setContentView()
     *
     * and add
     * android:fitsSystemWindows="true"
     * to your root layout of xml file
     *
     *
     * @param activity
     * @param colorResId
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setWindowStatusBarColorResource(Activity activity, int colorResId) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getColor(colorResId));
    }

    /**
     * must > API 19
     * put this before setContentView()
     *
     * and add
     * android:fitsSystemWindows="true"
     * to your root layout of xml file
     *
     *
     * @param activity
     * @param color
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setWindowStatusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    public static void setAndroidNativeLightStatusBar(Activity activity, boolean dark, boolean fullScreen) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            if(fullScreen) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            else {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        else {

            if(fullScreen) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
            else {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

    public static void setSystemBarTheme(final Activity pActivity, final boolean pIsDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final int lFlags = pActivity.getWindow().getDecorView().getSystemUiVisibility();
            pActivity.getWindow().getDecorView().setSystemUiVisibility(pIsDark ? (lFlags & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) : (lFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
        }
    }
}
