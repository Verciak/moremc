package net.moremc.api.configuration.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import net.moremc.api.configuration.Configuration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationImpl<T> implements Configuration<T>, Serializable {


    private Map<String, ConfigurationData> dataMap = new HashMap<>();
    private final String configName;
    private final Class<T> classData;


    public ConfigurationImpl(String configName, Class<T> classData) {
        this.configName = configName;
        this.classData = classData;
    }

    @Override
    public void initialize() {
        File file = new File(this.configName + ".json");

        if (!file.exists()) {
            try {
                ClassLoader classLoader = this.classData.getClassLoader();
                InputStream stream = classLoader.getResourceAsStream(this.configName + ".json");

                if (!file.createNewFile())
                    return;

                PrintWriter pw = new PrintWriter(new FileWriter(file));
                InputStreamReader streamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader);
                for (String line; (line = reader.readLine()) != null; ) pw.println(line);

                pw.close();
                reader.close();
                streamReader.close();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reload() {
        this.initialize();
    }

    @Override
    public void save(ConfigurationData configurationData, boolean application) {
        try {
            File filePath = new File(this.configName + ".json");
            FileWriter file = new FileWriter(filePath.getPath(), false);
            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                file.write(gson.toJson(this.dataMap.values()).toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                file.flush();
                file.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <X> X getOrCreate(String pathName, X value) {
        ConfigurationData configurationData = dataMap.get(pathName);
        if (configurationData == null) {
            configurationData = new ConfigurationData(pathName, value);
            dataMap.put(pathName, configurationData);
            this.save(configurationData, true);
        }
        return (X) configurationData.getObject();
    }

    public Map<String, ConfigurationData> getDataMap() {
        return dataMap;
    }

    @Override
    public T getConfiguration() {
        try {
            File file = new File(this.configName + ".json");
            JsonReader reader = new JsonReader(new FileReader(file.getPath()));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.fromJson(reader, this.classData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
