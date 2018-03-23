package com.lifecyclehealth.lifecyclehealth.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.utils.AESHelper;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.lifecyclehealth.lifecyclehealth.utils.NetworkRequestUtil;

import zemin.notification.NotificationDelegater;
import zemin.notification.NotificationLocal;
import zemin.notification.NotificationView;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.seedValue;

public class ChangeUsername extends BaseActivity implements View.OnClickListener {

    private TextView user_name, txt_use_different_id, back;
    private ImageView backArrowBtn;
    LinearLayout clickSameUser;
    private NotificationDelegater mDelegater;
    private NotificationLocal mLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);
        initialize();
    }

    private void initialize() {
        user_name = (TextView) findViewById(R.id.user_name);
        clickSameUser= (LinearLayout) findViewById(R.id.clickSameUser);
        clickSameUser.setOnClickListener(this);
        try {
            user_name.setText(AESHelper.decrypt(seedValue, MyApplication.getInstance().getFromSharedPreference(AppConstants.EMAIL_ID)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        txt_use_different_id = (TextView) findViewById(R.id.txt_use_different_id);
        back = (TextView) findViewById(R.id.back);
        backArrowBtn = (ImageView) findViewById(R.id.backArrowBtn);
        mDelegater = NotificationDelegater.getInstance();
        mLocal = mDelegater.local();
        mLocal.setView((NotificationView) findViewById(R.id.nv));
        txt_use_different_id.setOnClickListener(this);
        back.setOnClickListener(this);
        backArrowBtn.setOnClickListener(this);
    }

    @Override
    String getTag() {
        return "TouchIDActivity";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_use_different_id: {
                MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_USERNAME_EDITABLE, true);
                finish();
                break;
            }

            case R.id.clickSameUser:
                finish();

            case R.id.backArrowBtn:
                finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
