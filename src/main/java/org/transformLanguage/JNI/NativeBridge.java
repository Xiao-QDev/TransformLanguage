package org.transformLanguage.JNI;

import java.io.*;
import java.nio.file.*;

public class NativeBridge {

    private static boolean loaded = false;

    static {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String libName = os.contains("win") ? "transformlang.dll"
                    : os.contains("mac") ? "libtransformlang.dylib"
                    : "libtransformlang.so";

            String ext = os.contains("win") ? ".dll"
                    : os.contains("mac") ? ".dylib"
                    : ".so";
            InputStream in = NativeBridge.class.getResourceAsStream("/natives/" + libName);
            if (in != null) {
                File temp = File.createTempFile("transformlang", ext);
                temp.deleteOnExit();
                Files.copy(in, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.load(temp.getAbsolutePath());
                loaded = true;
            }
        } catch (Throwable e) {
            System.err.println("Failed to load native library: " + e.getMessage());
        }
    }

    public static boolean isLoaded() {
        return loaded;
    }

    public native static boolean initNative();
    public native static String transformText(String input, String mode);
}
