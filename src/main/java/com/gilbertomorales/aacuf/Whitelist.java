package com.gilbertomorales.aacuf;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Whitelist implements Listener {

    private final Set<UUID> whitelist;

    public Whitelist(JavaPlugin plugin) {
        this.whitelist = new HashSet<>();
        loadWhitelistFromFile(plugin);

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void loadWhitelistFromFile(JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), "whitelist.txt");

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String playerName;
            while ((playerName = reader.readLine()) != null) {
                playerName = playerName.trim();
                if (!playerName.isEmpty()) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
                    if (player != null) {
                        whitelist.add(player.getUniqueId());
                    }
                }
            }
            plugin.getLogger().info("Lista de colaboradores carregada com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();

        if (!whitelist.contains(playerUUID)) {
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "§c§lAACUF | aacuf.com\n\n§cVocê não possui permissão para conectar.\n \n§cEste servidor é exclusivo para os usuários que \ncolaboraram com a criação do mesmo através do grupo geral.\n\nSolicite seu acesso: §nl.aacuf.com/mine");

        }
    }
}
