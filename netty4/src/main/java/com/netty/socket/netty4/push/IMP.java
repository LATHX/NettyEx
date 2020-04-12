package com.netty.socket.netty4.push;

public enum IMP {
    SYSTEM("SYSTEM"),
    LOGIN("LOGIN"),
    LOGOUT("LOGOUT"),
    CHAT("CHAT"),
    FLOWER("FLOWER");
    private String name;
    IMP(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public static boolean isIMP(String content){
        return content.matches("^\\[(SYSTEM|LOGIN|LOGIN|CHAT)\\]");
    }
    public void setName(String name) {
        this.name = name;
    }
}
