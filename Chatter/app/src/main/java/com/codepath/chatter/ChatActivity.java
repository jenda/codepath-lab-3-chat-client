package com.codepath.chatter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChatActivity extends AppCompatActivity {
    static final String USER_ID_KEY = "userId";
    static final String BODY_KEY = "body";

    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.btSend)
    Button btSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        if (ParseUser.getCurrentUser() != null) { // start with existing user
            startWithCurrentUser();
        } else { // If not logged in, login as a new anonymous user
            login();
        }
    }


    // Get the userId from the cached currentUser object
    void startWithCurrentUser() {
    }

    // Setup button event handler which posts the entered message to Parse
    @OnClick(R.id.btSend)
    void setupMessagePosting() {

        String data = etMessage.getText().toString();
        ParseObject message = ParseObject.create("Message");
        message.put(USER_ID_KEY, ParseUser.getCurrentUser().getObjectId());
        message.put(BODY_KEY, data);
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Toast.makeText(ChatActivity.this, "Successfully created message on Parse",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("err", "Failed to save message", e);
                }
            }
        });
        etMessage.setText(null);
    }


    // Create an anonymous user using ParseAnonymousUtils and set sUserId
    void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e("failed", "Anonymous login failed: ", e);
                    e.printStackTrace();
                } else {
                    startWithCurrentUser();
                }
            }
        });
    }

}
