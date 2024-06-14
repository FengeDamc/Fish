package fun.network;

import fun.inject.Agent;
import fun.inject.inject.MinecraftVersion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient {
    public static void send(int port,String info){
        String host = "localhost"; // 服务器地址

        try (Socket socket = new Socket(host, port)) {
            System.out.println("已连接到服务器：" + host + "，端口：" + port);

            // 创建输入输出流
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            // 接收用户输入，发送给服务器，并接收回显


            out.println(info);
            String r=in.readLine();
            review(r);
            System.out.println("接收到服务器的回显：" + r);
            socket.close();

        } catch (UnknownHostException e) {
            System.err.println("无法识别的主机或客户端还未初始化完成: " + host);
        } catch (Exception e) {
        }
    }
    public static void review(String mess){
        String[] split=mess.split(" ");
        switch (split[0]){
            case "mcver":
                Agent.minecraftVersion= MinecraftVersion.values()[Integer.parseInt(split[1])];
                break;
        }
    }

}
