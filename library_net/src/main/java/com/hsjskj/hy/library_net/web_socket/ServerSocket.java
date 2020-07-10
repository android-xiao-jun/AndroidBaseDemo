package com.hsjskj.hy.library_net.web_socket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * @author Wen xiao
 * @time 2020/5/19
 */
public class ServerSocket extends WebSocketServer {

    private ServerManager _serverManager;

    public ServerSocket(ServerManager serverManager,int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
        _serverManager=serverManager;
    }


    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Some one Connected...");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        _serverManager.UserLeave(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("OnMessage:"+message.toString());
        _serverManager.UserLogin("1111", conn);
        _serverManager.SendMessageToAll("服务端返回给客户端socket数据_"+message.toString());
//        if (message.equals("1")) {
//            _serverManager.SendMessageToUser(conn, "What?");
//        }
//
//        String[] result=message.split(":");
//        if (result.length==2) {
//            if (result[0].equals("user")) {
//                _serverManager.UserLogin(result[1], conn);
//            }
//        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("Socket Exception:"+ex.toString());
    }

    @Override
    public void onStart() {

    }
}
