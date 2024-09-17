package com.gilbertomorales.aacuf;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WhiteList implements Listener, CommandExecutor {

    private final JavaPlugin plugin;
    private final Set<UUID> whitelist;
    private File arquivoWL;
    private FileConfiguration configWL;

    public WhiteList(JavaPlugin plugin) {
        this.plugin = plugin;
        this.whitelist = new HashSet<>();
        carregarLista();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getCommand("lista").setExecutor(this);
    }

    private void carregarLista() {
        arquivoWL = new File(plugin.getDataFolder(), "whitelist.yml");

        if (!arquivoWL.exists()) {
            try {
                arquivoWL.getParentFile().mkdirs();
                arquivoWL.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        configWL = YamlConfiguration.loadConfiguration(arquivoWL);
        for (String uuid : configWL.getStringList("whitelist")) {
            whitelist.add(UUID.fromString(uuid));
        }

        plugin.getLogger().info("Lista de jogadores carregada com sucesso.");
    }

    private void saveWhitelist() {
        List<String> uuidList = new ArrayList<>();
        for (UUID uuid : whitelist) {
            uuidList.add(uuid.toString());
        }
        configWL.set("whitelist", uuidList);

        try {
            configWL.save(arquivoWL);
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("aacuf.lista")) {
            sender.sendMessage("§cVocê não tem permissão para usar este comando.");
            return true;
        }

        if (args.length == 0 || (args.length == 1 && !args[0].equalsIgnoreCase("l"))) {
            sender.sendMessage("§cUtilize: /lista <a/r/l>");
            return false;
        }

        String argumento = args[0];

        if (argumento.equalsIgnoreCase("l")) {
            if (whitelist.isEmpty()) {
                sender.sendMessage("§eNenhum jogador registrado na lista.");
            } else {
                sender.sendMessage("\n§aLista de jogadores que constam na lista:\n ");

                List<String> lista = new ArrayList<>();
                for (UUID uuid : whitelist) {
                    OfflinePlayer jogador = Bukkit.getOfflinePlayer(uuid);
                    String nickname = jogador.getName() != null ? jogador.getName() : "§cUsuário não registrado§r";
                    lista.add(nickname);
                }

                Collections.sort(lista, String.CASE_INSENSITIVE_ORDER);

                for (String name : lista) {
                    sender.sendMessage("- " + name);
                }

                sender.sendMessage(" ");
            }
            return true;
        }



        if (args.length != 2) {
            sender.sendMessage("§cUso: /lista <a/r> <jogador>");
            return false;
        }

        String jogador = args[1];
        OfflinePlayer player = Bukkit.getOfflinePlayer(jogador);

        if (argumento.equalsIgnoreCase("a")) {
            if (whitelist.add(player.getUniqueId())) {
                saveWhitelist();
                sender.sendMessage("§aJogador " + jogador + " adicionado à whitelist.");
            } else {
                sender.sendMessage("§cJogador já está na whitelist.");
            }
        } else if (argumento.equalsIgnoreCase("r")) {
            if (whitelist.remove(player.getUniqueId())) {
                saveWhitelist();
                sender.sendMessage("§aJogador " + jogador + " removido da whitelist.");
            } else {
                sender.sendMessage("§cJogador não está na whitelist.");
            }
        } else {
            sender.sendMessage("§cUso: /lista <a/r> <jogador>");
            return false;
        }

        return true;
    }


}
