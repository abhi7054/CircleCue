package com.devabhi.circlecue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.devabhi.circlecue.ui.fragment.AddMessageFragment;
import com.devabhi.circlecue.ui.fragment.AddNoteFragment;
import com.devabhi.circlecue.ui.fragment.ChangePasswordFragment;
import com.devabhi.circlecue.ui.fragment.EditProfileFragment;
import com.devabhi.circlecue.ui.fragment.HelpFragment;
import com.devabhi.circlecue.ui.fragment.HomeFragment;
import com.devabhi.circlecue.ui.fragment.ViewProfileFragment;
import com.devabhi.circlecue.ui.fragment.MyCircleFragment;
import com.devabhi.circlecue.ui.fragment.NearByFragment;
import com.devabhi.circlecue.ui.fragment.SearchProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView expandedMenu, editProfileImageView;
    private TextView tvHome, tvChangePassword, tvCircle, tvSearch, tvAddnotes, tvHelp, tv_profile, tv_message, tv_nearBy, tv_Logout, usernameTextView, tv_favorites;

    ImageView profilePicImageView;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Boolean changeImage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*binding variables*/
        initViewsMethod();

        /*onclick event*/
        onCLicksMethod();



        /*default fragment*/
        loadFragment(new HomeFragment());

        preferences = getSharedPreferences(Constants.preference, MODE_PRIVATE);

        usernameTextView.setText(preferences.getString(Constants.username, ""));

        Picasso.get().load(Constants.downloadImageUrl + preferences.getString(Constants.pic, "")).placeholder(R.drawable.profile_picture_placeholder).into(profilePicImageView);



    }

    private void initViewsMethod() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        expandedMenu = findViewById(R.id.img_expandmenu);
        tvHome = findViewById(R.id.tv_Home);
        tvCircle = findViewById(R.id.tv_circle);
        tvChangePassword = findViewById(R.id.tv_changePassword);
        tvSearch = findViewById(R.id.tv_search);
        tvHelp = findViewById(R.id.tv_Help);
        tvAddnotes = findViewById(R.id.tv_addNotes);
        tv_profile = findViewById(R.id.tv_profile);
        tv_message = findViewById(R.id.tv_message);
        tv_nearBy = findViewById(R.id.tv_nearBy);
        tv_Logout = findViewById(R.id.tv_Logout);
        usernameTextView = findViewById(R.id.usernameNavigationTextView);
        editProfileImageView = findViewById(R.id.editProfileImageView);
        profilePicImageView = findViewById(R.id.profilePicImageViewNavigation);
        tv_favorites = findViewById(R.id.tv_favourite);


    }

    private void onCLicksMethod() {

        expandedMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDrawer();
            }
        });

        tvHome.setOnClickListener(this);
        tvCircle.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        tvHelp.setOnClickListener(this);
        tvAddnotes.setOnClickListener(this);
        tv_profile.setOnClickListener(this);
        tv_message.setOnClickListener(this);
        tv_nearBy.setOnClickListener(this);
        tv_Logout.setOnClickListener(this);
        editProfileImageView.setOnClickListener(this);
        tv_favorites.setOnClickListener(this);

    }

    private void checkDrawer() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            drawerLayout.openDrawer(navigationView);
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.editProfileImageView:
                changeImage = false;
                editProfileImageView.setVisibility(View.GONE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentLoad, new EditProfileFragment()).addToBackStack(null).commit();
                break;
            case R.id.tv_Home:
                checkDrawer();
                editProfileImageView.setVisibility(View.VISIBLE);
                loadFragment(new HomeFragment());
                break;
            case R.id.tv_profile:
                checkDrawer();
                preferences.edit().putBoolean(Constants.isMyProfile, true).apply();
                editProfileImageView.setVisibility(View.GONE);
                loadFragment(new ViewProfileFragment());
                break;
            case R.id.tv_circle:
                checkDrawer();
                editProfileImageView.setVisibility(View.GONE);
                loadFragment(new MyCircleFragment());
                break;
            case R.id.tv_changePassword:
                checkDrawer();
                editProfileImageView.setVisibility(View.GONE);
                loadFragment(new ChangePasswordFragment());
                break;
            case R.id.tv_search:
                checkDrawer();
                preferences.edit().putBoolean(Constants.isSearchProfile, true).apply();
                editProfileImageView.setVisibility(View.GONE);
                loadFragment(new SearchProfileFragment());
                break;
            case R.id.tv_favourite:
                checkDrawer();
                preferences.edit().putBoolean(Constants.isSearchProfile, false).apply();
                editProfileImageView.setVisibility(View.GONE);
                loadFragment(new SearchProfileFragment());
                break;
            case R.id.tv_addNotes:
                checkDrawer();
                editProfileImageView.setVisibility(View.GONE);
                loadFragment(new AddNoteFragment());
                break;
            case R.id.tv_message:
                checkDrawer();
                editProfileImageView.setVisibility(View.GONE);
                loadFragment(new AddMessageFragment());
                break;
            case R.id.tv_nearBy:
                checkDrawer();
                editProfileImageView.setVisibility(View.GONE);
                loadFragment(new NearByFragment());
                break;
            case R.id.tv_Help:
                checkDrawer();
                editProfileImageView.setVisibility(View.GONE);
                loadFragment(new HelpFragment());
                break;
            case R.id.tv_Logout:
                Intent i = new Intent(MainActivity.this, LoginActivity.class);

                editor = preferences.edit();

                editor.putString(Constants.id, "");
                editor.apply();

                startActivity(i);
                finish();
                break;
        }
    }

    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentLoad, fragment).commit();
    }

    @Override
    public void onBackPressed() {

        if(!changeImage)
            editProfileImageView.setVisibility(View.VISIBLE);
        super.onBackPressed();

    }
}
