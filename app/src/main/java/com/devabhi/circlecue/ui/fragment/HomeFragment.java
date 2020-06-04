package com.devabhi.circlecue.ui.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.devabhi.circlecue.Constants;
import com.devabhi.circlecue.MyPermissionClass;
import com.devabhi.circlecue.R;
import com.devabhi.circlecue.ui.adapter.SocialRecyAdapter;
import com.devabhi.circlecue.ui.gallery.DrawerModel;
import com.devabhi.circlecue.ui.model.socialmodel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {
    private ImageView image;
    TextView usernameTextView, countryTextView, clickTextView;
    private Uri image_URI;
    private MyPermissionClass permissionClass;

    private ArrayList<DrawerModel> drawerModels;
    private RecyclerView recyclerView;
    private ArrayList<socialmodel> socialmodels;
    private RecyclerView recyclerView1;
    private SocialRecyAdapter socialRecyAdapter;
    private Integer[] image1 = {R.drawable.facebook, R.drawable.insta, R.drawable.linkedin, R.drawable.twitter,
            R.drawable.ic_snapchat, R.drawable.ic_skype, R.drawable.ic_youtube, R.drawable.ic_tinder,
            R.drawable.ic_redddit, R.drawable.ic_flicker, R.drawable.ic_whatapp, R.drawable.ic_tumblr,
            R.drawable.ic_match, R.drawable.ic_meetup, R.drawable.tiktok, R.drawable.ic_mysapce,
            R.drawable.ic_shaddi, R.drawable.ic_zooks, R.drawable.ic_okcupid, R.drawable.ic_jdate,
            R.drawable.ic_hinge, R.drawable.ic_qq, R.drawable.ic_wechat, R.drawable.ic_viver,
            R.drawable.ic_quora, R.drawable.ic_plentyfish, R.drawable.browse, R.drawable.browse};

    private String[] text = {"", "", "", "", "", "", "", "", "", "", "", "", "", ""
            , "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

    private String[] clickCount = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"
            , "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};

    private String[] checkboxString = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"
            , "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};


    SharedPreferences preferences, preferencesSocial;
    String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        permissionClass = new MyPermissionClass(getActivity());

        preferences = getContext().getSharedPreferences(Constants.preference, Context.MODE_PRIVATE);
        preferencesSocial = getContext().getSharedPreferences("Social", Context.MODE_PRIVATE);

        recyclerView = view.findViewById(R.id.rvDrawer);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        image = view.findViewById(R.id.profilePicImageView);
        countryTextView = view.findViewById(R.id.countryTextView);
        clickTextView = view.findViewById(R.id.clickTextView);


        usernameTextView.setText(preferences.getString(Constants.username, ""));
        countryTextView.setText(preferences.getString(Constants.country, ""));
        clickTextView.setText("Clicks: "+ preferences.getString(Constants.click, ""));
        id = preferences.getString(Constants.id, "");
        Picasso.get().load(Constants.downloadImageUrl + preferences.getString(Constants.pic, "")).placeholder(R.drawable.profile_picture_placeholder).into(image);

        drawerModels = new ArrayList<>();
        recyclerView1 = view.findViewById(R.id.rvSocial);
        socialmodels = new ArrayList<>();

        fetchProfileData();


        for (int i =0; i< image1.length; i++) {

            if(clickCount[i] == null)
                clickCount[i] = "0";

           text[i] = preferencesSocial.getString("text"+i, text[i]);
           clickCount[i] = preferencesSocial.getString("click"+i, clickCount[i]);
           checkboxString[i] = preferencesSocial.getString("private"+i, checkboxString[i]);

            socialmodel beanClassForRecyclerView_contacts = new socialmodel(image1[i], text[i], clickCount[i], checkboxString[i]);
            socialmodels.add(beanClassForRecyclerView_contacts);
        }







        socialRecyAdapter = new SocialRecyAdapter(getActivity(), socialmodels);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(socialRecyAdapter);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            image_URI = data.getData();
            Glide.with(this).load(image_URI).into(image);
            image.setImageURI(image_URI);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionClass.resultCamera(requestCode, grantResults)) {
        } else {
            Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }


    void fetchProfileData(){

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        if(preferences.getBoolean(Constants.isFirstTimeLaunch, true))
            dialog.show();

        StringRequest profileDataRequest = new StringRequest(Request.Method.GET, Constants.userSocialList + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    JSONObject userDataObject = jsonArray.getJSONObject(0);

                    //JSONArray userDataArray = userDataObject.getJSONArray("");

                    Log.e("Social List", String.valueOf(userDataObject));

                    String country = userDataObject.getString(Constants.country);
                    String click = userDataObject.getString(Constants.click);

                    preferences.edit().putString(Constants.country, country).apply();
                    preferences.edit().putString(Constants.click, click).apply();
                    countryTextView.setText(country);
                    clickTextView.setText("Clicks: "+ click);



                    text[0] = userDataObject.getString(Constants.facebook);
                    text[1] = userDataObject.getString(Constants.instagram);
                    text[2] = userDataObject.getString(Constants.linkedIn);
                    text[3] = userDataObject.getString(Constants.twitter);
                    text[4] = userDataObject.getString(Constants.snapchat);
                    text[5] = userDataObject.getString(Constants.skype);
                    text[6] = userDataObject.getString(Constants.you_tube);
                    text[7] = userDataObject.getString(Constants.tinder);
                    text[8] = userDataObject.getString(Constants.reddit);
                    text[9] = userDataObject.getString(Constants.flickr);
                    text[10] = userDataObject.getString(Constants.whatsapp);
                    text[11] = userDataObject.getString(Constants.tumblr);
                    text[12] = userDataObject.getString(Constants.match);
                    text[13] = userDataObject.getString(Constants.meet_up);
                    text[14] = userDataObject.getString(Constants.tiktok);
                    text[15] = userDataObject.getString(Constants.my_space);
                    text[16] = userDataObject.getString(Constants.shaadi);
                    text[17] = userDataObject.getString(Constants.zoosk);
                    text[18] = userDataObject.getString(Constants.okcupid);
                    text[19] = userDataObject.getString(Constants.jdate);
                    text[20] = userDataObject.getString(Constants.hinge);
                    text[21] = userDataObject.getString(Constants.qq);
                    text[22] = userDataObject.getString(Constants.wechat);
                    text[23] = userDataObject.getString(Constants.viber);
                    text[24] = userDataObject.getString(Constants.quora);
                    text[25] = userDataObject.getString(Constants.plenty_of_fish);
                    text[26] = userDataObject.getString(Constants.personal_web_site);
                    text[27] = userDataObject.getString(Constants.business_web_site);

                    checkboxString[0] = userDataObject.getString(Constants.facebook_status);
                    checkboxString[1] = userDataObject.getString(Constants.instagram_status);
                    checkboxString[2] = userDataObject.getString(Constants.linkedIn_status);
                    checkboxString[3] = userDataObject.getString(Constants.twitter_status);
                    checkboxString[4] = userDataObject.getString(Constants.snap_chat_status);
                    checkboxString[5] = userDataObject.getString(Constants.skype_status);
                    checkboxString[6] = userDataObject.getString(Constants.you_tube_status);
                    checkboxString[7] = userDataObject.getString(Constants.tinder_status);
                    checkboxString[8] = userDataObject.getString(Constants.reddit_status);
                    checkboxString[9] = userDataObject.getString(Constants.flickr_status);
                    checkboxString[10] = userDataObject.getString(Constants.whatsapp_status);
                    checkboxString[11] = userDataObject.getString(Constants.tumblr_status);
                    checkboxString[12] = userDataObject.getString(Constants.match_status);
                    checkboxString[13] = userDataObject.getString(Constants.meet_up_status);
                    checkboxString[14] = userDataObject.getString(Constants.tiktok_status);
                    checkboxString[15] = userDataObject.getString(Constants.my_space_status);
                    checkboxString[16] = userDataObject.getString(Constants.shaadi_status);
                    checkboxString[17] = userDataObject.getString(Constants.zoosk_status);
                    checkboxString[18] = userDataObject.getString(Constants.okcupid_status);
                    checkboxString[19] = userDataObject.getString(Constants.jdate_status);
                    checkboxString[20] = userDataObject.getString(Constants.hinge_status);
                    checkboxString[21] = userDataObject.getString(Constants.qq_status);
                    checkboxString[22] = userDataObject.getString(Constants.wechat_status);
                    checkboxString[23] = userDataObject.getString(Constants.viber_status);
                    checkboxString[24] = userDataObject.getString(Constants.quora_status);
                    checkboxString[25] = userDataObject.getString(Constants.plenty_of_fish_status);
                    checkboxString[26] = userDataObject.getString(Constants.personal_web_site_status);
                    checkboxString[27] = userDataObject.getString(Constants.business_web_site_status);

                    clickCount[0] = userDataObject.getString(Constants.facebookcount);
                    clickCount[1] = userDataObject.getString(Constants.instagramcount);
                    clickCount[2] = userDataObject.getString(Constants.linkedIncount);
                    clickCount[3] = userDataObject.getString(Constants.twittercount);
                    clickCount[4] = userDataObject.getString(Constants.snap_chatcount);
                    clickCount[5] = userDataObject.getString(Constants.skypecount);
                    clickCount[6] = userDataObject.getString(Constants.you_tubecount);
                    clickCount[7] = userDataObject.getString(Constants.tindercount);
                    clickCount[8] = userDataObject.getString(Constants.redditcount);
                    clickCount[9] = userDataObject.getString(Constants.flickrcount);
                    clickCount[10] = userDataObject.getString(Constants.whatsappcount);
                    clickCount[11] = userDataObject.getString(Constants.tumblrcount);
                    clickCount[12] = userDataObject.getString(Constants.matchcount);
                    clickCount[13] = userDataObject.getString(Constants.meet_upcount);
                    clickCount[14] = userDataObject.getString(Constants.tiktokcount);
                    clickCount[15] = userDataObject.getString(Constants.my_spacecount);
                    clickCount[16] = userDataObject.getString(Constants.shaadicount);
                    clickCount[17] = userDataObject.getString(Constants.zooskcount);
                    clickCount[18] = userDataObject.getString(Constants.okcupidcount);
                    clickCount[19] = userDataObject.getString(Constants.jdatecount);
                    clickCount[20] = userDataObject.getString(Constants.hingecount);
                    clickCount[21] = userDataObject.getString(Constants.qqcount);
                    clickCount[22] = userDataObject.getString(Constants.wechatcount);
                    clickCount[23] = userDataObject.getString(Constants.vibercount);
                    clickCount[24] = userDataObject.getString(Constants.quoracount);
                    clickCount[25] = userDataObject.getString(Constants.plenty_of_fishcount);
                    clickCount[26] = userDataObject.getString(Constants.personal_web_sitecount);
                    clickCount[27] = userDataObject.getString(Constants.business_web_sitecount);


                    preferences.edit().putString(Constants.email, userDataObject.getString(Constants.email)).apply();



                    preferencesSocial.edit().clear().apply();
                    socialmodels.clear();

                    for (int i =0; i< image1.length; i++) {

                        if(clickCount[i].equals("null"))
                            clickCount[i] = "0";

                        preferencesSocial.edit().putString("text"+i, text[i]).apply();
                        preferencesSocial.edit().putString("click"+i, clickCount[i]).apply();
                        preferencesSocial.edit().putString("private"+i, checkboxString[i]).apply();

                        socialmodel beanClassForRecyclerView_contacts = new socialmodel(image1[i], text[i], clickCount[i], checkboxString[i]);
                        socialmodels.add(beanClassForRecyclerView_contacts);
                    }

                    socialRecyAdapter.notifyDataSetChanged();


                    dialog.dismiss();
                    preferences.edit().putBoolean(Constants.isFirstTimeLaunch, false).apply();



                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Profile Data Exception", String.valueOf(e));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Profile Data Error", String.valueOf(error));

            }
        });


        profileDataRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue loginQueue = Volley.newRequestQueue(getContext());

        loginQueue.add(profileDataRequest);
    }

}
