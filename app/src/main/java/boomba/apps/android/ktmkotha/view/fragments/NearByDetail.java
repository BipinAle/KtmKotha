package boomba.apps.android.ktmkotha.view.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import boomba.apps.android.ktmkotha.R;
import boomba.apps.android.ktmkotha.databinding.NearbyDetailBinding;
import boomba.apps.android.ktmkotha.model.Profile;
import boomba.apps.android.ktmkotha.viewModel.NearByDetailViewModel;

public class NearByDetail extends Fragment {

    View rootView;
    NearByDetailViewModel nearByDetailViewModel;
    CollapsingToolbarLayout collapsingToolbarLayout;
    NearbyDetailBinding nearbyDetailBinding;

    public NearByDetail() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        nearbyDetailBinding = DataBindingUtil.inflate(inflater, R.layout.nearby_detail, container, false);
        rootView = nearbyDetailBinding.getRoot();
        collapsingToolbarLayout = nearbyDetailBinding.collapsingToolbarLayout;
        collapsingToolbarLayout.setTitle("Owner");
        nearByDetailViewModel = new NearByDetailViewModel(getActivity());
        nearbyDetailBinding.setViewModel(nearByDetailViewModel);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    //receive data from event bus
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProfileSuccessEvent(Profile response) {
        nearByDetailViewModel.setProfile(response);
        nearbyDetailBinding.setViewModel(nearByDetailViewModel);

    }

}
