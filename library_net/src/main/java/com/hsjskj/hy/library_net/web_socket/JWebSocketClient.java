package com.hsjskj.hy.library_net.web_socket;

import com.hsjskj.hy.library_net.net.XLog;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * @author Wen xiao
 * @time 2020/5/19
 */
public class JWebSocketClient extends WebSocketClient {
    public JWebSocketClient(URI serverUri) {
        super(serverUri, new Draft_6455());
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        XLog.e("JWebSocketClient", "onOpen()");
    }

    @Override
    public void onMessage(String message) {
        XLog.e("JWebSocketClient", "onMessage()");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        XLog.e("JWebSocketClient", "onClose()");
    }

    @Override
    public void onError(Exception ex) {
        XLog.e("JWebSocketClient", "onError()");
    }
}