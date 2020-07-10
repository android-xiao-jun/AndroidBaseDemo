package com.hsjskj.hy.library_net.socket;

/**
 * @author Wen xiao
 * @time 2020/4/3
 */
public interface SocketMessageListener {
    /**
     *   连接成功socket后调用
     */
    void onConnect(boolean successConn);

    /**
     *   自己的socket断开
     */
    void onDisConnect();
}
