package com.devabhi.circlecue.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devabhi.circlecue.Constants;
import com.devabhi.circlecue.MainActivity;
import com.devabhi.circlecue.MyPermissionClass;
import com.devabhi.circlecue.R;
import com.devabhi.circlecue.ui.adapter.ChatRoomAdapter;
import com.devabhi.circlecue.ui.model.ChatRoomDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class ChatRoomFragment extends Fragment {

    RecyclerView chatRecyclerView;
    Button sendButton, attachmentButton, blockButton, reportButton;
    EditText messageEditText;
    Bitmap bitmap;
    private Uri image_URI;
    String imageName;


    String sid, rid;

    private MyPermissionClass permissionClass;

    SharedPreferences preferences;
    ArrayList<ChatRoomDataModel> chatRoomDataModelArrayList;
    ChatRoomAdapter chatRoomAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chatroom, container, false);

        preferences = getContext().getSharedPreferences(Constants.preference, Context.MODE_PRIVATE);
        sid = preferences.getString(Constants.id, "");
        rid = preferences.getString(Constants.othersID, "");

        permissionClass = new MyPermissionClass(getActivity());

        Log.e("Random String", random());

        checkBlock();

        chatRoomDataModelArrayList = new ArrayList<>();

        findView(view);

        getMessages();
        setOnClickListeners();

        return view;
    }

    void findView(View view){

        chatRecyclerView = view.findViewById(R.id.chatRoomRecyclerView);
        sendButton = view.findViewById(R.id.chatRoomSendMessageButton);
        attachmentButton = view.findViewById(R.id.chatRoomAttachmentButton);
        messageEditText = view.findViewById(R.id.chatRoomMessageEditText);
        blockButton = view.findViewById(R.id.blockChatButton);
        reportButton = view.findViewById(R.id.reportButton);

    }

    void setOnClickListeners(){

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        blockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(blockButton.getText().toString().equals("Block User"))
                    blockUser();
                else
                    unBlockUser();

            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "User Reported", Toast.LENGTH_SHORT).show();
            }
        });

        attachmentButton.setOnClickListener(new View.OnClickListener() {
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
    }


    void sendMessage(){

        StringRequest sendMessageRequest = new StringRequest(Request.Method.POST, Constants.sendMsgURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.getString("Status").equals("true"))

                        messageEditText.getText().clear();

                        getMessages();

                } catch (JSONException e) {
                    Log.e("send msg except", String.valueOf(e));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Send msg error", String.valueOf(error));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();

                param.put("sid", sid);
                param.put("rid", rid);
                param.put("msg", messageEditText.getText().toString());

                return param;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());

        sendMessageRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(sendMessageRequest);

    }



    void getMessages(){

        StringRequest getMessageRequest = new StringRequest(Request.Method.GET, Constants.chatListURL + "sid=" + sid + "&rid=" + rid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.e("Message List", response);
                    JSONArray messagesArray = new JSONArray(response);

                    chatRoomDataModelArrayList.clear();

                    for (int i = 0; i < messagesArray.length(); i++){

                        JSONObject messageObject = messagesArray.getJSONObject(i);

                        ChatRoomDataModel chatRoomData = new ChatRoomDataModel(messageObject.getString("id"),
                                messageObject.getString("sender_id"), messageObject.getString("receiver_id"),
                                messageObject.getString("message"), messageObject.getString("media"),
                                messageObject.getString("created_at"), messageObject.getString("type"),
                                messageObject.getString("block"));

                        chatRoomDataModelArrayList.add(chatRoomData);

                    }

                    chatRoomAdapter = new ChatRoomAdapter(getContext(), chatRoomDataModelArrayList);
                    chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    chatRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    chatRecyclerView.setAdapter(chatRoomAdapter);
                    chatRecyclerView.scrollToPosition(chatRoomAdapter.getItemCount()-1);

                }catch (Exception e){
                    Log.e("Get msgs execp", String.valueOf(e));
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Get msgs error", String.valueOf(error));
            }
        });


        getMessageRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue loginQueue = Volley.newRequestQueue(getContext());

        loginQueue.add(getMessageRequest);

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


                        blockButton.setText("Unblock User");

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


    void blockUser(){

        StringRequest blockRequest = new StringRequest(Request.Method.GET, "http://circlecue.com/api/block.php?id="+preferences.getString(Constants.id, "")+"&id2="+preferences.getString(Constants.othersID, ""), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getContext(), "User Blocked", Toast.LENGTH_SHORT).show();
                blockButton.setText("Unblock User");

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
                blockButton.setText("Block User");

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

    private static String random() {
        final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        StringBuilder sb = new StringBuilder(30);

            for (int i = 0; i < 30; i++) {
                sb.append(DATA.charAt(new Random().nextInt(DATA.length())));
            }

            return sb.toString();

    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            image_URI = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), image_URI);
            } catch (IOException e) {
                e.printStackTrace();
            }




        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionClass.resultCamera(requestCode, grantResults)) {

            imageName = random();

        } else {
            Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }


}
