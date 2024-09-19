package com.gilbertomorales.aacuf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;

import java.util.Arrays;

public class Especiais implements CommandExecutor, Listener {

    private final JavaPlugin plugin;

    public Especiais(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("aacuf.giveespecial")) {
            sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
            return true;
        }

        if (args.length < 4) {
            sender.sendMessage("§cUtilize: /giveespecial <Jogador> <Nome> <Descrição> <Permissão>");
            return false;
        }

        String jogadorNome = args[0];
        String nome = args[1];
        String lore = args[2].replace("_", " ");
        String permissao = args[3];

        Player jogador = Bukkit.getPlayer(jogadorNome);
        if (jogador == null) {
            sender.sendMessage(ChatColor.RED + "Jogador não encontrado.");
            return true;
        }

        darItemEspecial(jogador, nome, lore, permissao);
        return true;
    }

    private void darItemEspecial(Player jogador, String nome, String lore, String permissao) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', nome));
        meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lore)));

        meta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        NamespacedKey key = new NamespacedKey(plugin, "permissao");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, permissao);

        item.setItemMeta(meta);

        jogador.getInventory().addItem(item);
        jogador.sendMessage(ChatColor.GREEN + "Você recebeu um item especial!");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player jogador = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.PAPER || !item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "permissao");

        if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            String permissao = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);

            if (jogador.hasPermission(permissao)) {
                jogador.sendMessage(ChatColor.RED + "Você já possui esse privilégio.");
                return;
            }

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + jogador.getName() + " permission set " + permissao);
            jogador.sendMessage(ChatColor.GREEN + "Você recebeu um novo privilégio. Aproveite! :)");

            item.setAmount(item.getAmount() - 1);
        }
    }
}
