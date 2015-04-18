package com.minecraftly.core.bukkit.language;

import com.minecraftly.core.bukkit.utilities.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Keir on 20/03/2015.
 */
public class SimpleLanguageManager implements LanguageManager {

    public ConfigManager configManager;
    public FileConfiguration config;
    public Map<String, LanguageValue> languageValues = new ConcurrentHashMap<>();
    private Logger logger; // todo do we need this?

    public SimpleLanguageManager(Logger logger, File languageFile) throws IOException {
        checkNotNull(logger);
        checkNotNull(languageFile);

        this.logger = logger;
        this.configManager = new ConfigManager(languageFile);
        reload();

        if (!languageFile.exists()) {
            try {
                languageFile.createNewFile();
            } catch (IOException e) {
                throw new IOException("Unable to create language file at: " + languageFile.getPath(), e);
            }
        }

        if (!languageFile.isFile()) {
            throw new IllegalArgumentException("File is not a file: " + languageFile.getPath());
        }
    }

    @Override
    public void reload() {
        configManager.reloadConfig();
        this.config = configManager.getConfig();

        for (Map.Entry<String, LanguageValue> entry : languageValues.entrySet()) {
            String key = entry.getKey();

            if (config.contains(key)) {
                entry.getValue().setValue(config.getString(key));
            }
        }
    }

    @Override
    public Map<String, LanguageValue> getLanguageValues() {
        return new HashMap<>(languageValues);
    }

    @Override
    public void registerAll(Map<String, LanguageValue> languageValues) {
        for (Map.Entry<String, LanguageValue> entry : languageValues.entrySet()) {
            register(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void register(String key, LanguageValue languageValue) {
        languageValues.put(key, languageValue);

        if (config.contains(key)) {
            languageValue.setValue(config.getString(key));
        } else {
            config.set(key, languageValue.getValue());
        }
    }

    @Override
    public String getRaw(String key) {
        return languageValues.containsKey(key) ? languageValues.get(key).getValue() : null;
    }

    @Override
    public String get(String key, Object... args) {
        String message = getRaw(key);
        return message != null ? String.format(message, args) : key;
    }

    @Override
    public void save() {
        for (Map.Entry<String, LanguageValue> entry : languageValues.entrySet()) {
            config.set(entry.getKey(), entry.getValue().unformattedValue);
        }

        configManager.saveConfig();
    }
}