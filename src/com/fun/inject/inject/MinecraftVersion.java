package com.fun.inject.inject;

public enum MinecraftVersion {
    VER_1710("1.7.10"),
    VER_189("1.8.9"),
    VER_1122("1.12.2","花雨庭"),
    VER_1165("1.16.5");



    private String ver;
    private String[] clientNames;

    MinecraftVersion(String ver,String... clientNames) {
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
}
