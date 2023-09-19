package net.moremc.api.configuration.impl;

import java.io.Serializable;
import java.util.List;

public class ConfigurationData implements Serializable {

    private final String path;
    private final Object object;

    public ConfigurationData(String path, Object object){
        this.path = path;
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public String getPath() {
        return path;
    }

    public boolean isList(){
        if(object == null) return false;
        return object instanceof List;
    }

}
