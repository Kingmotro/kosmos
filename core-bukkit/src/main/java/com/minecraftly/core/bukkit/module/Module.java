package com.minecraftly.core.bukkit.module;

import com.minecraftly.core.ContentOwner;
import com.minecraftly.core.bukkit.MclyCoreBukkitPlugin;
import com.minecraftly.core.bukkit.MinecraftlyCore;
import com.minecraftly.core.bukkit.language.LanguageManager;
import com.sk89q.intake.fluent.DispatcherNode;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Keir on 09/03/2015.
 */
public abstract class Module implements ContentOwner {

    private boolean initialized = false;
    private boolean enabled = false;
    private Logger logger;
    private ModuleProperties moduleProperties;
    private File jarFile;

    protected void onLoad(MinecraftlyCore plugin) {
    }

    protected void onEnable(MinecraftlyCore plugin) {
    }

    protected void onDisable(MinecraftlyCore plugin) {
    }

    protected void registerCommands(DispatcherNode dispatcherNode) {
    }

    protected void registerLanguageValues(LanguageManager languageManager) {
    }

    final void init(Logger logger, ModuleProperties moduleProperties, File jarFile) {
        if (!initialized) {
            this.logger = logger;
            this.moduleProperties = moduleProperties;
            this.jarFile = jarFile;
            this.initialized = true;
        } else {
            throw new UnsupportedOperationException("This module has already been initialized.");
        }
    }

    public final Logger getLogger() {
        return logger;
    }

    public final ModuleProperties getModuleProperties() {
        return moduleProperties;
    }

    public final File getJarFile() {
        return jarFile;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    protected final void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;

            if (isEnabled()) {
                try {
                    onEnable(MclyCoreBukkitPlugin.getInstance()); // todo replace static reference?
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error occurred whilst enabling this module", e);
                }
            } else {
                try {
                    onDisable(MclyCoreBukkitPlugin.getInstance()); // todo replace static reference?
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error occurred whilst disabling this module", e);
                }
            }
        }
    }

    @Override
    public final String getName() {
        return moduleProperties.getName();
    }

    @Override
    public final String getIdentifier() {
        return getModuleProperties().getIdentifier();
    }

    public final void registerEvents(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, MclyCoreBukkitPlugin.getInstance());
    }
}