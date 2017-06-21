package boomba.apps.android.ktmkotha.dataFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataFactory {

    private final static String BASE_URL = "http://behemppy.com/boommba/apps/ktmroom/";
    //endpoints
    public final static String ROOM_ENDPOINT = "rooms.json";
    public final static String PROFILE_ENDPOINT="profile.json";

        private final static String BASE_URL1 = "http://192.168.89.66/gcm_chat/v1";
    //endpoints
    public final static String PUT_REGID = "/user/_ID_";



    public static DataService create() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(DataService.class);
    }
}
