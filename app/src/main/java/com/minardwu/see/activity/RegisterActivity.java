package com.minardwu.see.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.minardwu.see.R;
import com.minardwu.see.event.RegisterEvent;
import com.minardwu.see.net.Register;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RegisterActivity extends AppCompatActivity implements TextWatcher {

    private MaterialEditText et_username;
    private MaterialEditText et_password;
    private MaterialEditText et_comfirmpsd;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        et_username = (MaterialEditText) findViewById(R.id.et_username);
        et_password = (MaterialEditText) findViewById(R.id.et_password);
        et_comfirmpsd = (MaterialEditText) findViewById(R.id.et_comfirmpsw);
        btn_register = (Button) findViewById(R.id.btn_register);

        et_username.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        et_comfirmpsd.addTextChangedListener(this);

        btn_register.setEnabled(false);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_register.setEnabled(false);
                Register.register(et_username.getText().toString(),et_password.getText().toString());
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterEvent(RegisterEvent event){
        if(event.getResult()==1){
            Toast.makeText(this,"注册成功",Toast.LENGTH_LONG).show();
            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            this.finish();
        }else if(event.getResult()==-1){
            et_username.setError("用户名已存在");
            Toast.makeText(this,"用户名已存在",Toast.LENGTH_LONG).show();
        }else if(event.getResult()==-2){
            et_comfirmpsd.setError("该手机号已被占用");
            Toast.makeText(this,"该手机号已被占用",Toast.LENGTH_LONG).show();
        }else if(event.getResult()==-3){
            Toast.makeText(this,"注册出错了",Toast.LENGTH_LONG).show();
        }
        btn_register.setEnabled(true);
    };


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        if (et_username.getText().toString().length() != 0
                && et_password.getText().toString().length() != 0
                && et_comfirmpsd.getText().toString().length() != 0) {
            if(et_password.getText().toString().equals(et_comfirmpsd.getText().toString())){
                btn_register.setEnabled(true);
            }else {
                et_comfirmpsd.setError("两次密码不一致");
            }
        } else {
            btn_register.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
