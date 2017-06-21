package boomba.apps.android.ktmkotha.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import boomba.apps.android.ktmkotha.R;
import boomba.apps.android.ktmkotha.dataFactory.DataResponse;
import boomba.apps.android.ktmkotha.databinding.ActivityMainBinding;
import boomba.apps.android.ktmkotha.model.NotificationMessage;
import boomba.apps.android.ktmkotha.prefManager.PrefManager;
import boomba.apps.android.ktmkotha.viewModel.DashBoardViewModel;

public class Dashboard extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    DashBoardViewModel dashBoardViewModel;
    String regID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        dashBoardViewModel = new DashBoardViewModel(this, activityMainBinding);
        activityMainBinding.setViewModel(dashBoardViewModel);

        regID = PrefManager.getRegId(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRefreshedToken(NotificationMessage notificationMessage) {

        if (notificationMessage.isRegistered)
            Toast.makeText(this, "from regisration", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "from message", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseSucessRoomList(DataResponse response) {
        Toast.makeText(this, "respose is arrived", Toast.LENGTH_SHORT).show();
        dashBoardViewModel.startfabAnimation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
