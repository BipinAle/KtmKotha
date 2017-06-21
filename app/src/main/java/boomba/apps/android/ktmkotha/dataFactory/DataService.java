package boomba.apps.android.ktmkotha.dataFactory;


import boomba.apps.android.ktmkotha.model.Profile;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface DataService {
    @GET
    Observable<ResponseBody> fetchMapData(@Url String url);

    @GET
    Observable<DataResponse> fetchRooms(@Url String url);

    @GET
    Observable<Profile> fetchProfile(@Url String url);




}
