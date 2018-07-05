package com.pulse.firebasechat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private RadioGroup radioGroup;
    private CheckBox checkBox;
    private EditText userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.btn_login);
        userName = findViewById(R.id.et_user_name);
        radioGroup = findViewById(R.id.rg_layout);
        checkBox = findViewById(R.id.cb_receive_push_notification);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validationSuccess()){
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("isLoggedOn",true);
                    editor.putString("userName",userName.getText().toString());
                    if(!checkBox.isChecked()) {
                        editor.putBoolean("receivePushNotification", false);
                    }
                    editor.apply();
                    Intent chatActivity = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(chatActivity);
                    LoginActivity.this.finish();
                }
            }
        });
    }

    private boolean validationSuccess() {
        if(TextUtils.isEmpty(userName.getText())) {
            userName.setError("UserName is Required");
            return false;
        } else if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(
                    this,
                    "Please Check whether you want a chat of type public or private",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;
    }
}
