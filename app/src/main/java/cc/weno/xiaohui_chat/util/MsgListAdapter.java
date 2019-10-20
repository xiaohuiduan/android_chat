package cc.weno.xiaohui_chat.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cc.weno.xiaohui_chat.R;
import cc.weno.xiaohui_chat.dao.MsgDao;

public class MsgListAdapter extends RecyclerView.Adapter<MsgListAdapter.ViewHolder> {


    private Context context;
    private LayoutInflater layoutInflater;
    private List<MsgDao> msgDaoList=new ArrayList<>();
    /**
     * 自己的用户名
     */
    private String userName;

    public MsgListAdapter(Context context,String name) {
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
        this.userName = name;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.chat,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MsgDao msgDao = msgDaoList.get(position);
        if (msgDao == null){
            return;
        }
        // 是自己发送的消息
        if (userName.equals(msgDao.getName())){
            holder.rightMsg.setVisibility(View.GONE);
            holder.rightName.setVisibility(View.GONE);
            holder.rightTime.setVisibility(View.GONE);

            holder.leftTime.setText(msgDao.getDataTime());
            holder.leftName.setText(msgDao.getName());
            holder.leftMsg.setText(msgDao.getMsgContent());
        }
        // 不是是自己发送的消息
        else {
            holder.leftMsg.setVisibility(View.GONE);
            holder.leftName.setVisibility(View.GONE);
            holder.leftTime.setVisibility(View.GONE);

            holder.rightTime.setText(msgDao.getDataTime());
            holder.rightName.setText(msgDao.getName());
            holder.rightMsg.setText(msgDao.getMsgContent());
        }

    }

    @Override
    public int getItemCount() {
        return msgDaoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView leftName;
        TextView leftTime;
        TextView leftMsg;

        TextView rightName;
        TextView rightTime;
        TextView rightMsg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftName = itemView.findViewById(R.id.left_name);
            leftTime = itemView.findViewById(R.id.left_time);
            leftMsg = itemView.findViewById(R.id.left_msg);
            rightName = itemView.findViewById(R.id.right_name);
            rightTime = itemView.findViewById(R.id.right_time);
            rightMsg = itemView.findViewById(R.id.right_msg);
        }
    }
}
