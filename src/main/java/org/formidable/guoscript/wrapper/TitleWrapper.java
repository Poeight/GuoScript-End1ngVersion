package org.formidable.guoscript.wrapper;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.formidable.guoscript.util.ColorUtil;

import java.lang.reflect.Constructor;

public class TitleWrapper {

    String title;
    String subtitle;
    int fadeIn;
    int stay;
    int fadeOut;

    private static boolean reflect = true;
    private static String VERSION = null;
    private static Class packetPlayOutTitleClass = null;
    private static Class iChatBaseComponentClass = null;
    private static Class packetClass = null;

    private Object titlePacket = null;
    private Object subtitlePacket = null;

    public TitleWrapper(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = ColorUtil.translateColor(title);
        this.subtitle = ColorUtil.translateColor(subtitle);
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
        if (VERSION == null) {
            VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            if (Integer.valueOf(VERSION.split("_")[1]) >= 12) {
                reflect = false;
                return;
            }
            packetPlayOutTitleClass = getNMSClass("PacketPlayOutTitle");
            iChatBaseComponentClass = getNMSClass("IChatBaseComponent");
            packetClass = getNMSClass("Packet");
        }
    }


    public void send(Player player) {
        if (reflect) {
            sendTitle(player);
        } else {
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    private static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + VERSION + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendTitle(Player player) {
        try {

            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);

            if (title != null) {
                if (titlePacket == null){
                    Object titleObject = packetPlayOutTitleClass.getDeclaredClasses()[0].getField("TITLE").get(null);
                    Object ichatbaseObject = iChatBaseComponentClass.getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
                    Constructor<?> titleConstructor = packetPlayOutTitleClass.getConstructor(packetPlayOutTitleClass.getDeclaredClasses()[0], iChatBaseComponentClass, int.class, int.class, int.class);
                    titlePacket = titleConstructor.newInstance(titleObject, ichatbaseObject, fadeIn, stay, fadeOut);
                }
                playerConnection.getClass().getMethod("sendPacket", packetClass).invoke(playerConnection, titlePacket);
            }

            if (subtitle != null) {
                if (subtitlePacket == null){
                    Object subTitleObject = packetPlayOutTitleClass.getDeclaredClasses()[0].getField("SUBTITLE").get(null);
                    Object ichatbaseObject = iChatBaseComponentClass.getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
                    Constructor<?> titleConstructor = packetPlayOutTitleClass.getConstructor(packetPlayOutTitleClass.getDeclaredClasses()[0], iChatBaseComponentClass, int.class, int.class, int.class);
                    subtitlePacket = titleConstructor.newInstance(subTitleObject, ichatbaseObject, fadeIn, stay, fadeOut);
                }
                playerConnection.getClass().getMethod("sendPacket", packetClass).invoke(playerConnection, subtitlePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
