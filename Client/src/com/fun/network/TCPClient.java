package com.fun.network;

import java.io.*;
import java.net.Socket;
public class TCPClient {
    public static void send(int port,IPacket info){
        String host = "localhost"; // 服务器地址
        System.out.println("send"+info);
        try (Socket socket = new Socket(host, port)) {
            System.out.println(info);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            oos.writeObject(info);
            Object r=ois.readObject();
            if(r instanceof IReviewable) review((IReviewable) r);
            System.out.println("接收到服务器的回显：" + r);

        } catch (Exception e) {
        }
    }
    public static void review(IReviewable mess){
        mess.review();
    }


}
