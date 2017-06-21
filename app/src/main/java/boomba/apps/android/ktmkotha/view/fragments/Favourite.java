package boomba.apps.android.ktmkotha.view.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import boomba.apps.android.ktmkotha.R;
import boomba.apps.android.ktmkotha.databinding.FragmentFavouriteBinding;
import boomba.apps.android.ktmkotha.model.FavouriteModel;
import boomba.apps.android.ktmkotha.view.adapters.FavouriteAdapter;
import boomba.apps.android.ktmkotha.viewModel.FavouriteViewModel;

public class Favourite extends Fragment {

    FragmentFavouriteBinding fragmentFavouriteBinding;
    FavouriteViewModel favouriteViewModel;
    RecyclerView recyclerView;
    FavouriteAdapter adapter;
    View rootView;

    public Favourite() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentFavouriteBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite, container, false);
        rootView = fragmentFavouriteBinding.getRoot();

        fragmentFavouriteBinding.favouriteRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FavouriteAdapter(getActivity());
        fragmentFavouriteBinding.favouriteRV.setAdapter(adapter);

        favouriteViewModel = new FavouriteViewModel();
        favouriteViewModel.setFavouriteModels();
        fragmentFavouriteBinding.setViewModel(favouriteViewModel);

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getFavourite(ArrayList<FavouriteModel> favouriteModels) {
        Toast.makeText(getActivity(), "size " + favouriteModels.size(), Toast.LENGTH_SHORT).show();
        FavouriteAdapter adapter = (FavouriteAdapter) fragmentFavouriteBinding.favouriteRV.getAdapter();
        adapter.setFavouriteModels(favouriteModels);

    }
}
