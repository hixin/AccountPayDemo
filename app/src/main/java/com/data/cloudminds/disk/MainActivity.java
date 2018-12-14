package com.data.cloudminds.disk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.cloudminds.app.dataserver.UserInfo;
import com.cloudminds.app.dataserver.client.Constants;
import com.cloudminds.app.dataserver.client.DataServiceApi;
import com.cloudminds.app.dataserver.client.PermissionActivity;

import static com.cloudminds.app.dataserver.client.Constants.LOGOUT_SUCCESS;
import static com.cloudminds.app.dataserver.client.Constants.MODIFY_PASSWORD_SUCCESS;

public class MainActivity extends PermissionActivity {
    private static final String TAG = "MainActivity";
    private static final int PAY_CODE = 0;
    private ImageView imageView;
    private TextView textView;
    private UserInfo mUserInfo;
    private String flag = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataServiceApi.getInstance().getInstance().init(this);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.user_image);
        textView = findViewById(R.id.user_name);

        findViewById(R.id.Login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataServiceApi.getInstance().login(MainActivity.this, new DataServiceApi.AccountCallback() {
                    @Override
                    public void onUserInfoResponse(@NonNull final UserInfo userInfo) {
                        mUserInfo = userInfo;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(userInfo.getNick());
                                Glide.with(MainActivity.this)
                                        .load(userInfo.getAvatar())
                                        .into(imageView);
                            }
                        });

                    }

                    @Override
                    public void onOtherInfoResponse(@Nullable String status, @Nullable String msg) {

                    }

                });
            }
        });

        findViewById(R.id.LoginOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataServiceApi.getInstance().logout(MainActivity.this, new DataServiceApi.AccountCallback() {

                    @Override
                    public void onUserInfoResponse(@NonNull UserInfo userInfo) {
                    }

                    @Override
                    public void onOtherInfoResponse(@Nullable String status, @Nullable String msg) {
                        Log.i(TAG, "onOtherInfoResponse: " + status);
                        mUserInfo = null;
                        flag = msg;
                    }

                });
            }
        });


        findViewById(R.id.modify_userInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataServiceApi.getInstance().readWriteUserInfo(MainActivity.this, mUserInfo, new DataServiceApi.AccountCallback() {
                    @Override
                    public void onUserInfoResponse(@NonNull final UserInfo userInfo) {
                        Log.i(TAG, "onUserInfoResponse: " + userInfo.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(userInfo.getNick());
                                Glide.with(MainActivity.this)
                                        .load(userInfo.getAvatar())
                                        .into(imageView);
                            }
                        });
                    }

                    @Override
                    public void onOtherInfoResponse(@Nullable String status, @Nullable String msg) {
                        Log.i(TAG, "onOtherInfoResponse: " + msg);
                        flag = msg;
                    }
                });
            }
        });


        findViewById(R.id.test_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataServiceApi.getInstance().payOrder(MainActivity.this, "Test pay subject", 0.01, PAY_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (requestCode == PAY_CODE) {
                String payResult = data.getStringExtra(Constants.ARG_PAY_RESULT);
                Log.i(TAG, "onActivityResult: " + payResult);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag.equals(MODIFY_PASSWORD_SUCCESS)) {
            flag = "";
            //TODO modify password
        }

        if(flag.equals(LOGOUT_SUCCESS)) {
            flag = "";
            //TODO  logout
        }
    }
}
