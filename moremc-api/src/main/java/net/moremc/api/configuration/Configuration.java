package net.moremc.api.configuration;


import net.moremc.api.configuration.impl.ConfigurationData;

public interface Configuration<T> {


    /**
     * @parm Method initialize configuration
     */
    void initialize();

    /**
     * @parm Method reload configuration
     */
    void reload();


    /**
     * @parm Method save, save configData and put to the configName.json
     * @parm boolean application, where save? application or folderPath in the module
     */
    void save(ConfigurationData configurationData, boolean application);

    /**
     * @parm Method getOrCreate return select object with path or create path and put to the configName.json
     */
    <X> X getOrCreate(String pathName, X value);

    /**
     * @parm Method getConfiguration() return T class configuration from configName.json
     */

    T getConfiguration();

}
