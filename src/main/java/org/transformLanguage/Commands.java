package org.transformLanguage;

import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;
import java.util.List;
import java.util.Arrays;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Commands {
    
    private final Plugin plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    
    public Commands(Plugin plugin) {
        this.plugin = plugin;
    }

    private void sendMessage(CommandSender sender, String miniMessageStr) {
        if (sender instanceof Player) {
            sender.sendMessage(miniMessage.deserialize(miniMessageStr));
        } else {
            String ansiMessage = convertToAnsi(miniMessageStr);
            sender.sendMessage(ansiMessage);
        }
    }
    
    private String convertToAnsi(String miniMessageStr) {
        String result = miniMessageStr
            .replaceAll("<gradient:[^>]+>", "")
            .replace("</gradient>", "")
            .replace("<gold>", "\u001B[38;2;255;165;0m")
            .replace("</gold>", "\u001B[0m")
            .replace("<gray>", "\u001B[37m")
            .replace("</gray>", "\u001B[0m")
            .replace("<yellow>", "\u001B[93m")
            .replace("</yellow>", "\u001B[0m")
            .replace("<white>", "\u001B[97m")
            .replace("</white>", "\u001B[0m")
            .replace("<red>", "\u001B[91m")
            .replace("</red>", "\u001B[0m")
            .replace("<bold>", "")
            .replace("</bold>", "");
        return result + "\u001B[0m";
    }
    
    public void register() {
        plugin.getServer().getCommandMap().register("transformlanguage", new Command("transformlanguage", "A TransformLanguage Plugin Commands", "/tfl", List.of("tfl")) {
            
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                if (!sender.hasPermission("transformLanguage.admin")) {
                    sendMessage(sender, "<red>❌ You don't have permission to use this command!</red>");
                    return true;
                }
                
                if (args.length == 0) {
                    sendMessage(sender, "<gold>TransformLanguage</gold> <gray>- 通译互转</gray>");
                    return true;
                }
                
                switch (args[0].toLowerCase()) {
                    case "help", "?" -> {
                        sendMessage(sender, "<gradient:#FF00FF:#00FFFF>----------[ Help Message ]----------</gradient>");
                        sendMessage(sender, "<gradient:#F97316:#EC4899>/tfl help</gradient> <gray>- Display command help.</gray>");
                        sendMessage(sender, "<gradient:#F97316:#EC4899>/tfl version</gradient> <gray>- Display plugin version.</gray>");
                        sendMessage(sender, "<gradient:#F97316:#EC4899>/tfl transform <mode> <text...></gradient> <gray>- Transform text (upper/lower/reverse).</gray>");
                    }
                    case "version", "ver" -> sendMessage(sender, "<gradient:#FFD700:#FFA900>Plugin Version: </gradient><white>" + plugin.getPluginMeta().getVersion() + "</white>");
                    case "transform" -> {
                        if (args.length < 3) {
                            sendMessage(sender, "<red>Usage: /tfl transform <mode> <text...></red>");
                            sendMessage(sender, "<gray>Modes: upper, lower, reverse</gray>");
                            return true;
                        }
                        try {
                            if (!NativeBridge.isLoaded()) {
                                sendMessage(sender, "<red>Native library not loaded</red>");
                                return true;
                            }
                            String mode = args[1];
                            String text = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                            String result = NativeBridge.transformText(text, mode);
                            sendMessage(sender, "<gold>Result: </gold><white>" + result + "</white>");
                        } catch (Throwable e) {
                            sendMessage(sender, "<red>Transform failed: " + e.getMessage() + "</red>");
                        }
                    }
                    default -> sendMessage(sender, "<red>⚠ No,you entered it wrong.</red>");
                }
                return true;
            }
            
            @Override
            public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
                if (args.length == 1) {
                    return Arrays.asList("help","?", "version", "ver", "transform");
                }
                if (args.length == 2 && args[0].equalsIgnoreCase("transform")) {
                    return Arrays.asList("upper", "lower", "reverse");
                }
                return null;
            }
        });
    }
}

