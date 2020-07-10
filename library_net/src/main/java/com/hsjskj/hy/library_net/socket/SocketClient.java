package com.hsjskj.hy.library_net.socket;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * @author Wen xiao
 * @time 2020/4/3
 */
public class SocketClient {
    private static final String TAG = "socket";
    private Socket mSocket;
    private SocketHandler mSocketHandler;
    private String mStream;

    public SocketClient(String url, SocketMessageListener listener) {
        Log.e(TAG, "socket url--->" + url);
        if (!TextUtils.isEmpty(url)) {
            try {
                IO.Options option = new IO.Options();
                option.forceNew = true;
                option.reconnection = true;
                option.reconnectionDelay = 2000;
                mSocket = IO.socket(url, option);
                mSocket.on(Socket.EVENT_CONNECT, mConnectListener);//连接成功
                mSocket.on(Socket.EVENT_DISCONNECT, mDisConnectListener);//断开连接
                mSocket.on(Socket.EVENT_CONNECT_ERROR, mErrorListener);//连接错误
                mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, mTimeOutListener);//连接超时
                mSocket.on(Socket.EVENT_RECONNECT, mReConnectListener);//重连
                mSocket.on(Constants.SOCKET_CONN, onConn);//连接socket消息
                mSocket.on(Constants.SOCKET_BROADCAST, onBroadcast);//接收服务器广播的具体业务逻辑相关的消息
                mSocketHandler = new SocketHandler(listener);
            } catch (Exception e) {
                Log.e(TAG, "socket url 异常--->" + e.getMessage());
            }
        }
    }

    public void connect( String stream) {
        mStream = stream;
        if (mSocket != null) {
            mSocket.connect();
        }
    }

    public void sendEmit(String str){
        if (mSocket != null) {
            mSocket.emit("conn",str);
        }
    }

    public void disConnect() {
        if (mSocket != null) {
            mSocket.close();
            mSocket.off();
        }
        if (mSocketHandler != null) {
            mSocketHandler.release();
        }
        mSocketHandler = null;
        mStream = null;
    }

    /**
     * 向服务发送连接消息
     */
    private void conn() {
        org.json.JSONObject data = new org.json.JSONObject();
        try {
            data.put("date", "conn");
            data.put("stream", mStream);
            mSocket.emit("conn", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Emitter.Listener mConnectListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "--onConnect-->" + args);
            conn();
        }
    };

    private Emitter.Listener mReConnectListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "--reConnect-->" + args);
            //conn();
        }
    };

    private Emitter.Listener mDisConnectListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "--onDisconnect-->" + args);
            if (mSocketHandler != null) {
                mSocketHandler.sendEmptyMessage(Constants.SOCKET_WHAT_DISCONN);
            }
        }
    };
    private Emitter.Listener mErrorListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            for(Object obj:args){
                Log.e(TAG, "--onConnectError-->" + obj);
            }
        }
    };

    private Emitter.Listener mTimeOutListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "--onConnectTimeOut-->" + args);
        }
    };


    private Emitter.Listener onConn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (mSocketHandler != null) {
                try {
                    String s = ((JSONArray) args[0]).getString(0);
                    Log.e(TAG, "--onConn-->" + s);
                    Message msg = Message.obtain();
                    msg.what = Constants.SOCKET_WHAT_CONN;
                    msg.obj = s.equals("ok");
                    mSocketHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Emitter.Listener onBroadcast = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (mSocketHandler != null) {
                try {
                    JSONArray array = (JSONArray) args[0];
                    for (int i = 0; i < array.length(); i++) {
                        Message msg = Message.obtain();
                        msg.what = Constants.SOCKET_WHAT_BROADCAST;
                        msg.obj = array.getString(i);
                        if (mSocketHandler != null) {
                            mSocketHandler.sendMessage(msg);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    };

    private static class SocketHandler extends Handler {

        private SocketMessageListener mListener;

        public SocketHandler(SocketMessageListener listener) {
            mListener = new WeakReference<>(listener).get();
        }


        @Override
        public void handleMessage(Message msg) {
            if (mListener == null) {
                return;
            }
            switch (msg.what) {
                case Constants.SOCKET_WHAT_CONN:
                    mListener.onConnect((Boolean) msg.obj);
                    break;
                case Constants.SOCKET_WHAT_BROADCAST:
                    processBroadcast((String) msg.obj);
                    break;
                case Constants.SOCKET_WHAT_DISCONN:
                    mListener.onDisConnect();
                    break;
            }
        }

        private void processBroadcast(String socketMsg) {
            Log.e(TAG,socketMsg);
        }

        public void release() {
            mListener = null;
        }
    }
}
