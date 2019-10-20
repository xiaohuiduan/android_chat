package cc.weno.xiaohui_chat.page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cc.weno.xiaohui_chat.R;
import cc.weno.xiaohui_chat.mapper.MyDatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private TextView registerText;

    private EditText nameText;
    private EditText pwdText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    /**
     * 从数据库中间获取账号密码
     */
    private void intData(String name,String pwd) {

        SQLiteOpenHelper sqLiteOpenHelper = new MyDatabaseHelper(this,"xiaohui_chat",null,1);
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("user",new String[]{"id"},"name=? and pwd = ?",new String[]{name,pwd},null,null,null);
        // 通过游标的方法可迭代查询结果
        if(cursor.moveToFirst()) {
            Toast.makeText(this,"登录成功",Toast.LENGTH_LONG).show();
        }
        // 如果没有数据的话
        else{
            Toast.makeText(this,"用户尚未注册",Toast.LENGTH_LONG).show();
        }
        sqLiteDatabase.close();
    }

    private void init(){

        nameText  = findViewById(R.id.login_name);
        pwdText = findViewById(R.id.login_pwd);

        registerText = findViewById(R.id.register_text);
        loginButton = findViewById(R.id.login_button);

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
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

        Log.d("登录", "login: "+pwd.length() );
        if (name.length() ==0 || pwd.length() == 0){
            Toast.makeText(this,"请输入账号或者密码",Toast.LENGTH_SHORT).show();
        }else{
            intData(name,pwd);
        }
    }
}
