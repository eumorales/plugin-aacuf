package com.gilbertomorales.aacuf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ColetorChunk implements Listener, CommandExecutor {

    private final Set<Location> chestLocations = new HashSet<>();
    private final Main plugin;
    private File locationsFile;
    private FileConfiguration locationsConfig;

    private final String nomeColetor = ChatColor.DARK_PURPLE + "Coletor de Chunk";
    private final String nomeBauColetor = ChatColor.DARK_GRAY + "Itens - Coletor de Chunk";
    private final String loreColetor = ChatColor.WHITE + "Coleta os drops de um chunk.";

    public ColetorChunk(Main plugin) {
        this.plugin = plugin;
        carregarColetores();
        iniciarParticulas();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();
        if (block.getType() == Material.CHEST) {
            boolean isColetor = event.getItemInHand().hasItemMeta() &&
                    event.getItemInHand().getItemMeta().getDisplayName().equals(nomeColetor);

            if (isColetor) {
                for (Location chestLocation : chestLocations) {
                    if (chestLocation.getChunk().equals(block.getLocation().getChunk())) {
                        event.getPlayer().sendMessage(ChatColor.RED + "Já existe um Coletor posicionado neste chunk.");
                        event.setCancelled(true);
                        return;
                    }
                }
            }

            BlockFace[] horizontalFaces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
            for (BlockFace face : horizontalFaces) {
                Block adjacentBlock = block.getRelative(face);
                if (adjacentBlock.getType() == Material.CHEST) {
                    BlockState state = adjacentBlock.getState();
                    if (state instanceof Chest chest) {
                        if (chest.getCustomName() != null && chest.getCustomName().equals(nomeBauColetor)) {
                            event.getPlayer().sendMessage(ChatColor.RED + "Você não pode colocar um baú próximo a um Coletor de Chunk.");
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }

            if (isColetor) {
                BlockState state = block.getState();
                if (state instanceof Chest chest) {
                    chest.setCustomName(nomeBauColetor);
                    chest.update();
                    chestLocations.add(block.getLocation());
                    block.getWorld().spawnParticle(Particle.DRAGON_BREATH, block.getLocation().add(0.5, 1, 0.5), 100, 0.5, 0.5, 0.5, 0.1);
                    salvarColetores();
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Coletor de chunk posicionado com sucesso.");
                    block.getWorld().playSound(block.getLocation(), "entity.item.pickup", 1f, 1f);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();
        if (block.getType() == Material.CHEST) {
            BlockState state = block.getState();
            if (state instanceof Chest chest) {
                if (chest.getCustomName() != null && chest.getCustomName().equals(nomeBauColetor)) {
                    chestLocations.remove(block.getLocation());
                    event.setDropItems(false);
                    ItemStack chestItem = new ItemStack(Material.CHEST, 1);
                    ItemMeta meta = chestItem.getItemMeta();
                    if (meta != null) {
                        meta.setDisplayName(nomeColetor);
                        meta.setLore(Arrays.asList(loreColetor));
                        chestItem.setItemMeta(meta);
                    }
                    block.getWorld().dropItemNaturally(block.getLocation(), chestItem);
                    salvarColetores();
                }
            }
        }
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        Item item = event.getEntity();
        Location location = item.getLocation();

        for (Location chestLocation : chestLocations) {
            if (chestLocation.getChunk().equals(location.getChunk())) {
                BlockState state = chestLocation.getBlock().getState();

                if (state instanceof Chest chest) {
                    Inventory inventory = chest.getInventory();

                    if (inventory.addItem(item.getItemStack()).isEmpty()) {
                        item.remove();
                        item.getWorld().spawnParticle(Particle.PORTAL, item.getLocation(), 10, 0.1, 0.1, 0.1, 0.05);
                        item.getWorld().playSound(item.getLocation(), "entity.item.pickup", 1f, 1f);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("givecoletor")) {
            if (!sender.hasPermission("aacuf.coletor.give")) {
                sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
                return true;
            }

            if (args.length != 1) {
                sender.sendMessage(ChatColor.RED + "Uso correto: /givecoletor <jogador>");
                return false;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Jogador não encontrado.");
                return true;
            }

            ItemStack chestItem = new ItemStack(Material.CHEST, 1);
            ItemMeta meta = chestItem.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(nomeColetor);
                meta.setLore(Arrays.asList(loreColetor));
                chestItem.setItemMeta(meta);
            }

            target.getInventory().addItem(chestItem);
            sender.sendMessage(ChatColor.GREEN + "Coletor de Chunk entregue para " + target.getName() + ".");
            target.sendMessage(ChatColor.GREEN + "Você recebeu um Coletor de Chunk.");
            return true;
        } else if (command.getName().equalsIgnoreCase("removercoletores")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Este comando só pode ser usado por um jogador.");
                return true;
            }

            Player player = (Player) sender;

            if (!player.hasPermission("aacuf.coletor.remover")) {
                player.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
                return true;
            }

            removerColetoresProximos(player);
            return true;
        }

        return false;
    }

    private void removerColetoresProximos(Player player) {
        Location playerLocation = player.getLocation();
        int raio = 3;

        Set<Location> coletoresARemover = new HashSet<>();
        for (Location loc : chestLocations) {
            if (loc.getWorld().equals(playerLocation.getWorld()) && loc.distance(playerLocation) <= raio) {
                coletoresARemover.add(loc);
                loc.getBlock().setType(Material.AIR);
            }
        }

        chestLocations.removeAll(coletoresARemover);
        salvarColetores();

        if (coletoresARemover.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Nenhum coletor encontrado em um raio de " + raio + " blocos.");
        } else {
            player.sendMessage(ChatColor.GREEN + "Foram removidos " + coletoresARemover.size() + " coletor(es).");
        }
    }

    void carregarColetores() {
        locationsFile = new File(plugin.getDataFolder(), "coletores.yml");
        if (!locationsFile.exists()) {
            try {
                locationsFile.getParentFile().mkdirs();
                locationsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().severe("Não foi possível criar o arquivo coletores.yml!");
            }
        }

        locationsConfig = YamlConfiguration.loadConfiguration(locationsFile);

        for (String key : locationsConfig.getKeys(false)) {
            double x = locationsConfig.getDouble(key + ".x");
            double y = locationsConfig.getDouble(key + ".y");
            double z = locationsConfig.getDouble(key + ".z");
            String worldName = locationsConfig.getString(key + ".world");

            Location location = new Location(Bukkit.getWorld(worldName), x, y, z);
            chestLocations.add(location);
        }
    }

    void salvarColetores() {
        if (locationsConfig == null) {
            locationsConfig = new YamlConfiguration();
        }

        int index = 0;
        for (Location loc : chestLocations) {
            String key = "location" + index;
            locationsConfig.set(key + ".x", loc.getX());
            locationsConfig.set(key + ".y", loc.getY());
            locationsConfig.set(key + ".z", loc.getZ());
            locationsConfig.set(key + ".world", loc.getWorld().getName());
            index++;
        }

        try {
            locationsConfig.save(locationsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void iniciarParticulas() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Location location : chestLocations) {
                location.getWorld().spawnParticle(Particle.DRAGON_BREATH, location.clone().add(0.5, 1.5, 0.5), 3, 0.1, 0.1, 0.1, 0.01);
            }
        }, 0L, 20L);
    }
}
