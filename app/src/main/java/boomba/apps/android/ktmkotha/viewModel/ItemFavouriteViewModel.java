package boomba.apps.android.ktmkotha.viewModel;

import boomba.apps.android.ktmkotha.model.FavouriteModel;

public class ItemFavouriteViewModel {
    private FavouriteModel favouriteModel;

    public ItemFavouriteViewModel(FavouriteModel favouriteModel) {
        this.favouriteModel = favouriteModel;
    }

    public String getName() {
        return favouriteModel.name;
    }

    public Boolean getState() {
        return favouriteModel.state;
    }
}
