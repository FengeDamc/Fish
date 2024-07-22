package com.fun.inject;

public enum MinecraftVersion {
    VER_1710("1.7.10","1.7.10.jar"),
    VER_189("1.8.9","1.8.9.jar","虎牙","欧迈嘎"),
    VER_1122("1.12.2","1.12.2.jar","花雨庭"),
    VER_1165("1.16.5","1.16.5.jar"),
    VER_1181("1.18.1","1.18.1.jar","布吉岛");



    private String ver;
    public String injection;
    private String[] clientNames;

    MinecraftVersion(String ver,String injection,String... clientNames) {
        this.injection = injection;
        this.ver = ver;
        this.clientNames=clientNames;
    }

    public String getVer() {
        return ver;
    }
    public String getGeneralVer() {
        String[] split=getVer().split("\\.");
        return split[0]+"."+split[1];
    }

    public String[] getClientNames() {
        return clientNames;
    }

    public String getInjection() {
        return "/injections/"+injection;
    }
}
