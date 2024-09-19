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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Especiais implements CommandExecutor, Listener {

    private final JavaPlugin plugin;

    public Especiais(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private final Map<Integer, Propriedades> listaItens = new HashMap<>() {{
        put(1, new Propriedades("&b&lLIMITE DE 5 HOMES", "&fAumente seu limite de homes!", "system.homes.5"));

    }};

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("aacuf.giveespecial")) {
            sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(" \n§cUtilize: /giveespecial <Jogador> <ID>");
            for (Map.Entry<Integer, Propriedades> entry : listaItens.entrySet()) {
                int id = entry.getKey();
                String nome = ChatColor.translateAlternateColorCodes('&', entry.getValue().getNome());
                sender.sendMessage(ChatColor.GRAY + "\nID " + id + ": " + nome);
            }
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cUtilize: /giveespecial <Jogador> <ID>");
            return false;
        }

        String jogadorNome = args[0];
        int itemId;

        try {
            itemId = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "ID inválido.");
            return false;
        }

        Player jogador = Bukkit.getPlayer(jogadorNome);
        if (jogador == null) {
            sender.sendMessage(ChatColor.RED + "Jogador não encontrado.");
            return true;
        }

        Propriedades itemProps = listaItens.get(itemId);
        if (itemProps == null) {
            sender.sendMessage(ChatColor.RED + "ID não encontrado.");
            return false;
        }

        darItemEspecial(jogador, itemProps);
        return true;
    }

    private void darItemEspecial(Player jogador, Propriedades itemProps) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemProps.getNome()));
        meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', itemProps.getLore())));

        meta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

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
        String itemName = meta.getDisplayName();

        for (Propriedades itemProps : listaItens.values()) {
            String predefinedName = ChatColor.translateAlternateColorCodes('&', itemProps.getNome());
            if (itemName.equals(predefinedName)) {
                String permissao = itemProps.getPermissao();

                if (jogador.hasPermission(permissao)) {
                    jogador.sendMessage(ChatColor.RED + "Você já possui esse privilégio.");
                    return;
                }

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + jogador.getName() + " permission set " + permissao);
                jogador.sendMessage(ChatColor.GREEN + "Você recebeu um novo privilégio. Aproveite! :)");

                item.setAmount(item.getAmount() - 1);
                break;
            }
        }
    }

    private static class Propriedades {
        private final String nome;
        private final String lore;
        private final String permissao;

        public Propriedades(String nome, String lore, String permissao) {
            this.nome = nome;
            this.lore = lore;
            this.permissao = permissao;
        }

        public String getNome() {
            return nome;
        }

        public String getLore() {
            return lore;
        }

        public String getPermissao() {
            return permissao;
        }
    }
}
