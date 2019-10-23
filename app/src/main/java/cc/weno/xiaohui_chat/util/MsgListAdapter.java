package cc.weno.xiaohui_chat.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import cc.weno.xiaohui_chat.R;
import cc.weno.xiaohui_chat.dao.MsgDao;
import cc.weno.xiaohui_chat.page.ChatActivity;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class MsgListAdapter extends RecyclerView.Adapter<MsgListAdapter.ViewHolder> {


    private Context context;
    private LayoutInflater layoutInflater;
    public static List<MsgDao> msgDaoList = new ArrayList<>();
    private MsgListAdapter adapter = this;
    private ViewHolder holder;

    private Handler handler;
    /**
     * 自己的用户名
     */
    private String selfName;

    public MsgListAdapter(Context context, String name) {
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
        this.selfName = name;
        updateMsg();
    }

    /**
     * 更新数据
     */
    private void updateMsg() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                adapter.notifyDataSetChanged();
            }
        };
        new UpdateThread().start();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.chat, parent, false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MsgDao msgDao = msgDaoList.get(position);
        Log.d(TAG, position+"位置的数据"+msgDao.getMsgContent());

        if (msgDao == null) {
            return;
        }

        // BU是自己发送的消息
        if (!selfName.equals(msgDao.getName())) {

            holder.left.setVisibility(View.GONE);

            holder.rightTime.setText(msgDao.getDataTime());
            holder.rightName.setText(msgDao.getName());
            holder.rightMsg.setText(msgDao.getMsgContent());
        } else {
            holder.right.setVisibility(View.GONE);
            holder.leftTime.setText(msgDao.getDataTime());
            holder.leftName.setText(msgDao.getName());
            holder.leftMsg.setText(msgDao.getMsgContent());
        }

    }

    @Override
    public int getItemCount() {
        return msgDaoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView leftName;
        TextView leftTime;
        TextView leftMsg;
        LinearLayout left;

        TextView rightName;
        TextView rightTime;
        TextView rightMsg;
        LinearLayout right;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftName = itemView.findViewById(R.id.left_name);
            leftTime = itemView.findViewById(R.id.left_time);
            leftMsg = itemView.findViewById(R.id.left_msg);
            left = itemView.findViewById(R.id.left);


            rightName = itemView.findViewById(R.id.right_name);
            rightTime = itemView.findViewById(R.id.right_time);
            rightMsg = itemView.findViewById(R.id.right_msg);
            right = itemView.findViewById(R.id.right);
        }
    }


    class UpdateThread extends Thread {
        @Override
        public void run() {
            while (true) {

                HttpRequest request = HttpRequest.get("http://192.168.1.113:8080/getMsg");
                JSONObject jsonObject = JSONObject.parseObject(request.body());
                JSONArray list = jsonObject.getJSONArray("data");
                if (list.size() != msgDaoList.size()) {
                    if (!list.isEmpty()) {
                        for (int i = msgDaoList.size(); i < list.size(); i++) {
                            JSONObject json = list.getJSONObject(i);
                            MsgDao msgDao = new MsgDao();
                            String name = json.getString("name");
                            String dataTime = json.getString("time");
                            String msgContent = json.getString("msg");
                            msgDao.setName(name);
                            msgDao.setDataTime(dataTime);
                            msgDao.setMsgContent(msgContent);
                            msgDaoList.add(msgDao);
                            // 更新数据
                            if(holder != null) {
                                handler.sendEmptyMessage(i);
                            }
                        }
                    }
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
