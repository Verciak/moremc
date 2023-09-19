package net.moremc.bukkit.api.data;

import com.google.common.base.Charsets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiConfig {

    private final JavaPlugin javaPlugin;
    private final String configName;

    private boolean instantSave;

    public ApiConfig(JavaPlugin javaPlugin, String configName){
        this.javaPlugin = javaPlugin;
        this.configName = configName;
    }

    private Map<String, ConfigData> dataMap = new HashMap<>();

    private FileConfiguration fileConfiguration;
    private File file;

    public <T> T getOrCreate(String path, T value){
        ConfigData configData = dataMap.get(path);
        if(configData == null) {
            configData = new ConfigData(path, value);
            dataMap.put(path, configData);
            if(instantSave)
                save(configData);
        }
        return (T) configData.object;
    }


    public void save(ConfigData configData){
        if(!exists()) create();
        fileConfiguration.set(configData.path, configData.object);
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(){
        return file != null && file.exists();
    }

    public void updateFileConfiguration(){
        if(fileConfiguration == null){
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
        }
    }

    public void load(){
        if(!exists()) createEmpty();
        if(fileConfiguration == null){
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
        }
        for (String key : fileConfiguration.getKeys(true)) {
            dataMap.put(key, new ConfigData(key, fileConfiguration.get(key)));
        }
    }

    private void createEmpty(){
        if(configName == null) return;

        File file = new File(javaPlugin.getDataFolder(), configName);
        file.getParentFile().mkdirs();
        this.file = file;

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        this.fileConfiguration = configuration;

        InputStream stream = javaPlugin.getResource(configName);
        if(stream != null) configuration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(stream, Charsets.UTF_8)));
    }

    public void create(){
        if(configName == null) return;

        File file = new File(javaPlugin.getDataFolder(), configName);
        file.getParentFile().mkdirs();
        this.file = file;

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        this.fileConfiguration = configuration;

        InputStream stream = javaPlugin.getResource(configName);
        if(stream != null)
            configuration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(stream, Charsets.UTF_8)));

        if(dataMap.isEmpty()) return;

        dataMap.forEach((String path, ConfigData data) -> {
            configuration.set(path, data.object);
        });

        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JavaPlugin getJavaPlugin() {
        return javaPlugin;
    }

    public String getConfigName() {
        return configName;
    }

    public boolean isInstantSave() {
        return instantSave;
    }

    public void setInstantSave(boolean instantSave) {
        this.instantSave = instantSave;
    }

    public Map<String, ConfigData> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, ConfigData> dataMap) {
        this.dataMap = dataMap;
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public void setFileConfiguration(FileConfiguration fileConfiguration) {
        this.fileConfiguration = fileConfiguration;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public static class ConfigData {

        private String path;
        private Object object;

        public ConfigData(String path, Object object){
            this.path = path;
            this.object = object;
        }

        public boolean isList(){
            if(object == null) return false;
            return object instanceof List;
        }

    }
}