package fun.network;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import fun.client.FunGhostClient;
import fun.client.config.ConfigModule;
import fun.gui.FishFrame;
import fun.inject.Agent;
import fun.inject.Main;
import fun.inject.inject.InjectUtils;
import fun.inject.inject.MinecraftVersion;

import java.io.BufferedReader;
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
    public static String review(String mess){
        String[] split=mess.split(" ");
        switch (split[0]){
            case "mcver":
                 return split[0]+String.format(" %d",getVersion().ordinal());
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
        for (VirtualMachineDescriptor virtualMachineDescriptor : VirtualMachine.list()) {
            System.out.println(virtualMachineDescriptor.displayName());
            for (MinecraftVersion mcVer : MinecraftVersion.values()) {
                if (virtualMachineDescriptor.displayName().contains(mcVer.getVer())) {
                    return mcVer;
                }
            }
        }
        return MinecraftVersion.VER_189;
    }
    public static void receive(String info){
            String[] split = info.split(" ");

            if(Agent.isAgent){
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
                        FunGhostClient.moduleManager.mods.get(Integer.parseInt(split[1])).running= Boolean.parseBoolean(split[2]);
                        break;
                    case "destroy":
                        try {
                            ClassLoader.getSystemClassLoader().loadClass("fun.inject.inject.InjectUtils").getDeclaredMethod("destroyClient").invoke(null);
                        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                                 InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;

                }
            }
            else{
                switch (split[0]) {
                    case "mcpath":
                        try {
                            Main.mcPath = split[1];
                            Main.started = true;

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


    }

}
