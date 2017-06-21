package boomba.apps.android.ktmkotha;

import android.app.Application;
import android.content.Context;

import boomba.apps.android.ktmkotha.dataFactory.DataFactory;
import boomba.apps.android.ktmkotha.dataFactory.DataService;

public class MyApplication extends Application {

    private DataService dataService;

    public static MyApplication getContext(Context context) {
        return MyApplication.get(context);
    }

    private static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    public DataService getDataService() {
        if (dataService == null) {
            dataService = DataFactory.create();
        }
        return dataService;
    }



 }
