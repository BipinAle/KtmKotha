package boomba.apps.android.ktmkotha.viewModel;

import android.content.Context;
import android.databinding.ObservableInt;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import boomba.apps.android.ktmkotha.MyApplication;
import boomba.apps.android.ktmkotha.R;
import boomba.apps.android.ktmkotha.dataFactory.DataFactory;
import boomba.apps.android.ktmkotha.dataFactory.DataResponse;
import boomba.apps.android.ktmkotha.dataFactory.DataService;
import boomba.apps.android.ktmkotha.databinding.NearbyfragmentBinding;
import boomba.apps.android.ktmkotha.model.PolyLineDrawString;
import boomba.apps.android.ktmkotha.model.RoomData;
import boomba.apps.android.ktmkotha.view.widgets.FeedItemAnimator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class NearByViewModel extends Observable {

    public ObservableInt upArrowRotation, placeNameVisibility, backgroundVisibility, upArrowVisibility, progressbarVisibility, pinMarkerVisibility, nearbyListVisibility, bottomsheetlayoutVisibility;
    private List<RoomData> roomDatas;
    private String name;
    private Context context;
    private Boolean isExpanded = false;
    private BottomSheetBehavior bottomSheetBehavior;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private NearbyfragmentBinding nearbyfragmentBinding;

    public NearByViewModel(Context context, BottomSheetBehavior bottomSheetBehavior, NearbyfragmentBinding nearbyfragmentBinding) {
        this.context = context;
        this.nearbyfragmentBinding = nearbyfragmentBinding;
        roomDatas = new ArrayList<>();
        upArrowVisibility = new ObservableInt(View.GONE);
        progressbarVisibility = new ObservableInt(View.GONE);
        backgroundVisibility = new ObservableInt(View.GONE);
        pinMarkerVisibility = new ObservableInt(View.GONE);
        placeNameVisibility = new ObservableInt(View.GONE);
        nearbyListVisibility = new ObservableInt(View.GONE);
        bottomsheetlayoutVisibility = new ObservableInt(View.GONE);
        upArrowRotation = new ObservableInt(0);
        this.bottomSheetBehavior = bottomSheetBehavior;
        init();
        bottomSheetEvents();
        callApiForRooms();

    }

    private void init() {

        upArrowVisibility.set(View.VISIBLE);
        animateButton();
        progressbarVisibility.set(View.VISIBLE);
        backgroundVisibility.set(View.GONE);
        pinMarkerVisibility.set(View.GONE);
        placeNameVisibility.set(View.GONE);
        nearbyListVisibility.set(View.VISIBLE);
        bottomsheetlayoutVisibility.set(View.GONE);
    }

    private void animateButton() {
        // Load the animation
        final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
        nearbyfragmentBinding.upArrow.startAnimation(myAnim);

    }

    private void callApiForMapRoute(String url) {
        MyApplication myApplication = MyApplication.getContext(context);
        DataService dataService = myApplication.getDataService();
        Disposable disposable = dataService.fetchMapData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody response) throws Exception {
                        EventBus.getDefault().post(new PolyLineDrawString(customParser(response)));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });

        compositeDisposable.add(disposable);
    }

    private void callApiForRooms() {
        MyApplication myApplication = MyApplication.getContext(context);
        DataService dataService = myApplication.getDataService();
        Disposable disposable = dataService.fetchRooms(DataFactory.ROOM_ENDPOINT)

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataResponse>() {
                    @Override
                    public void accept(DataResponse response) throws Exception {
                        setRoomDataViewModels(response.getData());
                        EventBus.getDefault().post(response);//for fab visibility
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

        compositeDisposable.add(disposable);
    }


    private String customParser(ResponseBody response) {
        try {
            String jsonString = response.string();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray routeArray = jsonObject.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            return overviewPolylines.getString("points");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public List<RoomData> getRoomDataViewModels() {
        return roomDatas;
    }

    private void setRoomDataViewModels(List<RoomData> roomDataViewModels) {
        this.roomDatas = roomDataViewModels;
        setChanged();
        notifyObservers();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setChanged();
        notifyObservers();
    }


    public void setUrl(String url) {
        callApiForMapRoute(url);
    }

    public void onUpArrowClicked(View view) {
        if (!isExpanded) {
            upArrowRotation.set(0);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    nearbyfragmentBinding.recentRecyclerView.setItemAnimator(new FeedItemAnimator());
                }
            }, 500);


        } else {
            upArrowRotation.set(180);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        }
    }

    private void bottomSheetEvents() {
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        isExpanded = false;
                        upArrowRotation.set(0);
                        backgroundVisibility.set(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        isExpanded = true;
                        upArrowRotation.set(180);
                        backgroundVisibility.set(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

}
