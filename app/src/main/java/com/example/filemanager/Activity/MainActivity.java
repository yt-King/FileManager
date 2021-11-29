package com.example.filemanager.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanager.MyApplication;
import com.example.filemanager.entity.FileView;
import com.example.filemanager.Utils.ItemTouchCallBack;
import com.example.filemanager.R;
import com.example.filemanager.Utils.FileManagerUtils;
import com.example.filemanager.Utils.GetFilesUtils;
import com.example.filemanager.entity.UserContract;
import com.example.filemanager.greendao.UserContractDao;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends BaseActivity {

  private static final String TAG = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void init() {
    super.init();
    if (!path.equals(GetFilesUtils.getInstance().getBasePath())) {
      //添加默认的返回图标
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //设置返回键可用
      getSupportActionBar().setHomeButtonEnabled(true);
    }
    setTitle(new File(path).getName());
    getSupportActionBar().setSubtitle(path);
    RecyclerView recyclerView = findViewById(R.id.file_container);

    ItemTouchCallBack touchCallBack = new ItemTouchCallBack(adapter);
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallBack);
    itemTouchHelper.attachToRecyclerView(recyclerView);
    //  拖拽文件的监听函数
    recyclerView.setOnTouchListener((v, event) -> {
      int type = event.getAction();
      switch (type) {
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_MOVE:
          break;
        case MotionEvent.ACTION_UP:
          Log.i(TAG, "recyclerView touch UP");
          if (adapter.getFromPosition() != -1) {
            adapter.notifyItemRemoved(adapter.getFromPosition());
            FileView source = fileList.get(adapter.getFromPosition());
            FileView target = fileList.get(adapter.getToPosition());
            //  文件/文件夹 拖拽到文件夹，则将文件/文件夹移动到目标文件夹中
            if (target.isFolder()) {
              Log.d(TAG, "folder to folder");
              try {
                FileManagerUtils.Instance.moveToFolder(source.getFile(), target.getFile());
                fileList.remove(source);
                adapter.setItemCheckStates(adapter.getFromPosition(), false);
                adapter.notifyOperationFinish();
                Toast.makeText(this, "移动成功~", Toast.LENGTH_SHORT).show();
              } catch (IOException e) {
                e.printStackTrace();
              }
            } else if (!source.isFolder() && !target.isFolder()) {
              //  文件拖拽到文件，则将两者合并为一个文件夹
              String newFolderName = source.getFileName() + "和" + target.getFileName();
              File newFolder = new File(FilenameUtils.concat(source.getFile().getParent(), newFolderName));
              try {
                FileManagerUtils.Instance.mergeIntoFolder(source.getFile(), target.getFile(), newFolder);
                fileList.remove(source);
                fileList.remove(target);
                fileList.add(new FileView(newFolder));
                adapter.setItemCheckStates(adapter.getFromPosition(), false);
                adapter.notifyOperationFinish();
                Toast.makeText(this, "移动成功~", Toast.LENGTH_SHORT).show();
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
            adapter.setFromPosition(-1);
            return true;
          }
          break;

      }
      return false;
    });
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    //  进入搜索页面
    if (item.getItemId() == R.id.search) {
      Intent intent = new Intent(this, SearchActivity.class);
      intent.putExtra("path", path);
      startActivity(intent);
      return true;
    }
    //进入用户验证界面
    if (item.getItemId() == R.id.changeUser) {
      MyApplication myApplication = (MyApplication) getApplication(); //获取MyApplication得到session
      final View dialogView = LayoutInflater.from(MainActivity.this)
              .inflate(R.layout.cgperson_dialog, null);
      final EditText editText = dialogView.findViewById(R.id.dialog_input1);
      final EditText editText2 = dialogView.findViewById(R.id.dialog_input1);

      editText.requestFocus();

      final AlertDialog dialog =
              new AlertDialog.Builder(MainActivity.this).setTitle("请输入用户名密码").setView(dialogView).setPositiveButton(
                      "确定", null).setNegativeButton("取消", (dialog1, which) -> {
              }).create();
      dialog.setOnShowListener(dialogInterface -> {
        Button positiveBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveBtn.setOnClickListener(v -> {
          String name = editText.getText().toString().trim();
          String pwd = editText2.getText().toString().trim();
          if (TextUtils.isEmpty(name)||TextUtils.isEmpty(pwd)) {
            Toast.makeText(MainActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
          } else {
            myApplication.getDaoSession().insert(new UserContract(null,"yt","123"));
            List<UserContract> user_list = myApplication.getDaoSession().queryBuilder(UserContract.class).where(UserContractDao.Properties.Name.eq("yt")).list();
            if(user_list.size()>0){
              System.out.println("hahahaahahahahahahahahahahahhahahahahahahahaahahhaha");
            }
            dialog.dismiss();
          }
        });
      });
      dialog.show();
      dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.orangeDark));
      dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}