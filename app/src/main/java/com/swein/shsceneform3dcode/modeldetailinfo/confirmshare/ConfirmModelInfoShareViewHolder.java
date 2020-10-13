package com.swein.shsceneform3dcode.modeldetailinfo.confirmshare;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swein.shsceneform3dcode.R;
import com.swein.shsceneform3dcode.framework.util.device.DeviceUtil;
import com.swein.shsceneform3dcode.framework.util.glide.SHGlide;
import com.swein.shsceneform3dcode.framework.util.view.ViewUtil;
import com.swein.shsceneform3dcode.modeldetailinfo.item.ModelDetailInfoItemViewHolder;
import com.swein.shsceneform3dcode.sceneformpart.constants.SFConstants;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.RoomBean;
import com.swein.shsceneform3dcode.sceneformpart.tool.MathTool;

import java.util.ArrayList;
import java.util.List;

public class ConfirmModelInfoShareViewHolder {

    public interface ConfirmModelInfoPopupViewHolderDelegate {
        void onClose();
    }

    private View view;

    private LinearLayout linearLayoutPrint;
    private LinearLayout linearLayoutInfo;

    private TextView textViewTitle;
    private ImageView imageView;

    private ImageView imageViewClose;

    private ImageView imageViewJPG;
    private ImageView imageViewPDF;

    public RoomBean roomBean;

    private List<ModelDetailInfoItemViewHolder> modelDetailInfoItemViewHolderList = new ArrayList<>();

    public ConfirmModelInfoPopupViewHolderDelegate confirmModelInfoPopupViewHolderDelegate;

    public ConfirmModelInfoShareViewHolder(Context context) {
        view = ViewUtil.inflateView(context, R.layout.view_holder_confirm_model_info_share, null);
        findView();
        setListener();
    }

    private void findView() {
        linearLayoutInfo = view.findViewById(R.id.linearLayoutInfo);
        linearLayoutPrint = view.findViewById(R.id.linearLayoutPrint);

        textViewTitle = view.findViewById(R.id.textViewTitle);
        imageView = view.findViewById(R.id.imageView);
        imageViewClose = view.findViewById(R.id.imageViewClose);

        imageViewJPG = view.findViewById(R.id.imageViewJPG);
        imageViewPDF = view.findViewById(R.id.imageViewPDF);
    }

    private void setListener() {
        imageViewClose.setOnClickListener(view -> {
            confirmModelInfoPopupViewHolderDelegate.onClose();
        });

        imageViewJPG.setOnClickListener(view -> {
            String imagePath = DeviceUtil.screenCapture(view.getContext(), linearLayoutPrint);

            DeviceUtil.shareImage(view.getContext(), imagePath,
                    view.getContext().getString(R.string.scene_form_two_d),
                    view.getContext().getString(R.string.scene_form_two_d),
                    view.getContext().getString(R.string.scene_form_pop_menu_share));
        });

        imageViewPDF.setOnClickListener(view -> {

            List<View> viewList = new ArrayList<>();
            viewList.add(linearLayoutPrint);
            String pdfPath = DeviceUtil.createPdf(viewList, linearLayoutPrint, roomBean.name);

            DeviceUtil.shareFile(view.getContext(), pdfPath,
                    view.getContext().getString(R.string.scene_form_two_d),
                    view.getContext().getString(R.string.scene_form_two_d),
                    view.getContext().getString(R.string.scene_form_pop_menu_share));
        });
    }

    public void updateView() {

        textViewTitle.setText(roomBean.name);

        modelDetailInfoItemViewHolderList.clear();
        linearLayoutInfo.removeAllViews();

        ModelDetailInfoItemViewHolder modelDetailInfoItemViewHolder;

        modelDetailInfoItemViewHolder = new ModelDetailInfoItemViewHolder(view.getContext());

        SpannableStringBuilder areaString = new SpannableStringBuilder(String.format("%.2f", MathTool.getAreaByUnit(SFConstants.sfUnit, roomBean.area)));
        areaString.append(MathTool.getAreaUnitString(SFConstants.sfUnit)) ;

        modelDetailInfoItemViewHolder.updateView(view.getContext().getString(R.string.scene_form_area_title), areaString);
        modelDetailInfoItemViewHolderList.add(modelDetailInfoItemViewHolder);
        linearLayoutInfo.addView(modelDetailInfoItemViewHolder.getView());


        modelDetailInfoItemViewHolder = new ModelDetailInfoItemViewHolder(view.getContext());
        modelDetailInfoItemViewHolder.updateView(
                view.getContext().getString(R.string.scene_form_area_circumference_title),
                String.format("%.2f", MathTool.getLengthByUnit(SFConstants.sfUnit, roomBean.circumference)) + MathTool.getLengthUnitString(SFConstants.sfUnit));
        modelDetailInfoItemViewHolderList.add(modelDetailInfoItemViewHolder);
        linearLayoutInfo.addView(modelDetailInfoItemViewHolder.getView());

        modelDetailInfoItemViewHolder = new ModelDetailInfoItemViewHolder(view.getContext());
        modelDetailInfoItemViewHolder.updateView(
                view.getContext().getString(R.string.scene_form_area_height_title),
                String.format("%.2f", MathTool.getLengthByUnit(SFConstants.sfUnit, roomBean.height)) + MathTool.getLengthUnitString(SFConstants.sfUnit)
        );
        modelDetailInfoItemViewHolderList.add(modelDetailInfoItemViewHolder);
        linearLayoutInfo.addView(modelDetailInfoItemViewHolder.getView());

        modelDetailInfoItemViewHolder = new ModelDetailInfoItemViewHolder(view.getContext());

        SpannableStringBuilder wallAreaString = new SpannableStringBuilder(String.format("%.2f", MathTool.getAreaByUnit(SFConstants.sfUnit, roomBean.wallArea)));
        wallAreaString.append(MathTool.getAreaUnitString(SFConstants.sfUnit));
        modelDetailInfoItemViewHolder.updateView(view.getContext().getString(R.string.scene_form_wall_area_title), wallAreaString);
        modelDetailInfoItemViewHolderList.add(modelDetailInfoItemViewHolder);
        linearLayoutInfo.addView(modelDetailInfoItemViewHolder.getView());

        modelDetailInfoItemViewHolder = new ModelDetailInfoItemViewHolder(view.getContext());
        SpannableStringBuilder volumeString = new SpannableStringBuilder(String.format("%.2f", MathTool.getVolumeByUnit(SFConstants.sfUnit, roomBean.volume)));
        volumeString.append(MathTool.getVolumeUnitString(SFConstants.sfUnit));
        modelDetailInfoItemViewHolder.updateView(view.getContext().getString(R.string.scene_form_volume_title), volumeString);
        modelDetailInfoItemViewHolderList.add(modelDetailInfoItemViewHolder);
        linearLayoutInfo.addView(modelDetailInfoItemViewHolder.getView());


        if(!roomBean.thumbnailImage.equals("")) {
            imageView.post(() -> {

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageView.getWidth(), imageView.getWidth());
                imageView.setLayoutParams(layoutParams);

                SHGlide.instance.setImageBitmap(view.getContext(), roomBean.thumbnailImage, imageView, null, 0, 0, 0, 0);
            });
        }
    }


    public View getView() {
        return view;
    }

    public void destroy() {
        linearLayoutInfo.removeAllViews();
        modelDetailInfoItemViewHolderList.clear();
    }
}
