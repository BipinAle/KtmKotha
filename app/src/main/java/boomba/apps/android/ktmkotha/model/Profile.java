
package boomba.apps.android.ktmkotha.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile  {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("mobileNumber")
    @Expose
    public String mobileNumber;
    @SerializedName("note")
    @Expose
    public String note;
    @SerializedName("description")
    @Expose
    public String description;


}
