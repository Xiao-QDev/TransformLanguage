package org.transformLanguage.Cpp.Command_Bridge;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import java.io.File;
import org.transformLanguage.JNI.NativeBridge;

//C++ 命令注册表 - 动态管理所有 C++ 插件的命令 然后插件查看
public class Cpp_tool {

    private static final Map<String, Long> commandRegistry = new ConcurrentHashMap<>();
    private static final Map<String, File> loadedPlugins = new ConcurrentHashMap<>(); // 插件文件名 -> File 对象
    private static Plugin plugin = null;

    //初始化插件引用（由主类调用）
    public static void init(Plugin pluginInstance) {
        plugin = pluginInstance;
    }
    
    //注册 C++ 命令（由 C++ 插件通过 JNI 调用）
    public static native void registerCommand(String commandName, long functionPtr);
    
    //执行已注册的 C++ 命令
    public static boolean executeCommand(String commandName, Object playerObject, String[] args) {
        if (!commandRegistry.containsKey(commandName)) {
            return false;
        }

        long funcPtr = commandRegistry.get(commandName);
        executeNative(funcPtr, playerObject, args);
        return true;
    }
    
    //执行 C++ 命令（带权限检查的公共接口）
    public static void execute(String command, CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use C++ commands");
            return;
        }

        Player player = (Player) sender;

        try {
            boolean executed = executeCommand(command, player, args);

            if (!executed) {
                sender.sendMessage("§cUnknown C++ command: " + command);
            }
        } catch (Throwable e) {
            sender.sendMessage("§cC++ command error: " + e.getMessage());
            System.err.println("[CommandRegistry] Error executing command '" + command + "': " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    //加载单个 C++ 插件并自动注册命令
    public static void loadPlugin(File file) {
        if (plugin == null) {
            System.err.println("[CommandRegistry] Plugin instance not initialized!");
            return;
        }
        
        try {
            // 检查 NativeBridge 是否已加载
            if (!NativeBridge.isLoaded()) {
                plugin.getLogger().warning("⚠ NativeBridge not loaded, some features may not work");
            }
            
            System.load(file.getAbsolutePath());
            plugin.getLogger().info("✓ Loaded C++ plugin: " + file.getName());
            loadedPlugins.put(file.getName(), file); // 记录已加载的插件
        } catch (UnsatisfiedLinkError e) {
            plugin.getLogger().severe("✗ Failed to load C++ plugin: " + file.getName());
            plugin.getLogger().severe("  Error: " + e.getMessage());
        }
    }

    //自动扫描并加载 plugins 目录下的所有 C++ 插件
    public static void loadAllPlugins() {
        if (plugin == null) {
            System.err.println("[CommandRegistry] Plugin instance not initialized!");
            return;
        }
        
        File pluginsDir = new File(plugin.getDataFolder(), "plugins");
        
        if (!pluginsDir.exists()) {
            if (pluginsDir.mkdirs()) {
                plugin.getLogger().info("Created plugins directory");
            } else {
                plugin.getLogger().warning("Failed to create plugins directory");
            }
            return;
        }
        
        File[] files = pluginsDir.listFiles((dir, name) ->
            name.endsWith(".dll") || name.endsWith(".so") || name.endsWith(".dylib")
        );

        if (files != null && files.length > 0) {
            plugin.getLogger().info("Found " + files.length + " C++ plugin(s), loading...");
            for (File file : files) {
                loadPlugin(file);
            }
        } else {
            plugin.getLogger().info("No C++ plugins found in " + pluginsDir.getAbsolutePath());
        }
    }
    
    //检查命令是否已注册
    public static boolean isRegistered(String commandName) {
        return commandRegistry.containsKey(commandName);
    }
    
    //获取所有已注册的命令列表
    public static String[] getRegisteredCommands() {
        return commandRegistry.keySet().toArray(new String[0]);
    }
    
    //获取所有已加载的插件文件列表
    public static String[] getLoadedPlugins() {
        return loadedPlugins.keySet().toArray(new String[0]);
    }
    
    //卸载命令
    public static void unregisterCommand(String commandName) {
        commandRegistry.remove(commandName);
    }
    
    //清空所有注册命令
    public static void clear() {
        commandRegistry.clear();
    }
    
    //执行 C++ 原生函数（JNI 内部使用）
    // 这个方法会被 C++ 的 REGISTER_COMMAND 宏生成的函数调用
    private static native void executeNative(long funcPtr, Object playerObject, String[] args);
}
