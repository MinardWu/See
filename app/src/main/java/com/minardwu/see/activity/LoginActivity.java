package com.minardwu.see.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.minardwu.see.R;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private MaterialEditText et_username;
    private MaterialEditText et_password;
    private Button btn_login;
    private TextView tv_noaccount;
    private TextView tv_forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        et_username = (MaterialEditText) findViewById(R.id.et_username);
        et_password = (MaterialEditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_noaccount = (TextView) findViewById(R.id.tv_noaccount);
        tv_forgetPassword = (TextView) findViewById(R.id.tv_forgetPassword);

        et_username.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        btn_login.setOnClickListener(this);
        tv_noaccount.setOnClickListener(this);
        tv_forgetPassword.setOnClickListener(this);

        btn_login.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                break;
            case R.id.tv_noaccount:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
            case R.id.tv_forgetPassword:

                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (et_username.getText().toString().length() != 0 && et_password.getText().toString().length() != 0) {
            btn_login.setEnabled(true);
        } else {
            btn_login.setEnabled(false);
        }
    }
}
