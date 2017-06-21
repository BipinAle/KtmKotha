
package boomba.apps.android.ktmkotha.dataFactory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import boomba.apps.android.ktmkotha.model.RoomData;

import java.util.List;

public class DataResponse {

    @SerializedName("data")
    @Expose
    public List<RoomData> data = null;

    public List<RoomData> getData() {
        return data;
    }

    public void setData(List<RoomData> data) {
        this.data = data;
    }
}
