package boomba.apps.android.ktmkotha.viewModel;

import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Observable;

import boomba.apps.android.ktmkotha.model.FavouriteModel;

public class FavouriteViewModel extends Observable {
    private ArrayList<FavouriteModel> favouriteModels;
    public FavouriteViewModel() {

    }
    public void setFavouriteModels() {
        favouriteModels = new ArrayList<>();
        favouriteModels.add(new FavouriteModel("kalanki", false));
        favouriteModels.add(new FavouriteModel("kalanki", false));
        favouriteModels.add(new FavouriteModel("kalanki", true));
        favouriteModels.add(new FavouriteModel("kalanki", false));
        favouriteModels.add(new FavouriteModel("kalanki", false));
        favouriteModels.add(new FavouriteModel("kalanki", false));
        favouriteModels.add(new FavouriteModel("kalanki", false));
        favouriteModels.add(new FavouriteModel("kalanki", true));
        favouriteModels.add(new FavouriteModel("kalanki", false));
        favouriteModels.add(new FavouriteModel("kalanki", false));

        EventBus.getDefault().post(favouriteModels);
    }


    public void onSendClick(View view){
        Log.e("click","send button clicked");
     }

}
