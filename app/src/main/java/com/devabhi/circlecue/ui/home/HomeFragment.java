package com.devabhi.circlecue.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.devabhi.circlecue.Constants;
import com.devabhi.circlecue.MainActivity;
import com.devabhi.circlecue.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    String picString;
    CircleImageView profileImageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        profileImageView = root.findViewById(R.id.profilePicImageView);


        picString = getActivity().getSharedPreferences(Constants.preference, Context.MODE_PRIVATE).getString(Constants.pic, "null");


        Picasso.get().load(Constants.downloadImageUrl+picString).error(R.drawable.profile_picture_placeholder).placeholder(R.drawable.profile_picture_placeholder).into(profileImageView);

        Log.e(picString, picString);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        profileImageView.setImageResource(R.drawable.profile_picture_placeholder);

    }

    @Override
    public void onStart() {

        profileImageView.setImageResource(R.drawable.profile_picture_placeholder);

        super.onStart();
    }
}
