package com.gilbertomorales.aacuf;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        Player jogador = event.getPlayer();
        String nick = jogador.getName();
        String tag = Tag.getTag(jogador).trim();

        String mensagem;
        if (jogador.hasPermission("aacuf.chat.colorido")) {
            mensagem = ChatColor.translateAlternateColorCodes('&', event.getMessage());
        } else {
            mensagem = event.getMessage();
        }

        if (jogador.hasPermission("aacuf.chat.destaque")) {
            event.setCancelled(true);
            jogador.getServer().broadcastMessage("\n" + tag + nick + ": §7" + mensagem + "\n ");
        } else {
            event.setFormat(tag + nick + ": §7" + mensagem);
        }
    }
}
