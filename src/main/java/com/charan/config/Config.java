package com.charan.config;

public class Config {
    private volatile static Config config;

    private Config() {}

    public static Config getConfig() {
        if (config == null) {
            synchronized (Config.class) {
                if (config == null) {
                    config = new Config();
                }
            }
        }
        return config;
    }
}
