package com.minecraftly.modules.survivalworlds;

import static com.google.common.base.Preconditions.checkNotNull;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for converting to and from different dimensions (worlds).
 */
public enum WorldDimension {

    NETHER(World.Environment.NETHER, "_nether"), THE_END(World.Environment.THE_END, "_the_end");

    private World.Environment environment;
    private String suffix;

    WorldDimension(World.Environment environment, String suffix) {
        this.environment = environment;
        this.suffix = suffix;
    }

    public World.Environment getEnvironment() {
        return environment;
    }

    public String getSuffix() {
        return suffix;
    }

    public boolean matches(String worldName) {
        checkNotNull(worldName);
        return worldName.endsWith(suffix);
    }

    public String getBaseName(String worldName) {
        checkNotNull(worldName);

        for (WorldDimension worldDimension : values()) {
            if (worldDimension.matches(worldName)) {
                return worldName.substring(0, worldName.length() - suffix.length());
            }
        }

        return worldName;
    }

    public String convertTo(String baseWorldName) {
        checkNotNull(baseWorldName);
        return getBaseName(baseWorldName) + suffix;
    }

    public World convertTo(World world) {
        checkNotNull(world);
        return Bukkit.getWorld(convertTo(world.getName()));
    }

    public static String getBaseWorldName(String worldName) {
        checkNotNull(worldName);
        String returnWorldName = worldName;

        for (WorldDimension worldDimension : values()) {
            if (worldDimension.matches(worldName)) {
                returnWorldName = worldDimension.getBaseName(worldName);
                break;
            }
        }

        return returnWorldName;
    }

    public static World getBaseWorld(World world) {
        checkNotNull(world);
        World baseWorld = Bukkit.getWorld(getBaseWorldName(world.getName()));
        return baseWorld != null ? baseWorld : world;
    }

    public static List<Player> getPlayersAllDimensions(World baseWorld) {
        checkNotNull(baseWorld);
        List<Player> players = new ArrayList<>(baseWorld.getPlayers());

        for (WorldDimension worldDimension : WorldDimension.values()) {
            World world = worldDimension.convertTo(baseWorld);
            if (world != null) {
                players.addAll(world.getPlayers());
            }
        }

        return players;
    }

}
