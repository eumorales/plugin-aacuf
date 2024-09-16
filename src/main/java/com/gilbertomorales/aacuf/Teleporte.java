package com.gilbertomorales.aacuf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.UUID;

public class Teleporte implements Listener {

    private final JavaPlugin plugin;
    private final HashSet<UUID> jogadoresImobilizados;

    public Teleporte(JavaPlugin plugin) {
        this.plugin = plugin;
        this.jogadoresImobilizados = new HashSet<>();
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player jogador = event.getPlayer();
        String[] args = event.getMessage().split(" ");

        if (args[0].equalsIgnoreCase("/tp")) {
            event.setCancelled(true);
            if (jogador.hasPermission("aacuf.tp")) {
                handleTpCommand(jogador, args);
            } else {
                jogador.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
            }
        } else if (args[0].equalsIgnoreCase("/tphere")) {
            event.setCancelled(true);
            if (jogador.hasPermission("aacuf.tphere")) {
                handleTpHereCommand(jogador, args);
            } else {
                jogador.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
            }
        } else if (args[0].equalsIgnoreCase("/tpall")) {
            event.setCancelled(true);
            if (jogador.hasPermission("aacuf.tpall")) {
                handleTpAllCommand(jogador);
            } else {
                jogador.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player jogador = event.getPlayer();
        if (jogadoresImobilizados.contains(jogador.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    private void handleTpCommand(Player jogador, String[] args) {
        if (args.length < 2) {
            jogador.sendMessage(ChatColor.RED + "Utilize: /tp <Usuário>.");
            return;
        }

        Player alvo = Bukkit.getPlayer(args[1]);
        if (alvo == null) {
            jogador.sendMessage(ChatColor.RED + "Jogador não encontrado.");
            return;
        }

        if (alvo.equals(jogador)) {
            jogador.sendMessage(ChatColor.RED + "Você não pode se teleportar para você mesmo.");
            return;
        }

        String tagAlvo = Tag.getTag(alvo);
        jogador.teleport(alvo.getLocation());
        jogador.sendMessage(ChatColor.GREEN + "Teleportado até " + tagAlvo + alvo.getName() + ChatColor.GREEN + ".");
    }

    private void handleTpHereCommand(Player jogador, String[] args) {
        if (args.length < 2) {
            jogador.sendMessage(ChatColor.RED + "Utilize: /tphere <Usuário>.");
            return;
        }

        Player alvo = Bukkit.getPlayer(args[1]);
        if (alvo == null) {
            jogador.sendMessage(ChatColor.RED + "Jogador não encontrado.");
            return;
        }

        if (alvo.equals(jogador)) {
            jogador.sendMessage(ChatColor.RED + "Você não pode puxar a si mesmo.");
            return;
        }

        String tagJogador = Tag.getTag(jogador);
        String tagAlvo = Tag.getTag(alvo);
        alvo.teleport(jogador.getLocation());
        jogador.sendMessage(ChatColor.GREEN + tagAlvo + alvo.getName() + ChatColor.GREEN + " foi puxado até você.");
    }

    private void handleTpAllCommand(Player jogador) {
        String tagJogador = Tag.getTag(jogador);
        for (Player jogadorOnline : Bukkit.getOnlinePlayers()) {
            if (!jogadorOnline.equals(jogador)) {
                jogadorOnline.teleport(jogador.getLocation());
            }
        }
        jogador.sendMessage(ChatColor.GREEN + "Todos jogadores puxados até " + tagJogador + jogador.getName() + ChatColor.GREEN + ".");
    }
}
