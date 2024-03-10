package fun.inject.inject;

public enum MinecraftType {
    VANILLA("vanilla"),
    FORGE("forge");

    private final String type;

    MinecraftType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
