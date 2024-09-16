package com.gilbertomorales.aacuf;

import org.bukkit.entity.Player;

public class Tag {

    public static String getTag(Player player) {
        if (player.hasPermission("aacuf.manager")) {
            return "§b§l[M] §b";
        } else if (player.hasPermission("aacuf.builder")) {
            return "§5[BUILDER] §5";
        } else if (player.hasPermission("aacuf.admin")) {
            return "§c[ADMIN] §c";
        } else if (player.hasPermission("aacuf.diretoria")) {
            return "§6[Diretoria] §6";
        } else if (player.hasPermission("aacuf.socio")) {
            return "§a[Sócio] §a";
        } else if (player.hasPermission("aacuf.colab")) {
            return "§d[Colab] §d";
        } else {
            return "§f";
        }
    }
}
