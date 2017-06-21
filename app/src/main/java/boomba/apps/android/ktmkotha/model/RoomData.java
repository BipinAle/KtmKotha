
package boomba.apps.android.ktmkotha.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoomData {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("latitude")
    @Expose
    public String latitude;
    @SerializedName("longitude")
    @Expose
    public String longitude;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("contactNo")
    @Expose
    public String contactNo;
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("pubDate")
    @Expose
    public String pubDate;

}
