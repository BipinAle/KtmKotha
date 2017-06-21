package boomba.apps.android.ktmkotha.viewModel;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

import java.util.Observable;

import boomba.apps.android.ktmkotha.model.RoomData;
import boomba.apps.android.ktmkotha.view.fragments.NearBy;

public class ItemViewModel extends Observable {

    //listing the room data
    private RoomData roomData;
    private Context context;
    private BottomSheetBehavior bottomSheetBehavior;


    public ItemViewModel(RoomData roomData, Activity context, BottomSheetBehavior bottomSheetBehavior, NearBy nearBy) {
        this.roomData = roomData;
        this.context = context;
        this.bottomSheetBehavior = bottomSheetBehavior;
        onLatLong = nearBy;

    }

    public ItemViewModel() {

    }


    public String getRoomAddress() {
        return roomData.address;
    }


    public String getType() {
        return roomData.type;
    }


    public String getPhNumber() {
        return roomData.contactNo;
    }


    public String getPrice() {
        return roomData.price;
    }


    public String getPubDate() {
        return roomData.pubDate;
    }

    public String getLatitude() {
        return roomData.latitude;
    }


    public String getLongitude() {
        return roomData.longitude;
    }

    public void onItemClick(View view) {

        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


        onLatLong.onLatLngSuccess(Double.parseDouble(getLatitude()), Double.parseDouble(getLongitude()));

    }
    //end

    public void onCallClick(View view) {

        onPhoneDialled.onSuccess(getPhNumber());

    }

    //phone interface
    public OnPhoneDialled onPhoneDialled;

    public void setOnPhoneDialled(OnPhoneDialled onPhoneDialled) {
        this.onPhoneDialled = onPhoneDialled;

    }

    public interface OnPhoneDialled {
        void onSuccess(String phoneNumber);

    }

    //lat long interface
    public interface OnLatLong {
        void onLatLngSuccess(double lat, double lng);
    }

    private OnLatLong onLatLong;


}
