package com.swein.shsceneform3dcode.framework.util.device;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

public class DeviceUtil {

    public static void callWithDialog(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void screenCapture(Activity activity) {
        Date now = new Date();
        try {
            // image naming and path  to include sd card  appending name you choose for file
            /*
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"Trieber");
            dir.mkdirs();

            String mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
                    + "/Trieber/" + android.text.format.DateFormat.format("yyyyMMddHHmmss", now).toString() + ".jpg";
            */
            String mPath = null;
            File dir_screenshots = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"Screenshots");
            File dir_camera = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"Camera");

            if(dir_screenshots.exists()) {
                mPath = dir_screenshots.toString() + "/USHome_" + android.text.format.DateFormat.format("yyyyMMddHHmmss", now).toString() + ".jpg";
            } else if(dir_camera.exists()) {
                mPath = dir_camera.toString() + "/USHome_" + android.text.format.DateFormat.format("yyyyMMddHHmmss", now).toString() + ".jpg";
            } else {
                mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
                        + "/Trieber_" + android.text.format.DateFormat.format("yyyyMMddHHmmss", now).toString() + ".jpg";
            }

            // create bitmap screen capture
            View v1 = activity.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            activity.sendBroadcast(new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)) );
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }

    }

    public static String screenCapture(Context context, View view) {
        Date now = new Date();
        try {
            // image naming and path  to include sd card  appending name you choose for file
            /*
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"Trieber");
            dir.mkdirs();

            String mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
                    + "/Trieber/" + android.text.format.DateFormat.format("yyyyMMddHHmmss", now).toString() + ".jpg";
            */
            String mPath = null;
            File dir_screenshots = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"Screenshots");
            File dir_camera = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"Camera");

            if(dir_screenshots.exists()) {
                mPath = dir_screenshots.toString() + "/USHome_" + android.text.format.DateFormat.format("yyyyMMddHHmmss", now).toString() + ".jpg";
            } else if(dir_camera.exists()) {
                mPath = dir_camera.toString() + "/USHome_" + android.text.format.DateFormat.format("yyyyMMddHHmmss", now).toString() + ".jpg";
            } else {
                mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
                        + "/USHome_" + android.text.format.DateFormat.format("yyyyMMddHHmmss", now).toString() + ".jpg";
            }

            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 80;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            context.sendBroadcast(new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)) );

            return mPath;
        }
        catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
            return "";
        }
    }

    public static String createPdf(List<View> viewList, View defaultSizeView, String fileName) {

        try {
            PdfDocument document = new PdfDocument();

            // init pdf as A4 size paper
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(defaultSizeView.getWidth(), defaultSizeView.getWidth() * 297/210, 1).create();

            PdfDocument.Page page;
            for(View view : viewList ) {
                page = document.startPage(pageInfo);
                view.draw(page.getCanvas());
                document.finishPage(page);
            }

            String mPath = null;
            File dir_screenshots = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"Screenshots");
            File dir_camera = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"Camera");

            if(dir_screenshots.exists()) {
                mPath = dir_screenshots.toString() + "/USHome_" + fileName + ".pdf";
            } else if(dir_camera.exists()) {
                mPath = dir_camera.toString() + "/USHome_" + fileName + ".pdf";
            } else {
                mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
                        + "/USHome_" + fileName + ".pdf";
            }

            File file = new File(mPath);
            FileOutputStream outputStream = new FileOutputStream(file);
            document.writeTo(outputStream);

            outputStream.close();
            document.close();

            return mPath;
        }
        catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    /**
     * add this to Application.onCreate
     *
     *        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
     *
     *        StrictMode.setVmPolicy(builder.build());
     *
     *        builder.detectFileUriExposure();
     */
    public static void shareImage(Context context, String imagePath, String text, String subject, String chooserText) {

        Uri imageUri = Uri.fromFile(new File(imagePath));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, chooserText));

    }

    public static void shareFile(Context context, String imagePath, String text, String subject, String chooserText) {

        Uri imageUri = Uri.fromFile(new File(imagePath));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("application/pdf");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, chooserText));

    }

    public static void shareText(Context context, String text, String subject, String chooserText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, chooserText));
    }
}
