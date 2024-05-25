package fun.inject.inject;

public enum MinecraftVersion {
    VER_1710("1.7"),
    VER_189("1.8"),
    VER_1122("1.12"),
    VER_1165("1.16");



    private final String ver;

    MinecraftVersion(String ver) {
        this.ver = ver;
    }

    public String getVer() {
        return ver;
    }

}
