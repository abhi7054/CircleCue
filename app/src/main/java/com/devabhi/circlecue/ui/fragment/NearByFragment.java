package com.devabhi.circlecue.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devabhi.circlecue.Constants;
import com.devabhi.circlecue.MainActivity;
import com.devabhi.circlecue.R;
import com.devabhi.circlecue.ui.adapter.MyCircleAdapter;
import com.devabhi.circlecue.ui.model.CircleItemDataModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NearByFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{

    GoogleMap googleMap;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);


        preferences = getContext().getSharedPreferences(Constants.preference, Context.MODE_PRIVATE);

        SupportMapFragment map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.googleMap);
        map.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap1) {
        googleMap = googleMap1;
        /*googleMap.setMapStyle(GoogleMap.MAP_TYPE_NORMAL);*/
        googleMap.clear();

        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(22.3140, 73.1388))
                .zoom(3f)
                .bearing(0f)
                .tilt(45f)
                .build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null);

        fetchNearByUsers();

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {


                preferences.edit().putBoolean(Constants.isMyProfile, false).apply();
                preferences.edit().putString(Constants.othersID, String.valueOf(marker.getTag())).apply();

                MainActivity mainActivity = (MainActivity) getContext();
                FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null).replace(R.id.fragmentLoad, new ViewProfileFragment()).commit();



            }
        });


    }


    void fetchNearByUsers(){

        StringRequest getUserList = new StringRequest("http://circlecue.com/api/data.php?id="+preferences.getString(Constants.id, ""), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray profilesArray = jsonObject.getJSONArray("map");

                    Log.e("Profiles Response", String.valueOf(profilesArray));




                    for(int i = 0; i < profilesArray.length(); i++){

                        JSONObject profile = profilesArray.getJSONObject(i);
                        String id = profile.getString("id");
                        String name = profile.getString("name");
                        Double lat = profile.getDouble("lat");
                        Double lng = profile.getDouble("lng");
                        String center = profile.getString("center");
                        String di = profile.getString("dis");

                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(new LatLng(lat, lng))
                                .title(name +"("+di+" Miles away from you)")
                                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_marker)));



                        Marker marker = googleMap.addMarker(markerOptions);

                        marker.setTag(id);

                    }



                } catch (JSONException e) {

                    Log.e("Search profiles except", String.valueOf(e));
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        getUserList.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue loginQueue = Volley.newRequestQueue(getContext());

        loginQueue.add(getUserList);


    }



    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();

    }
}
