package chihalu.totalitems.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TotalItemsServerConfig {
    
    private static final Path CONFIG_DIR = Paths.get("config");
    private static final Path CONFIG_FILE = CONFIG_DIR.resolve("totalitems.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    private static ConfigData configData = new ConfigData();
    
    static {
        loadConfig();
    }
    
    public static class ConfigData {
        public boolean enabled = true;
        public List<String> trackedItems = new ArrayList<>();
        
        public ConfigData() {
            // デフォルト：追跡対象なし
        }
    }
    
    /**
     * 設定を読み込む
     */
    public static void loadConfig() {
        try {
            if (Files.exists(CONFIG_FILE)) {
                try (FileReader reader = new FileReader(CONFIG_FILE.toFile())) {
                    configData = GSON.fromJson(reader, ConfigData.class);
                    if (configData == null) {
                        configData = new ConfigData();
                    }
                }
            } else {
                saveConfig();
            }
        } catch (IOException e) {
            System.err.println("Failed to load TotalItems config: " + e.getMessage());
            configData = new ConfigData();
        }
    }
    
    /**
     * 設定を保存
     */
    public static void saveConfig() {
        try {
            if (!Files.exists(CONFIG_DIR)) {
                Files.createDirectories(CONFIG_DIR);
            }
            try (FileWriter writer = new FileWriter(CONFIG_FILE.toFile())) {
                GSON.toJson(configData, writer);
            }
        } catch (IOException e) {
            System.err.println("Failed to save TotalItems config: " + e.getMessage());
        }
    }
    
    // 設定のゲッター/セッター
    public static boolean isEnabled() {
        return configData.enabled;
    }
    
    public static void setEnabled(boolean value) {
        configData.enabled = value;
        saveConfig();
    }
    
    public static List<String> getTrackedItems() {
        return configData.trackedItems;
    }
    
    public static void setTrackedItems(List<String> items) {
        configData.trackedItems = items;
        saveConfig();
    }
    
    public static void addTrackedItem(String itemId) {
        if (!configData.trackedItems.contains(itemId)) {
            configData.trackedItems.add(itemId);
            saveConfig();
        }
    }
    
    public static void removeTrackedItem(String itemId) {
        if (configData.trackedItems.remove(itemId)) {
            saveConfig();
        }
    }
}
