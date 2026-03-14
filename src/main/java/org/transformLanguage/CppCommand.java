package org.transformLanguage;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CppCommand {

    static {
        try {
            System.loadLibrary("my_plugin");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Failed to load C++ plugin: " + e.getMessage());
        }
    }

    public static native void test(Player player, String[] args);

    public static void execute(String command, CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use C++ commands");
            return;
        }

        Player player = (Player) sender;

        try {
            if (command.equals("test")) {
                test(player, args);
            }
        } catch (Throwable e) {
            sender.sendMessage("C++ command error: " + e.getMessage());
        }
    }
}
