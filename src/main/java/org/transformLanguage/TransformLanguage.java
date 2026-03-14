package org.transformLanguage;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class TransformLanguage extends JavaPlugin {
    
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String PINK = "\u001B[38;2;255;102;245m";
    private static final String PINK1 = "\u001B[38;2;255;122;247m";
    private static final String PINK2 = "\u001B[38;2;255;142;249m";
    private static final String PINK3 = "\u001B[38;2;255;162;251m";
    private static final String PINK4 = "\u001B[38;2;255;182;253m";
    private static final String PINK5 = "\u001B[38;2;255;202;255m";

    @Override
    public void onEnable() {
        new Commands(this).register();
        
        new BukkitRunnable() {
            @Override
            public void run() {
                logo();
                getLogger().info(RESET + "       TransformLanguage Enabled!" + RESET);
                getLogger().info(RESET + "     Version: " + getPluginMeta().getVersion() + RESET);
                getLogger().info(RESET +"       JNI Bridge Initializing..." + RESET);
                
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        getLogger().info(RESET +"✓ JNI Bridge Initialized Successfully!" + RESET);
                    }
                }.runTaskLater(TransformLanguage.this, 80L);
            }
        }.runTaskLater(this, 20L);
    }

    @Override
    public void onDisable() {
        getLogger().info(RED +"⚠ TransformLanguage Disabled!" + RESET);
        getLogger().info(RESET + "Cleaning up native resources..." + RESET);
        getLogger().info(RESET + "          Goodbye!" + RESET);
    }
    
    private void logo() {
        getLogger().info( RESET + "--------------------------------------" + RESET);
        getLogger().info(PINK + "    ████████╗   ███████╗   ██║        " + RESET);
        getLogger().info(PINK1 + "    ╚══██╔══╝   ██╔════╝   ██║        " + RESET);
        getLogger().info(PINK2 + "       ██║      █████╗     ██║        " + RESET);
        getLogger().info(PINK3 + "       ██║      ██╔══╝     ██║        " + RESET);
        getLogger().info(PINK4 + "       ██║      ██║        ██████╗    " + RESET);
        getLogger().info(PINK5 + "       ╚═╝      ╚═╝        ╚═════╝    " + RESET);
        getLogger().info(RESET + "--------------------------------------" + RESET);
    }
}