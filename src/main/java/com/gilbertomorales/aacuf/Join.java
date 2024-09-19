package com.gilbertomorales.aacuf;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;

import java.util.HashSet;
import java.util.Set;

public class Join implements Listener {

    private final Set<Player> aguardandoSocio = new HashSet<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player jogador = event.getPlayer();
        String nick = jogador.getName();
        String tag = Tag.getTag(jogador);

        event.setJoinMessage(tag + nick + " §fentrou no servidor.");

        if (jogador.hasPermission("aacuf.manager")) {
            Glow.aplicarGlow(jogador);
        }

        aguardandoSocio.add(jogador);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player jogador = event.getPlayer();

        if (aguardandoSocio.contains(jogador)) {

            if (!jogador.hasPermission("aacuf.socio")) {
                ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta bookMeta = (BookMeta) book.getItemMeta();

                bookMeta.setTitle(ChatColor.GOLD + "AACUF");
                bookMeta.setAuthor("AACUF");

                bookMeta.addPage(

                        ChatColor.GOLD + ChatColor.BOLD.toString() + "     SEJA SÓCIO!\n\n" +
                                ChatColor.DARK_GRAY + "Receba desconto em produtos e eventos, além de benefícios de parceiros da atlética.\n" +
                                "\n\n" +
                                "§3Sócio Sênior: §7R$59,90\n§bSócio Pleno: §7R$39,90\n\n\n" +
                                ChatColor.GOLD + "Acesse:" + ChatColor.DARK_GRAY + " §naacuf.com"
                );
                bookMeta.setGeneration(Generation.ORIGINAL);

                book.setItemMeta(bookMeta);
                
                jogador.openBook(book);
            }
            
            aguardandoSocio.remove(jogador);
        }
    }
}
