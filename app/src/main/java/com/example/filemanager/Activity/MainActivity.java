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
      public void onClick(View view) {startActivity(new Intent(MainActivity.this,StartActivity.class));
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
          final boolean[] match = {true};
          Map usermap = new HashMap();
          usermap.put("username",name);
          usermap.put("pwd",password);
          // Android 4.0 之后不能在主线程中请求HTTP请求
          new Thread(new Runnable(){
            @Override
            public void run() {
              try {
                Map resmap = httpUtil.getLoginDate(usermap);
                System.out.println("resmap = " + resmap);
                if(resmap.get("code").equals("200")){
                  match[0] =true;
                }
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }).start();
          /**
           * 延时函数为了上一个线程可以拿到数据并修改状态（很蠢而且不一定成功的方法，但是还没想到其他的方法）
           * 因为调用http后台接口只能开线程执行，但是其他的东西放不到线程里去，这个问题待解决！！！
           */
          new Thread(new Runnable() {
            @Override
            public void run() {
              try {
                Thread.sleep(1000);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          }).start();
          if (match[0]) {
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            finish();//销毁这个Activity
          }else {
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
