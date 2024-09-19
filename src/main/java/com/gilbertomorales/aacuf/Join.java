package com.gilbertomorales.aacuf;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player jogador = event.getPlayer();
        String nick = jogador.getName();
        String tag = Tag.getTag(jogador);

        event.setJoinMessage(tag + nick + " §fentrou no servidor.");

        if (jogador.hasPermission("aacuf.manager")) {

            Glow.aplicarGlow(jogador);
        }


    }

}
