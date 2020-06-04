package com.devabhi.circlecue.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.devabhi.circlecue.ui.adapter.MyCircleAdapter;
import com.devabhi.circlecue.ui.model.CircleItemDataModel;
import com.devabhi.circlecue.ui.model.GetSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyCircleFragment extends Fragment {

    RecyclerView innerCircleRecyclerView, pendingRequestRecyclerView;
    TextView innerCircleTextView, pendingRequestTextView;

    ArrayList<CircleItemDataModel> innerCircleArrayList, pendingRequestArrayList;
    MyCircleAdapter innerCircleAdapter, pendingRequestAdapter;

    SharedPreferences preferences;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_circle, container, false);

        findView(root);

        preferences = getContext().getSharedPreferences(Constants.preference, Context.MODE_PRIVATE);
        fetchCircle();

        return root;
    }

    void findView(View view){

        innerCircleRecyclerView = view.findViewById(R.id.innerCircleRecyclerView);
        pendingRequestRecyclerView = view.findViewById(R.id.pendingRequestsRecyclerView);

        innerCircleTextView = view.findViewById(R.id.innerCircleTextView);
        pendingRequestTextView = view.findViewById(R.id.pendingRequestsTextView);

    }

    void fetchCircle(){

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading Profiles. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();


        StringRequest getUserList = new StringRequest("http://circlecue.com/api/circle.php?sid="+preferences.getString(Constants.id, ""), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray profilesArray = new JSONArray(response);
                    Log.e("Profiles Response", String.valueOf(profilesArray));

                    innerCircleArrayList = new ArrayList<>();
                    pendingRequestArrayList = new ArrayList<>();


                    for(int i = 0; i < profilesArray.length(); i++){

                        JSONObject profile = profilesArray.getJSONObject(i);

                        String fromId = profile.getString("fromid"),
                                toID = profile.getString("toid"),
                                status = profile.getString("status"),
                                rowId = profile.getString("idd"),
                                username = profile.getString("username"),
                                country = profile.getString("country"),
                                pic = profile.getString("pic"),
                                id = profile.getString("id");

                        CircleItemDataModel circleItemData = new CircleItemDataModel(fromId, toID, status, rowId, username, country, pic, id);

                        if(status.equals("1")){

                            innerCircleArrayList.add(circleItemData);

                        }else{
                            pendingRequestArrayList.add(circleItemData);
                        }
                    }

                    innerCircleAdapter = new MyCircleAdapter(getContext(), innerCircleArrayList);
                    pendingRequestAdapter = new MyCircleAdapter(getContext(), pendingRequestArrayList);

                    innerCircleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    innerCircleRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    innerCircleRecyclerView.setAdapter(innerCircleAdapter);

                    innerCircleTextView.setText("Inner Circle "+ innerCircleArrayList.size());

                    pendingRequestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    pendingRequestRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    pendingRequestRecyclerView.setAdapter(pendingRequestAdapter);

                    pendingRequestTextView.setText("Pending " + pendingRequestArrayList.size());



                    dialog.dismiss();


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
