package org.transformLanguage;

import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Arrays;

public class Commands {
    
    private final Plugin plugin;
    
    public Commands(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void register() {
        plugin.getServer().getCommandMap().register("transformlanguage", new Command("transformlanguage", "TransformLanguage 主指令", "/tfl", List.of("tfl")) {
            
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                if (args.length == 0) {
                    sender.sendMessage("§6TransformLanguage §7- 通译互转");
                    return true;
                }
                
                switch (args[0].toLowerCase()) {
                    case "help", "?" -> {
                        sender.sendMessage("----------------[§eHelp Message§f]----------------");
                        sender.sendMessage("                  [§e帮助信息§f]                    ");
                        sender.sendMessage("/tfl help: Display command help.|输入命令帮助        ");
                        sender.sendMessage("   /tfl version: Display plugin version.           ");
                        sender.sendMessage(" 输入命令来查询您服务器上面本插件模块的版本                ");
                    }
                    case "version", "ver" -> sender.sendMessage("§6plugin version：" + plugin.getPluginMeta().getVersion());
                    default -> sender.sendMessage("§cNo,you entered it wrong.");
                }
                return true;
            }
            
            @Override
            @SuppressWarnings("DataFlowIssue")
            public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
                if (args.length == 1) {
                    return Arrays.asList("help","?", "version", "ver");
                }
                return null;
            }
        });
    }
}

