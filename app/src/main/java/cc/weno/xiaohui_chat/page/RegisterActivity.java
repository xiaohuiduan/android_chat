package cc.weno.xiaohui_chat.page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import cc.weno.xiaohui_chat.R;
import cc.weno.xiaohui_chat.dao.ResponseDao;
import cc.weno.xiaohui_chat.mapper.MyDatabaseHelper;
import cc.weno.xiaohui_chat.net.AsyncRequest;
import cc.weno.xiaohui_chat.net.ProcessInterface;

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
            boolean result = mapperService(name,pwd);
            if (result){
                Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"注册失败",Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * 进行注册
     * @param name 账号名
     * @param pwd 密码
     * @return 返回插入后的id
     */
    private boolean mapperService(String name, String pwd) {

        AsyncTask asyncTask = new AsyncRequest().execute(new RegisterActivity.SendRegister(name, pwd));
        try {
            boolean success = (boolean) asyncTask.get();
            return success;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }



    class SendRegister implements ProcessInterface {

        private String name;
        private String pwd;

        public SendRegister(String name, String pwd) {
            this.name = name;
            this.pwd = pwd;
        }

        @Override
        public Object call() {
            Map map = new HashMap(2);
            map.put("name", name);
            map.put("pwd", pwd);
            HttpRequest request = new HttpRequest("http://192.168.1.113:8080/register", "POST").form(map);
            ResponseDao responseDao = JSONObject.parseObject(request.body(), ResponseDao.class);
            return responseDao.getSuccess();
        }
    }

}
