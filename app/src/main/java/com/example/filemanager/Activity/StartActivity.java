package com.example.filemanager.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.example.filemanager.MyApplication;
import com.example.filemanager.R;
import com.example.filemanager.entity.UserContract;
import com.example.filemanager.greendao.UserContractDao;
import com.google.android.material.bottomsheet.BottomSheetBehavior;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class StartActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private final GestureDetector gdt = new GestureDetector((GestureDetector.OnGestureListener) new  GestureListener());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.file_container);
//    recyclerView.setOnTouchListener(new View.OnTouchListener() {
//      @Override
//      public boolean onTouch(final View view, final MotionEvent event) {
//        return gdt.onTouchEvent(event);
//      }
//    });
    }

    public void getDate() throws IOException {
        //根据地址创建URL对象(网络访问
        //发布文章的url)
        URL url = new URL("http://150.158.28.238:3000/users/login");
        HttpURLConnection conn = (HttpURLConnection)
                //设置请求的方式
                url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);//发送POST请求必须设置允许输出
        conn.setDoOutput(true);//发送POST请求必须设置允许输入
        //设置请求的头
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Charset", "utf-8");
        String data = "";//传递的数据
        conn.setRequestProperty("Content-Length",
                String.valueOf(data.getBytes().length));
        //获取输出流
        OutputStream os = conn.getOutputStream();
        os.write(data.getBytes());
        os.flush();
        //获取响应的输入流对象
        InputStreamReader is = new InputStreamReader(conn.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(is);
        StringBuffer strBuffer = new StringBuffer();
        String line = null;
        //读取服务器返回信息
        while ((line = bufferedReader.readLine()) != null) {
            strBuffer.append(line);
        }
        String result = strBuffer.toString();//接收从服务器返回的数据
        System.out.println("收到的信息"+result);
        JSONObject json = JSONObject.parseObject(result);
        Map<String, Object> map = (Map<String, Object>)json;
        System.out.println("map = " + map);
        //关闭InputStream、关闭http连接
        is.close();
        conn.disconnect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //  进入搜索页面
        if (item.getItemId() == R.id.search) {
            // Android 4.0 之后不能在主线程中请求HTTP请求
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        getDate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("path", path);
            startActivity(intent);
            return true;
        }
        //进入用户验证界面
        if (item.getItemId() == R.id.changeUser) {
            MyApplication myApplication = (MyApplication) getApplication(); //获取MyApplication得到session
            final View dialogView = LayoutInflater.from(StartActivity.this)
                    .inflate(R.layout.cgperson_dialog, null);
            final EditText editText = dialogView.findViewById(R.id.dialog_input1);
            final EditText editText2 = dialogView.findViewById(R.id.dialog_input2);
            editText.requestFocus();

            final AlertDialog dialog =
                    new AlertDialog.Builder(StartActivity.this).setTitle("请输入用户名密码").setView(dialogView).setPositiveButton(
                            "确定", null).setNegativeButton("取消", (dialog1, which) -> {
                    }).create();
            dialog.setOnShowListener(dialogInterface -> {
                Button positiveBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveBtn.setOnClickListener(v -> {
                    String name = editText.getText().toString().trim();
                    String pwd = editText2.getText().toString().trim();
                    if (TextUtils.isEmpty(name)||TextUtils.isEmpty(pwd)) {
                        Toast.makeText(StartActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                    } else {
//            myApplication.getDaoSession().insert(new UserContract(null,"yt","123"));
                        List<UserContract> user_list = myApplication.getDaoSession()
                                .queryBuilder(UserContract.class)
                                .where(UserContractDao.Properties.Name.eq(name),UserContractDao.Properties.Pwd.eq(pwd))
                                .list();
                        if(user_list.size()<=0){
                            fab.setVisibility(View.GONE);
                            bottomSheetLayout.setVisibility(View.GONE);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            Toast.makeText(this, "该用户不存在", Toast.LENGTH_SHORT).show();
                        }else {
                            fab.setVisibility(View.VISIBLE);
                            bottomSheetLayout.setVisibility(View.GONE);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
            });
            dialog.show();
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //滑动事件
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private final int SWIPE_MIN_DISTANCE = 400;
        private final int SWIPE_THRESHOLD_VELOCITY = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) >  SWIPE_THRESHOLD_VELOCITY) {
                // Right to left, your code here
                System.out.println("Right to left, your code here ");
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE &&  Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//        Dialog loginAlertDialog = new Dialog(MainActivity.this);
//        loginAlertDialog.setContentView (R.layout.activity_main_user);
//        /**
//         * 获取弹出框的窗口对象及参数对象以修改对话框的布局设置,
//         * 可以直接调用getWindow(),表示获得这个Activity的Window
//         * 对象,这样这可以以同样的方式改变这个Activity的属性.
//         */
//        Window dialogWindow = loginAlertDialog.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
//        /**
//         * lp.x与lp.y表示相对于原始位置的偏移.
//         * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
//         * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
//         * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
//         * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
//         * 当参数值包含Gravity.CENTER_HORIZONTAL时
//         * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
//         * 当参数值包含Gravity.CENTER_VERTICAL时
//         * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
//         * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
//         * Gravity.CENTER_VERTICAL.
//         */
////        lp.x = -10; // 新位置X坐标
////        lp.y = -10; // 新位置Y坐标
////        lp.width = 300; // 宽度
////        lp.height = 300; // 高度
////        lp.alpha = 0.7f; // 透明度
//        dialogWindow.setAttributes(lp);
//        /**
//         * 将对话框的大小按屏幕大小的百分比设置
//         */
//        WindowManager m = getWindowManager();
//        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 1); // 高度设置为屏幕的1
//        p.width = (int) (d.getWidth() * 0.85); // 宽度设置为屏幕的0.85
//        dialogWindow.setAttributes(p);
//        loginAlertDialog.show();

//        AlertDialog.Builder loginAlertDialog = new AlertDialog.Builder (MainActivity.this);
//        loginAlertDialog.setView (R.layout.activity_main_user);
//        loginAlertDialog.show ();
                System.out.println("Left to right, your code here \n ");
                return true;
            }
            return false;
        }
    }
}
