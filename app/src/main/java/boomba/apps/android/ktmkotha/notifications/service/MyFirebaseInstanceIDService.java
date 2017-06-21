package boomba.apps.android.ktmkotha.notifications.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.greenrobot.eventbus.EventBus;

import boomba.apps.android.ktmkotha.model.NotificationMessage;
import boomba.apps.android.ktmkotha.prefManager.PrefManager;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("token",refreshedToken);
        PrefManager.setRegId(this, refreshedToken);
        EventBus.getDefault().post(new NotificationMessage(refreshedToken, "", true));
    }





}

