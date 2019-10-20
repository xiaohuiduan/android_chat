package cc.weno.xiaohui_chat.page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cc.weno.xiaohui_chat.R;
import cc.weno.xiaohui_chat.mapper.MyDatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEdit;
    private EditText pwdEdit;
    private EditText rePwdEdit;
    private Button  registerButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.init();
    }

    private void init(){
        nameEdit = findViewById(R.id.register_name);
        pwdEdit = findViewById(R.id.register_pwd);
        rePwdEdit = findViewById(R.id.re_pwd);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNamePwd();
            }
        });
    }

    /**
     * 正确的获取用户名以及密码
     */
    private void getNamePwd(){
        String name = nameEdit.getText().toString();
        String pwd = pwdEdit.getText().toString();
        String rePwd = rePwdEdit.getText().toString();
        if (name.length() == 0){
            Toast.makeText(this,"请输入账号",Toast.LENGTH_SHORT).show();
        }else if(pwd.length() ==0 || rePwd.length()==0){
            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
        }else if(!pwd.equals(rePwd)){
            Toast.makeText(this,"两次密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
        }else{
            long result = mapperService(name,pwd);
            if (result != -1){
                Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"注册失败",Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * 将注册的信息放入数据库
     * @param name 账号名
     * @param pwd 密码
     * @return 返回插入后的id
     */
    private long mapperService(String name, String pwd){
        SQLiteOpenHelper sqLiteOpenHelper = new MyDatabaseHelper(RegisterActivity.this,"xiaohui_chat",null,1);
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put("name",name);
        value.put("pwd",pwd);

        // 参数1：要操作的表名称
        // 参数2：SQl不允许一个空列，若ContentValues是空，那么这一列被明确的指明为NULL值
        // 参数3：ContentValues对象
        long re = sqLiteDatabase.insert("user",null,value);

        sqLiteDatabase.close();

        return re;
    }

}
