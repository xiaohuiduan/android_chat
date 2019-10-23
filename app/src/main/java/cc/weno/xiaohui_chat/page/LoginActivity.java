package cc.weno.xiaohui_chat.page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import cc.weno.xiaohui_chat.R;
import cc.weno.xiaohui_chat.dao.LoginDao;
import cc.weno.xiaohui_chat.dao.ResponseDao;
import cc.weno.xiaohui_chat.mapper.MyDatabaseHelper;
import cc.weno.xiaohui_chat.net.AsyncRequest;
import cc.weno.xiaohui_chat.net.ProcessInterface;
import lombok.Data;

public class LoginActivity extends AppCompatActivity {

    private TextView registerText;

    private EditText nameText;
    private EditText pwdText;
    private Button loginButton;
    private CheckBox saveBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        initData();
    }

    /**
     * 从数据库中间获取账号密码
     */
    private void initData() {

        SQLiteOpenHelper sqLiteOpenHelper = new MyDatabaseHelper(this, "xiaohui_chat", null, 1);
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("user", new String[]{"name", "pwd"}, null, null, null, null, null);

        // 通过游标的方法可迭代查询结果
        if (cursor.moveToFirst()) {

            String name = cursor.getString(cursor.getColumnIndex("name"));
            String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
            nameText.setText(name);
            pwdText.setText(pwd);
        }

        sqLiteDatabase.close();
    }

    private void init() {

        nameText = findViewById(R.id.login_name);
        pwdText = findViewById(R.id.login_pwd);

        registerText = findViewById(R.id.register_text);
        loginButton = findViewById(R.id.login_button);
        saveBox = findViewById(R.id.save_user_info);


        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String name = nameText.getText().toString();
        String pwd = pwdText.getText().toString();

        if (name.length() == 0 || pwd.length() == 0) {
            Toast.makeText(this, "请输入账号或者密码", Toast.LENGTH_SHORT).show();
        } else {
            // 异步发送数据
            AsyncTask asyncTask = new AsyncRequest().execute(new SendLogin(name, pwd));
            try {
                boolean success = (boolean) asyncTask.get();
                if (success) {
                    turnChat();
                }else {
                    Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 跳转到聊天页面
     */
    private void turnChat() {

        // 进行保存密码
        if (saveBox.isChecked()) {
            saveInfo(nameText.getText().toString(), pwdText.getText().toString());
        }

        Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
        intent.putExtra("LOGIN_NAME", nameText.getText().toString());
        startActivity(intent);
    }

    /**
     * 保存用户登录密码
     *
     * @param name
     * @param pwd
     */
    private void saveInfo(String name, String pwd) {

        SQLiteOpenHelper sqLiteOpenHelper = new MyDatabaseHelper(LoginActivity.this, "xiaohui_chat", null, 1);
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put("name", name);
        value.put("pwd", pwd);

        // 参数1：要操作的表名称
        // 参数2：SQl不允许一个空列，若ContentValues是空，那么这一列被明确的指明为NULL值
        // 参数3：ContentValues对象
        sqLiteDatabase.insert("user", null, value);
        sqLiteDatabase.close();
    }


    class SendLogin implements ProcessInterface {

        private String name;
        private String pwd;

        public SendLogin(String name, String pwd) {
            this.name = name;
            this.pwd = pwd;
        }

        @Override
        public Object call() {
            Map map = new HashMap(2);
            map.put("name", name);
            map.put("pwd", pwd);
            HttpRequest request = new HttpRequest("http://192.168.1.113:8080/login", "POST").form(map);
            ResponseDao responseDao = JSONObject.parseObject(request.body(), ResponseDao.class);
            return responseDao.getSuccess();
        }
    }

}
