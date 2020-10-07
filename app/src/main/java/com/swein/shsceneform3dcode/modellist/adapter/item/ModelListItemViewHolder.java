package com.swein.shsceneform3dcode.modellist.adapter.item;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swein.shsceneform3dcode.R;
import com.swein.shsceneform3dcode.framework.util.activity.ActivityUtil;
import com.swein.shsceneform3dcode.framework.util.glide.SHGlide;
import com.swein.shsceneform3dcode.modeldetailinfo.ModelDetailInfoActivity;
import com.swein.shsceneform3dcode.sceneformpart.SceneFormViewHolder;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.RoomBean;

import org.json.JSONException;

import java.io.File;
import java.lang.ref.WeakReference;

public class ModelListItemViewHolder extends RecyclerView.ViewHolder {

    private WeakReference<View> view;

    public RoomBean roomBean;

    private TextView textViewName;
    private ImageView imageViewEditName;
    private ImageView imageView;

    private SceneFormViewHolder sceneFormViewHolder;

    public ModelListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        view = new WeakReference<>(itemView);
        findView();
        setListener();
    }

    private void findView() {
        imageView = view.get().findViewById(R.id.imageView);
        textViewName = view.get().findViewById(R.id.textViewName);
        imageViewEditName = view.get().findViewById(R.id.imageViewEditName);
    }

    private void setListener() {
        imageViewEditName.setOnClickListener(view -> {

        });

        view.get().setOnClickListener(view -> {
            try {
                String string = roomBean.toJSONObject().toString();
                Bundle bundle = new Bundle();
                bundle.putString("roomBean", string);
                ActivityUtil.startNewActivityWithoutFinish(view.getContext(), ModelDetailInfoActivity.class, bundle);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        });
    }

    public void updateView() {

        textViewName.setText(roomBean.name);

        if(!roomBean.thumbnailImage.equals("")) {
            imageView.post(() -> SHGlide.instance.setImageBitmap(view.get().getContext(), Uri.fromFile(new File(roomBean.thumbnailImage)), imageView, null, imageView.getWidth(), imageView.getHeight(), 0f, 0f));
        }

    }

    @Override
    protected void finalize() throws Throwable {
        if(sceneFormViewHolder != null) {
            sceneFormViewHolder.destroy();
            sceneFormViewHolder = null;
        }
        super.finalize();
    }
}
