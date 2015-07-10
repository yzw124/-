package com.tinywindow.adfkoff.common;

import android.content.Context;
import android.os.Environment;

import com.stericson.RootTools.RootTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Zhongwen.Ye
 * Date : 2015/7/10
 * Time : 6:06.
 */
public class Util {

    private static final String TAG = Util.class.getSimpleName();

    /**
     *  从输入流得到字符串
     *  @param  inputStream  输入流
     */

    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "gbk");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        if (inputStreamReader != null) {
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder("");
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sb.toString();
        }
        return "";
    }

    /**
     *  从assets目录中复制整个文件夹内容
     *  @param  context  Context 使用CopyFiles类的Activity
     *  @param  oldPath  String  原文件路径  如：/aa
     *  @param  newPath  String  复制后路径  如：xx:/bb/cc
     */
    static public void copyFilesFassets(Context context,String oldPath,String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFassets(context,oldPath + "/" + fileName,newPath+"/"+fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount=0;
                while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * read hosts file
     * */
    static public void readHosts(){
        File file = new File("/system/etc/hosts");
        String hosts;
        try {
            InputStream inputStream = new FileInputStream(file);
            hosts = Util.getString(inputStream);
            LogUtil.LOG_D(TAG, "hosts :" + hosts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *  appends
     *
     * */
    static public void appendHosts(){

    }

    static public void copyHosts(Context context) throws IOException {
        Util.copyFilesFassets(context, "hosts", Environment.getExternalStorageDirectory().getPath() + "AdFkoff/");
        boolean result = RootTools.copyFile(Environment.getExternalStorageDirectory().getPath() + "AdFkoff/hosts", "/system/etc/hosts", true, true);
        LogUtil.LOG_D(TAG, "copyHosts result :" + String.valueOf(result));
    }
}
