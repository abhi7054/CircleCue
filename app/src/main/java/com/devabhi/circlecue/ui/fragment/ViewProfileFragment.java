package com.devabhi.circlecue.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devabhi.circlecue.Constants;
import com.devabhi.circlecue.MainActivity;
import com.devabhi.circlecue.R;
import com.devabhi.circlecue.ui.adapter.ViewProfileAdapter;
import com.devabhi.circlecue.ui.model.ViewProfileModel;
import com.devabhi.circlecue.ui.model.socialmodel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfileFragment extends Fragment {

    SharedPreferences preferences, socialPreferences;
    CircleImageView profilePicImageView;
    ImageView circleCueLinkImageView;
    LinearLayout otherLinearLayout;

    TextView usernameTextView, countryTextView, linkTextView, copyTextView, blockUserTextView, addToFavouriteTextView;
    RecyclerView viewProfileRecyclerView;

    ArrayList<ViewProfileModel> viewProfileModelArrayList = new ArrayList<>();
    ViewProfileAdapter viewProfileAdapter;

    Button innerCircleRequestButton, sendMessageButton;

    private String[] text = {"", "", "", "", "", "", "", "", "", "", "", "", "", ""
            , "", "", "", "", "", "", "", "", "", "", "", "", "", ""};


    Integer[] imageIds = {R.drawable.facebook, R.drawable.insta, R.drawable.linkedin, R.drawable.twitter,
            R.drawable.snapchat, R.drawable.skype, R.drawable.youtube, R.drawable.tinder,
            R.drawable.redddit, R.drawable.flicker, R.drawable.whatapp, R.drawable.tumblr,
            R.drawable.match, R.drawable.meetup, R.drawable.tiktok, R.drawable.mysapce,
            R.drawable.shaddi, R.drawable.zooks, R.drawable.okcupid, R.drawable.jdate,
            R.drawable.hinge, R.drawable.qq, R.drawable.wechat, R.drawable.viver,
            R.drawable.quora, R.drawable.plentyfish, R.drawable.browse, R.drawable.browse};



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);

        preferences = getContext().getSharedPreferences(Constants.preference, Context.MODE_PRIVATE);
        socialPreferences = getContext().getSharedPreferences("Social" , Context.MODE_PRIVATE);

        findView(view);
        setOnClickListener();



        if(preferences.getBoolean(Constants.isMyProfile, true))
            ownProfileSetup();
        else{
            otherProfileSetup();
            checkBlock();
        }



        return view;
    }

    void findView(View view){

        profilePicImageView = view.findViewById(R.id.image);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        countryTextView = view.findViewById(R.id.countryTextView);
        linkTextView = view.findViewById(R.id.linkTextView);
        viewProfileRecyclerView = view.findViewById(R.id.viewProfileRecyclerView);
        copyTextView = view.findViewById(R.id.tv_copy);
        circleCueLinkImageView = view.findViewById(R.id.circleCueLinkImageView);
        otherLinearLayout = view.findViewById(R.id.otherLinearLayout);
        blockUserTextView = view.findViewById(R.id.blockUserTextView);
        addToFavouriteTextView = view.findViewById(R.id.addToFavoriteTextView);
        innerCircleRequestButton = view.findViewById(R.id.innerCircleRequestButton);
        sendMessageButton = view.findViewById(R.id.sendMessageButton);

    }

    void setOnClickListener(){

        copyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(linkTextView.getText(), linkTextView.getText());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getContext(), "Profile link copied", Toast.LENGTH_LONG).show();

            }
        });


        circleCueLinkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(linkTextView.getText().toString()));

                startActivity(browserIntent);

            }
        });

        blockUserTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(blockUserTextView.getText().toString().equals("Block User"))
                    blockUser();
                else
                    unBlockUser();

            }
        });

        addToFavouriteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(addToFavouriteTextView.getText().toString().equals("Add to favorites"))
                    addToFavorites();
                else
                    removeFromFavorites();

            }
        });


        innerCircleRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                innerCircleRequest();

            }
        });


    }


    void innerCircleRequest(){

        StringRequest innerCircleRequest = new StringRequest(Request.Method.GET, "http://circlecue.com/api/addtocircle.php?id="+preferences.getString(Constants.id, "")+"&id2="+preferences.getString(Constants.othersID, ""), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getContext(), "Request Sent", Toast.LENGTH_SHORT).show();




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Error Blocking", String.valueOf(error));

            }
        });


        innerCircleRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue loginQueue = Volley.newRequestQueue(getContext());

        loginQueue.add(innerCircleRequest);

    }

    void ownProfileSetup(){

        otherLinearLayout.setVisibility(View.GONE);
        innerCircleRequestButton.setVisibility(View.GONE);
        sendMessageButton.setVisibility(View.GONE);
        viewProfileModelArrayList = new ArrayList<>();

        for(int i = 0; i < imageIds.length; i++){

            String url = socialPreferences.getString("text"+i, "");

            if(!url.equals(""))
                viewProfileModelArrayList.add(new ViewProfileModel(imageIds[i], url));


        }

        viewProfileAdapter = new ViewProfileAdapter(getContext(), viewProfileModelArrayList);
        viewProfileRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        viewProfileRecyclerView.setItemAnimator(new DefaultItemAnimator());

        viewProfileRecyclerView.setAdapter(viewProfileAdapter);


        Picasso.get().load(Constants.downloadImageUrl + preferences.getString(Constants.pic, "")).placeholder(R.drawable.profile_picture_placeholder).into(profilePicImageView);

        usernameTextView.setText(preferences.getString(Constants.username, ""));
        countryTextView.setText(preferences.getString(Constants.country, ""));
        linkTextView.setText("https://circlecue.com/"+preferences.getString(Constants.username, ""));
    }

    void otherProfileSetup(){




        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

           dialog.show();

        StringRequest profileDataRequest = new StringRequest(Request.Method.GET, Constants.userSocialList + preferences.getString(Constants.othersID, ""), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    JSONObject userDataObject = jsonArray.getJSONObject(0);

                    //JSONArray userDataArray = userDataObject.getJSONArray("");

                    Log.e("Social List", String.valueOf(userDataObject));

                    String country = userDataObject.getString(Constants.country);
                    String username = userDataObject.getString(Constants.username);

                    linkTextView.setText("https://circlecue.com/"+username);

                    usernameTextView.setText(userDataObject.getString(Constants.username));
                    Picasso.get().load(Constants.downloadImageUrl + userDataObject.getString(Constants.pic)).placeholder(R.drawable.profile_picture_placeholder).into(profilePicImageView);


                    countryTextView.setText(country);

                    text = new String[]{userDataObject.getString(Constants.facebook),
                            userDataObject.getString(Constants.instagram),
                            userDataObject.getString(Constants.linkedIn),
                            userDataObject.getString(Constants.twitter),
                            userDataObject.getString(Constants.snapchat),
                            userDataObject.getString(Constants.skype),
                            userDataObject.getString(Constants.you_tube),
                            userDataObject.getString(Constants.tinder),
                            userDataObject.getString(Constants.reddit),
                            userDataObject.getString(Constants.flickr),
                            userDataObject.getString(Constants.whatsapp),
                            userDataObject.getString(Constants.tumblr),
                            userDataObject.getString(Constants.match),
                            userDataObject.getString(Constants.meet_up),
                            userDataObject.getString(Constants.tiktok),
                            userDataObject.getString(Constants.my_space),
                            userDataObject.getString(Constants.shaadi),
                            userDataObject.getString(Constants.zoosk),
                            userDataObject.getString(Constants.okcupid),
                            userDataObject.getString(Constants.jdate),
                            userDataObject.getString(Constants.hinge),
                            userDataObject.getString(Constants.qq),
                            userDataObject.getString(Constants.wechat),
                            userDataObject.getString(Constants.viber),
                            userDataObject.getString(Constants.quora),
                            userDataObject.getString(Constants.plenty_of_fish),
                            userDataObject.getString(Constants.personal_web_site),
                            userDataObject.getString(Constants.business_web_site)

};






                    otherLinearLayout.setVisibility(View.VISIBLE);
                    innerCircleRequestButton.setVisibility(View.VISIBLE);
                    sendMessageButton.setVisibility(View.VISIBLE);
                   ArrayList<ViewProfileModel> viewProfileModelArrayList2 = new ArrayList<>();

                    for(int i = 0; i < imageIds.length; i++){

                        String url = text[i];

                        if(!url.equals("") && !url.equals("null"))
                            viewProfileModelArrayList2.add(new ViewProfileModel(imageIds[i], url));


                    }

                    viewProfileAdapter = new ViewProfileAdapter(getContext(), viewProfileModelArrayList2);
                    viewProfileRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                    viewProfileRecyclerView.setItemAnimator(new DefaultItemAnimator());

                    viewProfileRecyclerView.setAdapter(viewProfileAdapter);



                    dialog.dismiss();



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


    void blockUser(){

        StringRequest blockRequest = new StringRequest(Request.Method.GET, "http://circlecue.com/api/block.php?id="+preferences.getString(Constants.id, "")+"&id2="+preferences.getString(Constants.othersID, ""), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getContext(), "User Blocked", Toast.LENGTH_SHORT).show();
                blockUserTextView.setText("Unblock User");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Error Blocking", String.valueOf(error));

            }
        });


        blockRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue loginQueue = Volley.newRequestQueue(getContext());

        loginQueue.add(blockRequest);
    }


    void unBlockUser(){

        StringRequest unBlockRequest = new StringRequest(Request.Method.GET, "http://circlecue.com/api/unblock.php?id="+preferences.getString(Constants.id, "")+"&id2="+preferences.getString(Constants.othersID, ""), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getContext(), "User Unblocked", Toast.LENGTH_SHORT).show();
                blockUserTextView.setText("Block User");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Error Blocking", String.valueOf(error));

            }
        });


        unBlockRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue loginQueue = Volley.newRequestQueue(getContext());

        loginQueue.add(unBlockRequest);

    }

    void addToFavorites(){

        StringRequest blockRequest = new StringRequest(Request.Method.GET, "http://circlecue.com/api/addtofav.php?id="+preferences.getString(Constants.id, "")+"&id2="+preferences.getString(Constants.othersID, ""), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                addToFavouriteTextView.setText("Remove from favorites");
                addToFavouriteTextView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_favorite_black_24dp, null), null, null, null);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Error Favorite", String.valueOf(error));

            }
        });


        blockRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue loginQueue = Volley.newRequestQueue(getContext());

        loginQueue.add(blockRequest);


    }

    void removeFromFavorites() {


        StringRequest blockRequest = new StringRequest(Request.Method.GET, "http://circlecue.com/api/removetofav.php?id="+preferences.getString(Constants.id, "")+"&id2="+preferences.getString(Constants.othersID, ""), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                addToFavouriteTextView.setText("Add to favorites");
                addToFavouriteTextView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_favorite, null), null, null, null);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Error Favorite", String.valueOf(error));

            }
        });


        blockRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue loginQueue = Volley.newRequestQueue(getContext());

        loginQueue.add(blockRequest);



    }

    void checkBlock(){


        StringRequest blockRequest = new StringRequest(Request.Method.GET, "http://circlecue.com/api/checkblock.php?sid="+preferences.getString(Constants.id, "")+"&rid="+preferences.getString(Constants.othersID, ""), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    Log.e("BLock Check", jsonObject.getString("block"));

                    if(!jsonObject.getString("block").equals("0")){


                        blockUserTextView.setText("Unblock User");

                        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

                        dialog.setTitle("User Blocked");
                        dialog.setMessage("Do you want to unblock the user?");
                        dialog.setPositiveButton("Unblock", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                unBlockUser();
                            }
                        });
                        dialog.setCancelable(false);
                        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();

                            }
                        });




                        dialog.create().show();

                    }


                } catch (JSONException e) {
                    Log.e("Block check except", String.valueOf(e));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Error Blocking", String.valueOf(error));

            }
        });


        blockRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue loginQueue = Volley.newRequestQueue(getContext());

        loginQueue.add(blockRequest);


    }

}
