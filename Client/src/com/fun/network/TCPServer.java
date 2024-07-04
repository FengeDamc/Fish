package com.fun.network;

import com.fun.client.FunGhostClient;
import com.fun.client.config.ConfigModule;
import com.fun.inject.*;
import com.fun.utils.jna.WindowEnumerator;
import com.fun.gui.FishFrame;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static Thread thread;
    public static void startServer(int portIn){
         thread= new ServerThread(portIn);
         //System.out.println(thread.getContextClassLoader().getClass().getName());
         thread.start();
    }
    public static boolean isNetworkThread(){
        return Thread.currentThread() instanceof ServerThread;
    }
    public static int getTargetPort(){
        return Agent.isAgent? Main.SERVERPORT:Agent.SERVERPORT;
    }
    public static String review(String mess){
        String[] split=mess.split(" ");
        switch (split[0]){
            case "mcver":
                Agent.minecraftVersion= getVersion();
                 return split[0]+String.format(" %d",Agent.minecraftVersion.ordinal());
        }
        return mess;
    }
    public static class ServerThread extends Thread{
        public int portIn;
        public ServerThread(int portIn) {
            super();
            this.portIn=portIn;
            System.out.println("isAgent: "+Agent.isAgent);
        }

        @Override
        public void run() {
            super.run();
            try (ServerSocket serverSocket = new ServerSocket(portIn)) {
                System.out.println("服务器启动，监听端口：" + portIn);

                while (true) {
                    // 等待客户端连接
                    try {
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("客户端已连接：" + clientSocket);

                        // 创建输入输出流
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                        // 读取客户端发送的消息，并回显
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            System.out.println("收到消息：" + inputLine);
                            String r=review(inputLine);
                            out.println(r); // 回显消息
                            System.out.println("回显消息：" + r);

                            receive(inputLine);
                        }

                        // 关闭连接
                        clientSocket.close();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static MinecraftVersion getVersion(){
        for(String s: WindowEnumerator.getWindows(Integer.parseInt(Main.pid))){
            for(MinecraftVersion v: MinecraftVersion.values()){
                if(s.contains(v.getGeneralVer())){
                    System.out.println(s);
                    return v;
                }
                for(String cn:v.getClientNames()){
                    if(s.contains(cn)){
                        System.out.println(s);
                        return v;
                    }
                }
            }
        }
        return MinecraftVersion.VER_189;
    }
    public static void receive(String info){
            String[] split = info.split(" ");


            try{
                switch (split[0]) {
                    case "setstring":
                        FunGhostClient.settingsManager.getSettings().get(Integer.parseInt(split[1])).setValString(split[2]);
                        break;
                    case "setbool":
                        FunGhostClient.settingsManager.getSettings().get(Integer.parseInt(split[1])).setValBoolean(Boolean.parseBoolean(split[2]));
                        break;
                    case "setdouble":
                        FunGhostClient.settingsManager.getSettings().get(Integer.parseInt(split[1])).setValDouble(Double.parseDouble(split[2]));
                        break;
                    case "setrun":
                        FunGhostClient.moduleManager.mods.get(Integer.parseInt(split[1])).setRunning(Boolean.parseBoolean(split[2]));
                        break;
                    case "setkey":
                        FunGhostClient.moduleManager.mods.get(Integer.parseInt(split[1])).setKey(Integer.parseInt(split[2]));
                        break;
                    case "destroy":
                        try {
                            ClassLoader.getSystemClassLoader().loadClass("com.fun.inject.InjectUtils").getDeclaredMethod("destroyClient").invoke(null);
                        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                                 InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "mcpath":
                        try {
                            Main.mcPath = split[1];
                            Main.started = true;
                            File injection=new File(new File(Main.path),"/injections/"+Agent.minecraftVersion.injection);
                            System.out.println("injection: "+injection.getAbsolutePath());
                            InjectorUtils.addToSystemClassLoaderSearch(injection.getAbsolutePath());
                            FunGhostClient.init();

                            ConfigModule.loadConfig();

                            FishFrame.init0();

                            System.out.println("fishgc Started");
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;

                }
            }
            catch (Exception e){

            }



    }

}
