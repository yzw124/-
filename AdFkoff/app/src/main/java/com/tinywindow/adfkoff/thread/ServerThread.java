package com.tinywindow.adfkoff.thread;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tinywindow.adfkoff.constant.MessageConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Zhongwen.Ye
 * Date : 2015/7/10
 * Time : 3:51.
 */
public class ServerThread extends Thread {
    private Handler mHandler;
    private Context mContext;

    public ServerThread(Context context, Handler handler) {
        super();
        mHandler = handler;
        mContext = context;
    }

    @Override
    public void run() {
        super.run();
        getFromServer();
//        getFromAssets();
    }

    private void getFromAssets() throws IOException, JSONException {
        JSONArray array = new JSONArray();
        AssetManager assetManager = mContext.getAssets();
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = assetManager.open("hosts.json");
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bf.readLine()) != null) {
            stringBuilder.append(line);
        }
        JSONObject jsonObject = new JSONObject(stringBuilder.toString());
        array = jsonObject.getJSONArray("hosts");

        Message msg = new Message();
        msg.what = MessageConstant.MSG_HOSTSDONE;
        msg.obj = array;
        mHandler.sendMessage(msg);
    }

    private void getFromServer(){
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, "http://www.baidu.com",
                new RequestCallBack<Object>() {
                    /*
                    * get hosts.json file from server
                    * the response format is json
                    * example :
                    * {
                    *  hosts :[{192.168.1.1},{192.168.1.2}]
                    * }
                    * */
                    @Override
                    public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                        JSONObject jsonObject;
                        JSONArray array;
                        try {
                            jsonObject = new JSONObject(String.valueOf(objectResponseInfo.result));
                            array = jsonObject.optJSONArray("hosts");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            msg.what = MessageConstant.MSG_HOSTSERROR;
                            mHandler.sendMessage(msg);
                            return;
                        }
                        Message msg = new Message();
                        msg.what = MessageConstant.MSG_HOSTSDONE;
                        msg.obj = array;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                });
    }

}
