package com.gilbertomorales.aacuf;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Join implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player jogador = event.getPlayer();
        String nick = jogador.getName();
        String tag = Tag.getTag(jogador);

        event.setJoinMessage(tag + nick + " §fentrou no servidor.");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player jogador = event.getPlayer();
        String nick = jogador.getName();
        String tag = Tag.getTag(jogador);

        event.setQuitMessage(tag + nick + " §fsaiu do servidor.");
    }
}
