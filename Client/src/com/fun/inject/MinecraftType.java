package com.fun.inject;

public enum MinecraftType {
    VANILLA("vanilla"),
    FORGE("forge"),
    MCP("mcp"),
    NONE("none");

    private final String type;

    MinecraftType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
