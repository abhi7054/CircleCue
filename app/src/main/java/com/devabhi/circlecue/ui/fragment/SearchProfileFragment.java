package com.devabhi.circlecue.ui.fragment;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devabhi.circlecue.Constants;
import com.devabhi.circlecue.R;
import com.devabhi.circlecue.ui.adapter.SearchAdapter;
import com.devabhi.circlecue.ui.model.GetSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.SEARCH_SERVICE;

public class SearchProfileFragment extends Fragment {

    RecyclerView rvUser;
    SearchAdapter searchAdapter;
    ArrayList<GetSearch> arr_Search;
    SearchView searchView;
    SharedPreferences preferences;
    TextView searchTextView;
    ImageView searchImageView;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        findViews(root);

        preferences = getContext().getSharedPreferences(Constants.preference, Context.MODE_PRIVATE);

        arr_Search = new ArrayList<>();

        searchAdapter = new SearchAdapter(getActivity(), arr_Search);
        rvUser.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvUser.setItemAnimator(new DefaultItemAnimator());
        rvUser.setAdapter(searchAdapter);


        if(preferences.getBoolean(Constants.isSearchProfile, true)){

            fetchProfiles(Constants.getUsersList);
            searchView.setVisibility(View.VISIBLE);
            searchTextView.setText("Search Profile");
            searchImageView.setImageResource(R.drawable.search_profile_icon);

        }else{
            searchTextView.setText("My Favorites");
            searchImageView.setImageResource(R.drawable.favourites_icon);
            searchView.setVisibility(View.GONE);
            fetchProfiles("http://circlecue.com/api/favourite.php?sid="+preferences.getString(Constants.id, ""));

        }



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchAdapter.getFilter().filter(newText);
                return false;
            }
        });


        return root;
    }

    void findViews(View view){

        rvUser = view.findViewById(R.id.rvUser);
        searchView = view.findViewById(R.id.searchView);
        searchTextView = view.findViewById(R.id.searchProfileTextView);
        searchImageView = view.findViewById(R.id.searchProfileImageView);


    }

    void fetchProfiles(String url){

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading Profiles. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();


        StringRequest getUserList = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray profilesArray = new JSONArray(response);
                    Log.e("Profiles Response", String.valueOf(profilesArray));

                    for(int i = 0; i < profilesArray.length(); i++){

                        JSONObject profile = profilesArray.getJSONObject(i);

                        arr_Search.add(new GetSearch(profile.getString("id"), profile.getString(Constants.pic), profile.getString(Constants.username), profile.getString(Constants.country)));

                    }

                    dialog.dismiss();
                    searchAdapter.notifyDataSetChanged();


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
}
