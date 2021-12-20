package com.example.filemanager.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.filemanager.R;
import com.example.filemanager.Utils.HttpUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private HttpUtil httpUtil = new HttpUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        findViewById(R.id.other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, StartActivity.class));
            }
        });

    }


    @BindView(R.id.tv_loginactivity_register)
    TextView mTvLoginactivityRegister;
    @BindView(R.id.rl_loginactivity_top)
    RelativeLayout mRlLoginactivityTop;
    @BindView(R.id.et_loginactivity_username)
    EditText mEtLoginactivityUsername;
    @BindView(R.id.et_loginactivity_password)
    EditText mEtLoginactivityPassword;


    @OnClick({
            R.id.tv_loginactivity_register,
            R.id.bt_loginactivity_login,
    })

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_loginactivity_register:

                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            case R.id.bt_loginactivity_login:
                String name = mEtLoginactivityUsername.getText().toString().trim();
                String password = mEtLoginactivityPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    final boolean[] match = {false};
                    Map usermap = new HashMap();
                    usermap.put("username", name);
                    usermap.put("pwd", password);
                    // Android 4.0 之后不能在主线程中请求HTTP请求
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Map resmap = httpUtil.getLoginDate(usermap);
                                System.out.println("resmap = " + resmap);
                                if (resmap.get("code").equals(200)) {
                                    match[0]=true;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                    try {//等待线程调用结束
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(match[0]);
                    if (match[0]) {
                        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, StartActivity.class);
                        startActivity(intent);
                        finish();//销毁这个Activity
                    } else {
                        Toast.makeText(this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请输入你的用户名或密码", Toast.LENGTH_SHORT).show();
                }
                break;
            //case R.id.tv_loginactivity_else:
            //TODO 第三方登录，时间有限，暂时未实现
            //    break;
        }
    }
}
