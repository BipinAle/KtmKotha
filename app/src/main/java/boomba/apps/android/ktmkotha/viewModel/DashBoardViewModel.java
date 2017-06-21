package boomba.apps.android.ktmkotha.viewModel;

import android.databinding.ObservableInt;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import boomba.apps.android.ktmkotha.R;
import boomba.apps.android.ktmkotha.databinding.ActivityMainBinding;
import boomba.apps.android.ktmkotha.view.fragments.Favourite;
import boomba.apps.android.ktmkotha.view.fragments.NearBy;
import boomba.apps.android.ktmkotha.view.fragments.NearByDetail;

public class DashBoardViewModel {

    private static final long ANIM_DURATION_FAB = 1000;
    public ObservableInt fabVisibility;
    private AppCompatActivity activity;
    private ActivityMainBinding activityMainBinding;
    private Fragment newContent;


    public DashBoardViewModel(final AppCompatActivity activity, ActivityMainBinding activityMainBinding) {
        this.activity = activity;
        this.activityMainBinding = activityMainBinding;
        fabVisibility = new ObservableInt(View.INVISIBLE);
        selectFragment(1);

    }

    public void startfabAnimation() {
        fabVisibility.set(View.VISIBLE);
        activityMainBinding.floatingMenu.setTranslationY(2 * activity.getResources().getDimensionPixelOffset(R.dimen.fab_size));
        activityMainBinding.floatingMenu.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(1000)
                .setDuration(ANIM_DURATION_FAB)
                .start();
    }

    private void selectFragment(int index) {

        switch (index) {
            case 1:
                if (!(newContent instanceof NearBy)) {
                    newContent = new NearBy();
                }
                break;

            case 2:
                if (!(newContent instanceof NearByDetail)) {
                    newContent = new NearByDetail();
                }

                break;

            case 3:
                if (!(newContent instanceof Favourite)) {
                    newContent = new Favourite();
                }

                break;
        }
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_container, newContent).commit();
    }

    public void onFab1Clicked(View view) {
        selectFragment(2);
        activityMainBinding.floatingMenu.close(true);
    }

    public void onFab2Clicked(View view) {
        selectFragment(1);
        activityMainBinding.floatingMenu.close(true);

    }

    public void onFavouriteClicked(View view) {
        selectFragment(3);
        activityMainBinding.floatingMenu.close(true);
    }


}
