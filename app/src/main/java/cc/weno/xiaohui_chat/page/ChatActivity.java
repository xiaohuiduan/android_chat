package cc.weno.xiaohui_chat.page;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.weno.xiaohui_chat.R;

public class ChatActivity extends AppCompatActivity {


    @BindView(R.id.msg_list)
    private RecyclerView msgList;
    @BindView(R.id.msg_content)
    private EditText msgContent;
    @BindView(R.id.msg_send_button)
    private Button msgSendButton;
    @BindView(R.id.linear_chat_input)
    private LinearLayout linearChatInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        init();
    }

    private void init() {


    }

    @OnClick(R.id.msg_send_button)
    public void onViewClicked() {

    }
}
