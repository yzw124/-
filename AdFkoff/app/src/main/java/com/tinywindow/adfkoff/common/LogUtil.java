package com.tinywindow.adfkoff.common;

import android.text.Html;
import android.widget.ScrollView;
import android.widget.TextView;

public class LogUtil {

    private static boolean mIsLogAllowed = true;

    private static TextView mLogView;

    private static ScrollView mScrollView;

    public static void setLogAllowed(boolean isAllowed) {
        mIsLogAllowed = isAllowed;
        if (!isAllowed) {
            android.util.Log.w("LOG", getMsg("log closed..."));
        }
    }

    public static boolean isLogAllow() {
        return mIsLogAllowed;
    }

    public static void setLogOutput(TextView logView, ScrollView scrollView) {
        mLogView = logView;
        mScrollView = scrollView;
    }

    public static void LOG_V(Object tagObject, Object... msgs) {
        if (mIsLogAllowed) {
            String msg = getMsg(msgs);
            android.util.Log.v(tagObject.getClass().getSimpleName(), msg);
            if (mLogView != null) {
                mLogView.post(new TextViewDraw(msg, "#000000"));
            }
        }
    }

    public static void LOG_D(Object tagObject, Object... msgs) {
        if (mIsLogAllowed) {
            String msg = getMsg(msgs);
            android.util.Log.d(tagObject.getClass().getSimpleName(), msg);
            if (mLogView != null) {
                mLogView.post(new TextViewDraw(msg, "#0000ff"));
            }
        }
    }

    public static void LOG_E(Object tagObject, Object... msgs) {
        if (mIsLogAllowed) {
            String msg = getMsg(msgs);
            android.util.Log.e(tagObject.getClass().getSimpleName(), msg);
            if (mLogView != null) {
                mLogView.post(new TextViewDraw(msg, "#ff0000"));
            }
        }
    }

    public static void LOG_E(Object tagObject, Throwable e, Object... msgs) {
        if (mIsLogAllowed) {
            String msg = getMsg(msgs);
            android.util.Log.e(tagObject.getClass().getSimpleName(), msg, e);
            if (mLogView != null) {
                mLogView.post(new TextViewDraw(msg, "#ff0000"));
            }
        }
    }

    public static void LOG_I(Object tagObject, Object... msgs) {
        if (mIsLogAllowed) {
            String msg = getMsg(msgs);
            android.util.Log.i(tagObject.getClass().getSimpleName(), msg);
            if (mLogView != null) {
                mLogView.post(new TextViewDraw(msg, "#00ff00"));
            }
        }
    }

    public static void LOG_W(Object tagObject, Object... msgs) {
        if (mIsLogAllowed) {
            String msg = getMsg(msgs);
            android.util.Log.w(tagObject.getClass().getSimpleName(), msg);
            if (mLogView != null) {
                mLogView.post(new TextViewDraw(msg, "#ffff00"));
            }
        }
    }

    private static class TextViewDraw implements Runnable {

        private String msg;

        private String color;

        TextViewDraw(String msg, String color) {
            this.msg = msg;
            this.color = color;
        }

        @Override
        public void run() {
            if (mLogView != null) {
                String str = "<font color=" + this.color + ">" + this.msg
                        + "</font><br>";
                mLogView.append(Html.fromHtml(str));
                if (mScrollView != null) {
                    mScrollView.scrollTo(0, mLogView.getHeight());
                }
            }
        }
    }

    private static String getMsg(Object... msgs) {
        StringBuilder builder = new StringBuilder();
        for (Object msg : msgs) {
            builder.append(msg);
        }
        return builder.toString();
    }
}
