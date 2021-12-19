package com.example.filemanager.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.filemanager.R;
import com.example.filemanager.Utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {
    private HttpUtil httpUtil = new HttpUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @BindView(R.id.et_registeractivity_username)
    EditText mEtRegisteractivityUsername;
    @BindView(R.id.et_registeractivity_password1)
    EditText mEtRegisteractivityPassword1;
    @BindView(R.id.et_registeractivity_password2)
    EditText mEtRegisteractivityPassword2;

    /**
     * 注册页面能点击的就三个地方
     * top处返回箭头、刷新验证码图片、注册按钮
     */
    @OnClick({
            R.id.iv_registeractivity_back,
            R.id.bt_registeractivity_register
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_registeractivity_back: //返回登录页面
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.bt_registeractivity_register:    //注册按钮
                //获取用户输入的用户名、密码、验证码
                String username = mEtRegisteractivityUsername.getText().toString().trim();
                String password = mEtRegisteractivityPassword2.getText().toString().trim();
                //注册验证
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    //将用户名和密码加入到数据库中
                    System.out.println("username = " + username);
                    System.out.println("password = " + password);
                    Map usermap = new HashMap();
                    usermap.put("username",username);
                    usermap.put("pwd",password);
                    // Android 4.0 之后不能在主线程中请求HTTP请求
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                Map resmap = httpUtil.getLoginDate(usermap);
                                System.out.println("resmap = " + resmap);
                                if(resmap.get("code").equals("200")){
//                                    match[0] =true;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    Intent intent2 = new Intent(this, MainActivity.class);
                    startActivity(intent2);
                    finish();
                    Toast.makeText(this,  "验证通过，注册成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "未完善信息，注册失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

