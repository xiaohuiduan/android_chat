package cc.weno.xiaohui_chat.net;


import android.os.AsyncTask;

/**
 * 安卓禁止在主线程中间调用网络
 * @author xiaohui
 */
public class AsyncRequest extends AsyncTask<ProcessInterface,Integer,Object> {


    @Override
    protected Object doInBackground(ProcessInterface... processInterfaces) {
        return processInterfaces[0].call();
    }
}
