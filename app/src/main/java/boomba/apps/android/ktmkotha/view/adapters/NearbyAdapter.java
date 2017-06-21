package boomba.apps.android.ktmkotha.view.adapters;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import boomba.apps.android.ktmkotha.R;
import boomba.apps.android.ktmkotha.databinding.SingleRoomRowBinding;
import boomba.apps.android.ktmkotha.model.RoomData;
import boomba.apps.android.ktmkotha.view.fragments.NearBy;
import boomba.apps.android.ktmkotha.viewModel.ItemViewModel;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.MyViewHolder> {
    private Activity context;
    private List<RoomData> roomDatas;
    private BottomSheetBehavior bottomSheetBehavior;
    private NearBy nearBy;

    public NearbyAdapter(Activity context, BottomSheetBehavior bottomSheetBehavior, NearBy nearBy) {
        this.context = context;
        this.nearBy = nearBy;
        roomDatas = new ArrayList<>();
        this.bottomSheetBehavior = bottomSheetBehavior;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SingleRoomRowBinding singleRoomRowBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.single_room_row,
                        parent, false);
        return new MyViewHolder(singleRoomRowBinding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RoomData item = roomDatas.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return roomDatas.size();
    }

    public void setRoomDataViewModel(List<RoomData> roomDataViewModel) {
        this.roomDatas = roomDataViewModel;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements ItemViewModel.OnPhoneDialled {
        SingleRoomRowBinding singleRoomRowBinding;
        ItemViewModel itemViewModel;

        MyViewHolder(SingleRoomRowBinding singleRoomRowBinding) {
            super(singleRoomRowBinding.mainItem);
            this.singleRoomRowBinding = singleRoomRowBinding;

        }

        void bind(RoomData item) {
            itemViewModel = new ItemViewModel(item, context, bottomSheetBehavior, nearBy);
            itemViewModel.setOnPhoneDialled(this);
            singleRoomRowBinding.setRoomDataViewModel(itemViewModel);

        }

        @Override
        public void onSuccess(String phoneNumber) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + Uri.encode(phoneNumber.trim())));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(callIntent);
        }
    }
}
