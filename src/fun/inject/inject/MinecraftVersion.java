package fun.inject.inject;

public enum MinecraftVersion {
    VER_1710("1.7.10"),
    VER_189("1.8.9"),
    VER_1122("1.12.2"),
    VER_1165("1.16.5");



    private final String ver;

    MinecraftVersion(String ver) {
        this.ver = ver;
    }

    public String getVer() {
        return ver;
    }
    public String getGeneralVer() {
        String[] split=getVer().split("\\.");
        return split[0]+"."+split[1];
    }

}
