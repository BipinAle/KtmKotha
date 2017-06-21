package boomba.apps.android.ktmkotha.viewModel;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import boomba.apps.android.ktmkotha.MyApplication;
import boomba.apps.android.ktmkotha.dataFactory.DataFactory;
import boomba.apps.android.ktmkotha.dataFactory.DataService;
import boomba.apps.android.ktmkotha.model.Profile;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NearByDetailViewModel {
    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Profile profile;

    public NearByDetailViewModel(Context context) {
        this.context = context;
        profile = new Profile();
        callApiForNearByDetail();

    }

    private void callApiForNearByDetail() {
        MyApplication myApplication = MyApplication.getContext(context);
        DataService dataService = myApplication.getDataService();
        Disposable disposable = dataService.fetchProfile(DataFactory.PROFILE_ENDPOINT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Profile>() {
                    @Override
                    public void accept(Profile response) throws Exception {
                        EventBus.getDefault().post(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("error");
                    }
                });

        compositeDisposable.add(disposable);
    }


    public String getName() {
        return profile.name;
    }

    public String getGender() {
        return profile.gender;
    }

    public String getPhone() {
        return profile.mobileNumber;
    }

    public String getEmail() {
        return profile.email;
    }

    public String getNote() {
        return profile.note;
    }

    public String getDescroption() {
        return profile.description;
    }

    public void setProfile(Profile response) {
        this.profile = response;

    }
}
