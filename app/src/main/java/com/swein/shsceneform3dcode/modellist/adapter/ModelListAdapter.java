package com.swein.shsceneform3dcode.modellist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swein.shsceneform3dcode.R;
import com.swein.shsceneform3dcode.bean.ModelWrapperItemBean;
import com.swein.shsceneform3dcode.modellist.adapter.item.ModelListItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ModelListAdapter extends RecyclerView.Adapter {

    public interface ModelListAdapterDelegate {
        void loadMore();
    }

    private List<ModelWrapperItemBean> modelWrapperItemBeanList = new ArrayList<>();

    private static final int TYPE_ITEM_NORMAL_LIST = 0;

    public ModelListAdapterDelegate modelListAdapterDelegate;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_model_list_item, parent, false);
        return new ModelListItemViewHolder(view);
    }

    public void loadMoreList(List<ModelWrapperItemBean> modelWrapperItemBeanList) {
        this.modelWrapperItemBeanList.addAll(modelWrapperItemBeanList);
        notifyItemRangeChanged(this.modelWrapperItemBeanList.size() - modelWrapperItemBeanList.size() + 1, modelWrapperItemBeanList.size());

    }

    public void update(ModelWrapperItemBean modelWrapperItemBean) {
        for(int i = modelWrapperItemBeanList.size() - 1; i >= 0; i--) {
            if(modelWrapperItemBean.id == modelWrapperItemBeanList.get(i).id) {
                modelWrapperItemBeanList.get(i).name = modelWrapperItemBean.name;
                modelWrapperItemBeanList.get(i).roomBean.name = modelWrapperItemBean.name;
                notifyItemChanged(i);
                return;
            }
        }
    }

    public void delete(ModelWrapperItemBean modelWrapperItemBean) {
        for(int i = modelWrapperItemBeanList.size() - 1; i >= 0; i--) {
            if(modelWrapperItemBean.id == modelWrapperItemBeanList.get(i).id) {
                modelWrapperItemBeanList.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    public void reloadList(List<ModelWrapperItemBean> modelWrapperItemBeanList) {

        this.modelWrapperItemBeanList.clear();
        this.modelWrapperItemBeanList.addAll(modelWrapperItemBeanList);

        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ModelListItemViewHolder) {
            ModelListItemViewHolder modelListItemViewHolder = (ModelListItemViewHolder) holder;
            modelListItemViewHolder.modelWrapperItemBean = modelWrapperItemBeanList.get(position);
            modelListItemViewHolder.updateView();
        }

        if(position == modelWrapperItemBeanList.size() - 1) {
            modelListAdapterDelegate.loadMore();
        }
    }

    @Override
    public int getItemCount() {
        return modelWrapperItemBeanList.size();
    }
}
