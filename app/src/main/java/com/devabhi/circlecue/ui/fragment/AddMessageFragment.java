package com.devabhi.circlecue.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.devabhi.circlecue.Constants;
import com.devabhi.circlecue.R;
import com.devabhi.circlecue.ui.adapter.ChatListAdapter;
import com.devabhi.circlecue.ui.model.ChatListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddMessageFragment extends Fragment {


    RecyclerView chatListRecyclerView;
    ArrayList<ChatListModel> chatListModelArrayList;
    ChatListAdapter chatListAdapter;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        findView(view);

        preferences = getContext().getSharedPreferences(Constants.preference, Context.MODE_PRIVATE);

        getMessagesList();

        chatListModelArrayList = new ArrayList<>();

        return view;
    }

    void findView(View view){

        chatListRecyclerView = view.findViewById(R.id.chatListRecyclerView);

    }

    void getMessagesList(){

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading Messages. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.messageListURL+ preferences.getString(Constants.id, ""), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray chatListArray = new JSONArray(response);


                    Log.e("Chat List response", String.valueOf(chatListArray));

                    for (int i = 0; i < chatListArray.length(); i++){

                        JSONObject chatListObject = chatListArray.getJSONObject(i);


                        chatListModelArrayList.add(new ChatListModel(chatListObject.getString("id"),
                                chatListObject.getString(Constants.username),
                                chatListObject.getString(Constants.country),
                                chatListObject.getString("pic"),
                                chatListObject.getString("msg"),
                                chatListObject.getString("time")));


                    }

                    chatListAdapter = new ChatListAdapter(getContext(), chatListModelArrayList);
                    chatListRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    chatListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    chatListRecyclerView.setAdapter(chatListAdapter);

                    dialog.dismiss();

                }catch (Exception e){
                    Log.e("Chat list Except", String.valueOf(e));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Chat List error", String.valueOf(error));

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue loginQueue = Volley.newRequestQueue(getContext());

        loginQueue.add(stringRequest);


    }
}
