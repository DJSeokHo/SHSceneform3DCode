package com.swein.shsceneform3dcode.modellist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swein.shsceneform3dcode.R;
import com.swein.shsceneform3dcode.modellist.adapter.item.ModelListItemViewHolder;
import com.swein.shsceneform3dcode.sceneformpart.data.room.bean.RoomBean;

import java.util.ArrayList;
import java.util.List;

public class ModelListAdapter extends RecyclerView.Adapter {

    public interface ModelListAdapterDelegate {
        void loadMore();
    }

    private List<RoomBean> roomBeanList = new ArrayList<>();

    private static final int TYPE_ITEM_NORMAL_LIST = 0;

    public ModelListAdapterDelegate modelListAdapterDelegate;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_model_list_item, parent, false);
        return new ModelListItemViewHolder(view);
    }

    public void loadMoreList(List<RoomBean> roomBeanList) {
        this.roomBeanList.addAll(roomBeanList);
        notifyItemRangeChanged(this.roomBeanList.size() - roomBeanList.size() + 1, roomBeanList.size());

    }

    public void insert(RoomBean roomBean) {
        this.roomBeanList.add(0, roomBean);
        notifyItemInserted(0);
    }

    public void reloadList(List<RoomBean> roomBeanList) {

        this.roomBeanList.clear();
        this.roomBeanList.addAll(roomBeanList);

        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ModelListItemViewHolder) {
            ModelListItemViewHolder modelListItemViewHolder = (ModelListItemViewHolder) holder;
            modelListItemViewHolder.roomBean = roomBeanList.get(position);
            modelListItemViewHolder.updateView();
        }

        if(position == roomBeanList.size() - 1) {
            modelListAdapterDelegate.loadMore();
        }
    }

    @Override
    public int getItemCount() {
        return roomBeanList.size();
    }
}
