package boomba.apps.android.ktmkotha.view.fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import boomba.apps.android.ktmkotha.R;
import boomba.apps.android.ktmkotha.databinding.NearbyfragmentBinding;
import boomba.apps.android.ktmkotha.model.PolyLineDrawString;
import boomba.apps.android.ktmkotha.view.adapters.NearbyAdapter;
import boomba.apps.android.ktmkotha.viewModel.ItemViewModel;
import boomba.apps.android.ktmkotha.viewModel.NearByViewModel;


public class NearBy extends Fragment implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnCameraIdleListener, Observer, ItemViewModel.OnLatLong {
    private GoogleMap map;
    String[] reqPermissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE};
    final int requestCode = 100;
    private GoogleApiClient mGoogleApiClient;
    private double longitude;
    private double latitude;
    NearbyfragmentBinding nearbyfragmentBinding;
    NearByViewModel viewModel;
    NearbyAdapter adapter;
    RecyclerView recyclerView;
    Toolbar toolbar;

    public static String url;

    private LatLng srcLatlng;
    private LatLng destLatlng;
    Marker marker2;
    String drawString;
    ArrayList<LatLng> markersValue = new ArrayList<>();

    public NearBy() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        nearbyfragmentBinding = DataBindingUtil.inflate(inflater, R.layout.nearbyfragment, container, false);
        View rootView = nearbyfragmentBinding.getRoot();
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(rootView.findViewById(R.id.bottomSheetLayout));
        initMap();
        viewModel = new NearByViewModel(getActivity(), bottomSheetBehavior,nearbyfragmentBinding);
        toolbar = nearbyfragmentBinding.toolbar;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        viewModel.addObserver(this);
        nearbyfragmentBinding.setViewModel(viewModel);

        recyclerView = nearbyfragmentBinding.recentRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new NearbyAdapter(getActivity(), bottomSheetBehavior, this);
        recyclerView.setAdapter(adapter);
        return rootView;
    }


    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void setMarker(LatLng latLng) {
        marker2 = map.addMarker(new MarkerOptions()
                .position(destLatlng)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.map_primary)));
        markersValue.add(latLng);
        markersValue.add(destLatlng);


    }

    public void findMidLatLng() {
        double lat = (srcLatlng.latitude + destLatlng.latitude) / (double) 2;
        double lng = (srcLatlng.longitude + destLatlng.longitude) / (double) 2;
        animateCamera(new LatLng(lat, lng));
    }

    public void animateCamera(LatLng latLng) {

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - pxFromDp(getActivity(), 90) - getStatusBarHeight() * 5;

        LatLngBounds.Builder b = new LatLngBounds.Builder();
        for (LatLng mv : markersValue) {
            b.include(mv);
        }
        LatLngBounds bounds = b.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, pxFromDp(getActivity(), 80));
        map.moveCamera(cu);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int pxFromDp(final Context context, final float dp) {
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }

    private int pxToDp() {
        Resources r = getResources();
        float margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, r.getDisplayMetrics());
        return (Math.round(margin));

    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(reqPermissions, requestCode);
        } else {
            getCurrentLocation();
            map.setMyLocationEnabled(true);
        }
        map.setOnCameraIdleListener(this);
    }

    public String makeURL(double sourcelat, double sourcelog, double destlat, double destlog) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=" + getResources().getString(R.string.goggle_key));
        return urlString.toString();
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            mGoogleApiClient.connect();
            getCurrentLocation();

        } else {
            Toast.makeText(getActivity(), "Location is disabled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation();
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            moveMap();
            mGoogleApiClient.disconnect();
        }
    }


    private void moveMap() {
        LatLng latLng = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.moveCamera(CameraUpdateFactory.zoomTo(18));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), "Error in Loading Map", Toast.LENGTH_SHORT).show();
    }

    public void drawPolyLine(List<LatLng> list) {
        PolylineOptions options = new PolylineOptions()
                .addAll(list).color(getResources().getColor(R.color.colorPrimary)).width(12).geodesic(true);

        PolylineOptions options1 = new PolylineOptions()
                .addAll(list).color(getResources().getColor(R.color.cardview_shadow_end_color)).width(14).geodesic(true);

        map.addPolyline(options1);
        map.addPolyline(options);
        markersValue.add(list.get(list.size() - 1));
    }

    LatLng latLng;

    @Override
    public void onCameraIdle() {
        latLng = map.getCameraPosition().target;
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                String locality = addressList.get(0).getAddressLine(0);
                if (!locality.isEmpty()) {

                    viewModel.setName(locality);
                    nearbyfragmentBinding.infoWindow.setVisibility(View.VISIBLE);
                    nearbyfragmentBinding.imageView.setVisibility(View.VISIBLE);
                    nearbyfragmentBinding.infoWindow.setText(viewModel.getName());
                    nearbyfragmentBinding.bottomSheetLayout.setVisibility(View.VISIBLE);
                    nearbyfragmentBinding.progressbar.setVisibility(View.GONE);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof NearByViewModel) {
            NearByViewModel nearByViewModel = (NearByViewModel) observable;
            nearbyfragmentBinding.infoWindow.setVisibility(View.VISIBLE);
            nearbyfragmentBinding.infoWindow.setText(nearByViewModel.getName());

            NearbyAdapter adapter = (NearbyAdapter) nearbyfragmentBinding.recentRecyclerView.getAdapter();
            adapter.setRoomDataViewModel(nearByViewModel.getRoomDataViewModels());
        }


    }

    public void drawPath(String encodedString) {
        List<LatLng> list = decodePoly(encodedString);
        drawPolyLine(list);
    }

    public List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    //receive data from event bus
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getPolyLineMapDrawEvent(PolyLineDrawString polyLineDrawString) {
        drawString = polyLineDrawString.convertdString;
        if (drawString != null)
            drawPath(drawString);
    }

    @Override
    public void onLatLngSuccess(double lat, double lng) {
        map.clear();
        nearbyfragmentBinding.bottomSheetLayout.setVisibility(View.VISIBLE);
        nearbyfragmentBinding.imageView.setVisibility(View.GONE);
        nearbyfragmentBinding.infoWindow.setVisibility(View.GONE);
        nearbyfragmentBinding.progressbar.setVisibility(View.GONE);
        map.setOnCameraIdleListener(null);
        destLatlng = new LatLng(lat, lng);
        srcLatlng = new LatLng(latLng.latitude, latLng.longitude);
        url = makeURL(srcLatlng.latitude, srcLatlng.longitude, destLatlng.latitude, destLatlng.longitude);
        viewModel.setUrl(url);
        setMarker(srcLatlng);
        findMidLatLng();

    }
}
