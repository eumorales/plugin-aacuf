package com.gilbertomorales.aacuf;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Proximos implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return true;
        }

        Player jogador = (Player) sender;

        if (!jogador.hasPermission("aacuf.near")) {
            jogador.sendMessage("§cVocê não tem permissão para usar este comando.");
            return true;
        }

        Location localizacaoJogador = jogador.getLocation();

        List<Player> jogadoresProximos = new ArrayList<>();

        for (Player alvo : Bukkit.getOnlinePlayers()) {
            if (alvo != jogador) {
                jogadoresProximos.add(alvo);
            }
        }

        jogadoresProximos.sort(Comparator.comparingDouble(p -> p.getLocation().distance(localizacaoJogador)));

        if (jogadoresProximos.isEmpty()) {
            jogador.sendMessage("§eNenhum jogador encontrado nas proximidades.");
        } else {
            jogador.sendMessage("§eJogadores próximos:");
            for (Player proximo : jogadoresProximos) {
                double distancia = localizacaoJogador.distance(proximo.getLocation());
                jogador.sendMessage("§f- " + proximo.getName() + " §7(" + String.format("%.2f", distancia) + " blocos)");
            }
        }

        return true;
    }
}
