package com.gilbertomorales.aacuf;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private WhiteList whitelist;
    private ColetorChunk coletorChunk;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        whitelist = new WhiteList(this);
        this.getCommand("lista").setExecutor(whitelist);

        coletorChunk = new ColetorChunk(this);
        getServer().getPluginManager().registerEvents(coletorChunk, this);

        this.getCommand("givecoletor").setExecutor(coletorChunk);
        this.getCommand("removercoletores").setExecutor(coletorChunk);
        this.getCommand("near").setExecutor(new Proximos());
        this.getCommand("giveespecial").setExecutor(new Especiais(this));

        getServer().getConsoleSender().sendMessage(" ");
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "     AAA     AAAAA   CCCCCC   U     U   FFFFFF");
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "    A   A   A     A  C        U     U   F     ");
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "   AAAAAAA  AAAAAAA  C        U     U   FFFFF ");
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "  A       A A     A  C        U     U   F     ");
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + " A         A     A   CCCCCC    UUUUU    F     ");
        getServer().getConsoleSender().sendMessage(" ");
        getServer().getConsoleSender().sendMessage(ChatColor.WHITE + " AACUF " + ChatColor.YELLOW + "v1.0" + ChatColor.WHITE + " iniciado com sucesso.");
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + " Autor: " + ChatColor.WHITE + "Gilberto Morales");
        getServer().getConsoleSender().sendMessage(" ");
        getServer().getConsoleSender().sendMessage(ChatColor.UNDERLINE + "gilbertomorales.com" + ChatColor.WHITE + " | " + ChatColor.UNDERLINE + "github.com/eumorales§f");
        getServer().getConsoleSender().sendMessage(" ");

        getServer().getPluginManager().registerEvents(new Chat(), this);
        getServer().getPluginManager().registerEvents(new Join(), this);
        getServer().getPluginManager().registerEvents(new ComandosBloqueados(), this);
        getServer().getPluginManager().registerEvents(new Teleporte(this), this);
    }

    @Override
    public void onDisable() {
        coletorChunk.salvarColetores();
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "Plugin desativado com sucesso.");
    }
}
