package com.swein.shsceneform3dcode.modellist.adapter.item;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swein.shsceneform3dcode.R;
import com.swein.shsceneform3dcode.bean.ModelWrapperItemBean;
import com.swein.shsceneform3dcode.framework.util.activity.ActivityUtil;
import com.swein.shsceneform3dcode.framework.util.eventsplitshot.eventcenter.EventCenter;
import com.swein.shsceneform3dcode.framework.util.eventsplitshot.subject.ESSArrows;
import com.swein.shsceneform3dcode.framework.util.glide.SHGlide;
import com.swein.shsceneform3dcode.modeldetailinfo.ModelDetailInfoActivity;
import com.swein.shsceneform3dcode.sceneformpart.SceneFormViewHolder;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class ModelListItemViewHolder extends RecyclerView.ViewHolder {

    private WeakReference<View> view;

    public ModelWrapperItemBean modelWrapperItemBean;

    private TextView textViewName;
    private ImageView imageViewMore;
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
        imageViewMore = view.get().findViewById(R.id.imageViewMore);
    }

    private void setListener() {
        imageViewMore.setOnClickListener(view -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("modelWrapperItemBean", modelWrapperItemBean);
            hashMap.put("fromView", new WeakReference<>(imageViewMore));

            EventCenter.instance.sendEvent(ESSArrows.MODEL_ITEM_CLICK_MENU, this, hashMap);
        });

        view.get().setOnClickListener(view -> {

            try {
                Bundle bundle = new Bundle();
                bundle.putString("roomBean", modelWrapperItemBean.roomBean.toJSONObject().toString());
                bundle.putBoolean("isNew", false);
                ActivityUtil.startNewActivityWithoutFinish(view.getContext(), ModelDetailInfoActivity.class, bundle);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    public void updateView() {

        textViewName.setText(modelWrapperItemBean.name);

        if(!modelWrapperItemBean.imgUrl.equals("")) {
            imageView.post(() -> SHGlide.instance.setImageBitmap(view.get().getContext(), modelWrapperItemBean.imgUrl, imageView, null, imageView.getWidth(), imageView.getHeight(), 0f, 0f));
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
