package com.devabhi.circlecue.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment {

    private ImageView image;
    private Uri image_URI;
    private MyPermissionClass permissionClass;

    private TextView usernameTextView, countryTextView;

    private Button showPersonalInfoButton, showGroupAButton, showGroupBButton, showGroupCButton, showGroupDButton, showGroupEButton, updateButton;
    private LinearLayout personalInfoLayout, groupALinearLayout, groupBLinearLayout, groupCLinearLayout, groupDLinearLayout, groupELinearLayout;

    private EditText emailEditText, facebookEDitText, instaEditText, linkedInEditText, twitterEditText, websiteEditText, pinterestEditText,
                snapChatEditText, youtubeEditText, skypeEditText, whatsappEditText, viberEditText, matchEditText, yelpEditText, meetUpEditText,
                tiktokEditText, tinderEditText, okCupidEditText, xooksEditText, hingeEditText, plentyOfFishEditText, wwChatEditText, tumblrEditText,
                flickrEditText, quoraEditText, bumbleEditText;

    CheckBox emailNotificationCheckbox, showLocationCheckBox, facebookCheckBox, instaCheckBox, linkedInCheckBox, twitterCheckbox, websiteCheckBox,
            pinterestCheckBox, snapChatCheckBox, youtubeCheckBox, skypeCheckBox, whatsAppCheckBOx, viberCheckBOx, matchCheckBox, yelpCheckBox,
            meetUpCheckBox, tiktokCheckBox, tinderCheckBox, okCupidCheckBox, zooksCheckBox, hingeCheckBox, plentyOfFishCheckBox, weChatCheckBox,
            tumblrCheckBox, flickrCheckBox, quoraCheckBox, bumbleCheckBox;

    SharedPreferences preferences;
    SharedPreferences preferencesSocial;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);


        permissionClass = new MyPermissionClass(getActivity());

        fetchLayouts(view);


        preferences = getContext().getSharedPreferences(Constants.preference, Context.MODE_PRIVATE);
        preferencesSocial= getContext().getSharedPreferences("Social", Context.MODE_PRIVATE);


        usernameTextView.setText(preferences.getString(Constants.username, ""));
        countryTextView.setText(preferences.getString(Constants.country, ""));

        Picasso.get().load(Constants.downloadImageUrl + preferences.getString(Constants.pic, "")).placeholder(R.drawable.profile_picture_placeholder).into(image);




        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (permissionClass.camera()) {
                        Intent gallreyIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(gallreyIntent, 100);
                    }
                } else {

                    permissionClass.camera();
                    Intent gallreyIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(gallreyIntent, 100);
                }
            }
        });

       hideShowLayout(showPersonalInfoButton, personalInfoLayout);
       hideShowLayout(showGroupAButton, groupALinearLayout);
       hideShowLayout(showGroupBButton, groupBLinearLayout);
       hideShowLayout(showGroupCButton, groupCLinearLayout);
       hideShowLayout(showGroupDButton, groupDLinearLayout);
       hideShowLayout(showGroupEButton, groupELinearLayout);


       updateButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               editProfile();

           }
       });

        return view;
    }

    private void hideShowLayout(final Button button, final LinearLayout linearLayout){

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(linearLayout.getVisibility() == View.VISIBLE){

                    linearLayout.setVisibility(View.GONE);
                    button.setText("Show");

                }else {

                    linearLayout.setVisibility(View.VISIBLE);
                    button.setText("Hide");

                }

            }
        });

    }

    void fetchLayouts(View view){

        image = view.findViewById(R.id.profilePicEditImageView);
        usernameTextView = view.findViewById(R.id.usernameEditProfileTextView);
        countryTextView = view.findViewById(R.id.countryEditProfileTextView);
        showPersonalInfoButton = view.findViewById(R.id.showPersonalInfoButton);
        personalInfoLayout = view.findViewById(R.id.personalInfoLinearLayout);
        groupALinearLayout = view.findViewById(R.id.groupALinearLayout);
        groupBLinearLayout = view.findViewById(R.id.groupBLinearLayout);
        groupCLinearLayout = view.findViewById(R.id.groupCLinearLayout);
        groupDLinearLayout = view.findViewById(R.id.groupDLinearLayout);
        groupELinearLayout = view.findViewById(R.id.groupELinearLayout);
        showGroupAButton = view.findViewById(R.id.showGroupAInfo);
        showGroupBButton = view.findViewById(R.id.showGroupBInfo);
        showGroupCButton = view.findViewById(R.id.showGroupCInfo);
        showGroupDButton = view.findViewById(R.id.showGroupDInfo);
        showGroupEButton = view.findViewById(R.id.showGroupEInfo);
        updateButton = view.findViewById(R.id.profileUpdateButton);


        emailEditText = view.findViewById(R.id.updateProfileEmailEditText);
        facebookEDitText = view.findViewById(R.id.editProfileFacebookEditText);
        instaEditText = view.findViewById(R.id.editProfileInstaEditText);
        linkedInEditText = view.findViewById(R.id.editProfileLinkedInEditText);
        twitterEditText = view.findViewById(R.id.editProfileTwitterEditText);
        websiteEditText = view.findViewById(R.id.editProfileWebsiteEditText);
        pinterestEditText = view.findViewById(R.id.editProfilePinterestEditText);
        snapChatEditText = view.findViewById(R.id.editProfileSnapChatEditText);
        youtubeEditText = view.findViewById(R.id.editProfileYoutubeEditText);
        skypeEditText = view.findViewById(R.id.editProfileSkypeEditText);
        whatsappEditText = view.findViewById(R.id.editProfileWhatsappEditText);
        viberEditText = view.findViewById(R.id.editProfileViberEditText);
        matchEditText = view.findViewById(R.id.editProfileMatchEditText);
        yelpEditText = view.findViewById(R.id.editProfileYelpEditText);
        meetUpEditText = view.findViewById(R.id.editProfileMeetUpEditText);
        tiktokEditText = view.findViewById(R.id.editProfileTikTokEditText);
        tinderEditText = view.findViewById(R.id.editProfileTinderEditText);
        okCupidEditText = view.findViewById(R.id.editProfileOkCupidEditText);
        xooksEditText = view.findViewById(R.id.editProfileZooskEditText);
        hingeEditText = view.findViewById(R.id.editProfileHingeEditText);
        plentyOfFishEditText = view.findViewById(R.id.editProfilePlentyOfFishEditText);
        wwChatEditText = view.findViewById(R.id.editProfileWeChatEditText);
        tumblrEditText = view.findViewById(R.id.editProfileTumblrEditText);
        flickrEditText = view.findViewById(R.id.editProfileFlickrEditText);
        quoraEditText = view.findViewById(R.id.editProfileQuoraEditText);
        bumbleEditText = view.findViewById(R.id.editProfileBumbleEditText);

        emailNotificationCheckbox = view.findViewById(R.id.emailNotificationCheckbox);
        showLocationCheckBox = view.findViewById(R.id.showLocationCheckbox);
        facebookCheckBox = view.findViewById(R.id.EditprofileFacebookCheckbox);
        instaCheckBox = view.findViewById(R.id.EditprofileInstaCheckbox);
        linkedInCheckBox = view.findViewById(R.id.EditprofileLinkedInCheckbox);
        twitterCheckbox = view.findViewById(R.id.EditProfileTwitterCheckbox);
        websiteCheckBox = view.findViewById(R.id.EditProfileWebsiteCheckbox);
        pinterestCheckBox = view.findViewById(R.id.EditprofilePinterestCheckbox);
        snapChatCheckBox = view.findViewById(R.id.EditprofileSnapChatCheckbox);
        youtubeCheckBox = view.findViewById(R.id.EditprofileYoutubeCheckbox);
        skypeCheckBox = view.findViewById(R.id.EditProfileSkypeCheckbox);
        whatsAppCheckBOx = view.findViewById(R.id.EditProfileWhatsappCheckbox);
        viberCheckBOx = view.findViewById(R.id.EditprofileViberCheckbox);
        matchCheckBox = view.findViewById(R.id.EditprofileMatchCheckbox);
        yelpCheckBox = view.findViewById(R.id.EditprofileYelpCheckbox);
        meetUpCheckBox = view.findViewById(R.id.EditProfileMeetUpCheckbox);
        tiktokCheckBox = view.findViewById(R.id.EditProfileTikTokCheckbox);
        tinderCheckBox = view.findViewById(R.id.EditprofileTinderCheckbox);
        okCupidCheckBox = view.findViewById(R.id.EditprofileOKCupidCheckbox);
        zooksCheckBox = view.findViewById(R.id.EditprofileZooksCheckbox);
        hingeCheckBox = view.findViewById(R.id.EditProfileHingeCheckbox);
        plentyOfFishCheckBox = view.findViewById(R.id.EditProfilePlentyOfFishCheckbox);
        weChatCheckBox = view.findViewById(R.id.EditprofileWeChatCheckbox);
        tumblrCheckBox = view.findViewById(R.id.EditprofileTumblrCheckbox);
        flickrCheckBox = view.findViewById(R.id.EditprofileFlickrCheckbox);
        quoraCheckBox = view.findViewById(R.id.EditProfileQuoraCheckbox);
        bumbleCheckBox = view.findViewById(R.id.EditProfileBumbleCheckbox);


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

    String checkBoxStatus(CheckBox checkBox){

        if(checkBox.isChecked())
            return "1";
        else
            return "0";

    }

    void editProfile(){

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Updating Profile");
        dialog.setMessage("Updating. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();

            StringRequest updateProfile = new StringRequest(Request.Method.POST, Constants.updateProfileUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject profileUpdateResponse = new JSONObject(response);

                        Log.e("Update profile response", String.valueOf(profileUpdateResponse));

                        dialog.dismiss();

                    } catch (JSONException e) {
                        Log.e("Json Decoding error", String.valueOf(e));
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> param = new HashMap<String, String>();

                    param.put("userid", preferences.getString(Constants.id, ""));
                    param.put("username", preferences.getString(Constants.username, ""));
                    param.put("email", preferences.getString(Constants.email, ""));
                    param.put("mobile", preferences.getString(Constants.phone, ""));
                    param.put("notification", checkBoxStatus(emailNotificationCheckbox));
                    param.put("llocation", checkBoxStatus(showLocationCheckBox));
                    param.put("facebook", paramValue(facebookEDitText, 0));
                    param.put("facebook_status", checkBoxResult(facebookCheckBox, 0));
                    param.put("instagram", paramValue(instaEditText, 1));
                    param.put("instagram_status", checkBoxResult(instaCheckBox, 1));
                    param.put("linkedIn", paramValue(linkedInEditText, 2));
                    param.put("linkedIn_status", checkBoxResult(linkedInCheckBox, 2));
                    param.put("twitter", paramValue(twitterEditText, 3));
                    param.put("twitter_status", checkBoxResult(twitterCheckbox, 3));
                    param.put("skype", paramValue(skypeEditText, 5));
                    param.put("skype_status", checkBoxResult(skypeCheckBox, 5));
                    param.put("you_tube", paramValue(youtubeEditText, 6));
                    param.put("you_tube_status", checkBoxResult(youtubeCheckBox, 6));
                    param.put("tinder", paramValue(tinderEditText, 7));
                    param.put("tinder_status", checkBoxResult(tinderCheckBox, 7));
                    param.put("reddit", paramValue(yelpEditText, 8));
                    param.put("reddit_status", checkBoxResult(yelpCheckBox, 8));
                    param.put("flickr", paramValue(flickrEditText, 9));
                    param.put("flickr_status", checkBoxResult(flickrCheckBox, 9));
                    param.put("whatsapp", paramValue(whatsappEditText, 10));
                    param.put("whatsapp_status", checkBoxResult(whatsAppCheckBOx, 10));
                    param.put("tumblr", paramValue(tumblrEditText, 11));
                    param.put("tumblr_status", checkBoxResult(tumblrCheckBox, 11));
                    param.put("match", paramValue(matchEditText, 12));
                    param.put("match_status", checkBoxResult(matchCheckBox,12));
                    param.put("meet_up", paramValue(meetUpEditText, 13));
                    param.put("meet_up_status", checkBoxResult(meetUpCheckBox,13));
                    param.put("tiktok", paramValue(tiktokEditText, 14));
                    param.put("tiktok_status", checkBoxResult(tinderCheckBox, 14));
                    param.put("shaadi", paramValue(pinterestEditText, 16));
                    param.put("shaadi_status", checkBoxResult(pinterestCheckBox, 16));
                    param.put("zoosk", paramValue(xooksEditText, 17));
                    param.put("zoosk_status", checkBoxResult(zooksCheckBox, 17));
                    param.put("okcupid", paramValue(okCupidEditText, 18));
                    param.put("okcupid_status", checkBoxResult(okCupidCheckBox, 18));
                    param.put("hinge", paramValue(hingeEditText, 20));
                    param.put("hinge_status", checkBoxResult(hingeCheckBox, 20));
                    param.put("qq", paramValue(bumbleEditText, 21));
                    param.put("qq_status", checkBoxResult(bumbleCheckBox, 21));
                    param.put("wechat", paramValue(wwChatEditText, 22));
                    param.put("wechat_status", checkBoxResult(weChatCheckBox, 22));
                    param.put("viber", paramValue(viberEditText, 23));
                    param.put("viber_status", checkBoxResult(viberCheckBOx, 23));
                    param.put("quora", paramValue(quoraEditText, 24));
                    param.put("quora_status", checkBoxResult(quoraCheckBox, 24));
                    param.put("plenty_of_fish", paramValue(plentyOfFishEditText, 25));
                    param.put("plenty_of_fish_status", checkBoxResult(plentyOfFishCheckBox, 25));
                    param.put("snap_chat", paramValue(snapChatEditText, 4));
                    param.put("snap_chat_status", checkBoxResult(snapChatCheckBox, 4));


                    return param;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(getContext());

            updateProfile.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(updateProfile);





    }


    String paramValue(EditText editText, Integer position){


        if(editText.getText().toString().equals(""))
            return preferencesSocial.getString("text"+position, "");
        else
            return editText.getText().toString();


    }

    String checkBoxResult(CheckBox checkBox, Integer position){

        String isChecked;
        String result;

        if(checkBox.isChecked())
            isChecked = "1";
        else
            isChecked = "0";

        if(!isChecked.equals(preferencesSocial.getString("private"+position, ""))) {
            result = isChecked;
        } else
            result = preferencesSocial.getString("private"+position, "");


        return result;
    }
}
