package boomba.apps.android.ktmkotha.view.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import boomba.apps.android.ktmkotha.R;
import boomba.apps.android.ktmkotha.databinding.SingleFavouriteBinding;
import boomba.apps.android.ktmkotha.model.FavouriteModel;
import boomba.apps.android.ktmkotha.viewModel.ItemFavouriteViewModel;


public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<FavouriteModel> favouriteModels;

    public FavouriteAdapter(Context context) {
        this.context = context;
        favouriteModels = new ArrayList<>();
    }

    public void setFavouriteModels(ArrayList<FavouriteModel> favouriteModels) {
        this.favouriteModels = favouriteModels;
        notifyDataSetChanged();

    }

    @Override
    public FavouriteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SingleFavouriteBinding singleFavouriteBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.single_favourite, parent, false);
        return new MyViewHolder(singleFavouriteBinding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FavouriteModel item = favouriteModels.get(position);
        holder.bind(item);

    }

    @Override
    public int getItemCount() {
        return favouriteModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        SingleFavouriteBinding singleFavouriteBinding;

        MyViewHolder(SingleFavouriteBinding singleFavouriteBinding) {
            super(singleFavouriteBinding.favouriteMain);
            this.singleFavouriteBinding = singleFavouriteBinding;
        }

        void bind(FavouriteModel item) {
            singleFavouriteBinding.setViewModel(new ItemFavouriteViewModel(item));
        }
    }
}
