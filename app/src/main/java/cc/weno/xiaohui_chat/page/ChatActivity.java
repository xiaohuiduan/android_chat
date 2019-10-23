package cc.weno.xiaohui_chat.page;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import cc.weno.xiaohui_chat.R;
import cc.weno.xiaohui_chat.dao.MsgDao;
import cc.weno.xiaohui_chat.dao.ResponseDao;
import cc.weno.xiaohui_chat.net.AsyncRequest;
import cc.weno.xiaohui_chat.net.ProcessInterface;
import cc.weno.xiaohui_chat.util.MsgListAdapter;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView msgList;
    private EditText msgContent;
    private Button msgSendButton;
    private LinearLayout linearChatInput;
    private MsgListAdapter adapter;

    /**
     * 进行登录的用户名
     */
    private String loginName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        loginName = getIntent().getStringExtra("LOGIN_NAME");
        init();
    }

    private void init() {
        msgList = findViewById(R.id.msg_list);
        msgContent = findViewById(R.id.msg_content);
        msgSendButton = findViewById(R.id.msg_send_button);

        msgSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewClicked();
            }
        });

        linearChatInput = findViewById(R.id.linear_chat_input);
        msgList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MsgListAdapter(this,loginName);

        msgList.setAdapter(adapter);

    }

    /**
     * 发送按钮点击
     */
    public void onViewClicked() {
        String msg = msgContent.getText().toString().trim();
        sendMsg(msg);
        msgContent.setText("");
    }

    private void sendMsg(String msg) {
        if (msg.length() == 0) {
            Toast.makeText(this, "消息不能为空", Toast.LENGTH_SHORT).show();
        } else {
            // 异步发送数据
            AsyncTask asyncTask = new AsyncRequest().execute(new SendMsgToNet(msg,loginName));
            try {
                boolean success = (boolean) asyncTask.get();
                if (success) {
                    MsgDao  msgDao = new MsgDao();
                    msgDao.setName(loginName);
                    msgDao.setMsgContent(msg);
                    MsgListAdapter.msgDaoList.add(msgDao);
                    adapter.notifyItemInserted(MsgListAdapter.msgDaoList.size()-1);

                }else {
                    Toast.makeText(this, "消息发送失败", Toast.LENGTH_SHORT).show();
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendMsgToNet implements ProcessInterface {
        String msg;
        String name;

        public SendMsgToNet(String msg, String name) {
            this.msg = msg;
            this.name = name;
        }

        @Override
        public Object call() {
            Map map = new HashMap(2);
            map.put("name", name);
            map.put("msg", msg);
            HttpRequest request = new HttpRequest("http://192.168.1.113:8080/sendMsg", "POST").form(map);

            ResponseDao responseDao = JSONObject.parseObject(request.body(), ResponseDao.class);
            return responseDao.getSuccess();
        }
    }
}
