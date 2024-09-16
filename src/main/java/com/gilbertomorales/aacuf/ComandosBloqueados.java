package com.gilbertomorales.aacuf;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.Map;

public class ComandosBloqueados implements Listener {

    private final Map<String, ComandoBloqueado> comandosBloqueados;

    private static class ComandoBloqueado {
        String mensagem;
        String permissao;

        ComandoBloqueado(String mensagem, String permissao) {
            this.mensagem = mensagem;
            this.permissao = permissao;
        }
    }

    public ComandosBloqueados() {
        comandosBloqueados = new HashMap<>();

        comandosBloqueados.put("/kick", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/ban", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/op", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/stop", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/plugins", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/pl", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/plugin", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/bukkit:plugins", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/bukkit:pl", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/bukkit:plugin", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/reload", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/restart", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/deop", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/kill", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/save-all", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/save-off", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/save-on", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/effect", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/gamerule", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/time", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/weather", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/difficulty", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/worldborder", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/summon", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/give", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/xp", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/fly", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/bukkit:reload", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/bukkit:kill", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/spigot:restart", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));
        comandosBloqueados.put("/minecraft:gamerule", new ComandoBloqueado("§cÉ preciso ser do grupo §6[ADMIN] §cpara utilizar este comando.", "aacuf.admin"));

    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0];

        if (comandosBloqueados.containsKey(command.toLowerCase())) {
            ComandoBloqueado comandoBloqueado = comandosBloqueados.get(command.toLowerCase());

            if (!event.getPlayer().hasPermission(comandoBloqueado.permissao)) {

                event.setCancelled(true);
                event.getPlayer().sendMessage(comandoBloqueado.mensagem);
            }
        }
    }
}
