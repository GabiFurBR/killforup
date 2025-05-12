package com.gabifur_br.killforup.comandos;

import java.io.File;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.gabifur_br.killforup.KillForUP;

public class changelog implements CommandExecutor {

    private final KillForUP plugin;
    private File changelogFile;
    private YamlConfiguration changelogConfig;
    private int changesPerPage;
    private boolean changelogEnabled;
    private int maxPagesSetting;

    public changelog(KillForUP plugin) {
        this.plugin = plugin;
        loadConfig();
        loadChangelog();
    }

    private void loadConfig() {
        plugin.saveDefaultConfig(); // Garante que o config.yml exista
        changelogEnabled = plugin.getConfig().getBoolean("changelog.enabled", true);
        changesPerPage = plugin.getConfig().getInt("changelog.items_per_page", 5);
        maxPagesSetting = plugin.getConfig().getInt("changelog.max_pages", 30);
    }

    private void loadChangelog() {
        changelogFile = new File(plugin.getDataFolder(), "changelog.yml");
        if (!changelogFile.exists()) {
            plugin.saveResource("changelog.yml", false);
        }
        changelogConfig = YamlConfiguration.loadConfiguration(changelogFile);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("killforup.admin")) {
                sender.sendMessage(ChatColor.RED + "Você não tem permissão para recarregar o changelog.");
                return true;
            }

            loadConfig();
            loadChangelog();
            sender.sendMessage(ChatColor.GREEN + "Configurações e Changelog recarregados com sucesso!");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Apenas jogadores podem usar esse comando para ver o changelog.");
            return true;
        }
        Player player = (Player) sender;

        if (!changelogEnabled) {
            player.sendMessage(ChatColor.RED + "O sistema de changelog está desabilitado.");
            return true;
        }

        List<String> changes = changelogConfig.getStringList("changelog");

        if (changes.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Nenhum changelog encontrado.");
            return true;
        }

        int page = 1;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Número de página inválido.");
                return true;
            }
        }

        int maxPages = (int) Math.ceil((double) changes.size() / changesPerPage);
        if (maxPages > maxPagesSetting) {
            maxPages = maxPagesSetting; // limita o número de páginas
        }

        if (page < 1 || page > maxPages) {
            player.sendMessage(ChatColor.RED + "Página inválida. Existem " + maxPages + " páginas.");
            return true;
        }

        player.sendMessage(ChatColor.GOLD + "[KillForUP] " + ChatColor.WHITE + "Changelog (Página " + page + "/" + maxPages + "):");

        int start = (page - 1) * changesPerPage;
        int end = Math.min(start + changesPerPage, changes.size());

        for (int i = start; i < end; i++) {
            player.sendMessage(ChatColor.GRAY + "➤ " + ChatColor.YELLOW + changes.get(i));
        }

        if (page < maxPages) {
            player.sendMessage(ChatColor.GRAY + "Use " + ChatColor.GREEN + "/changelog " + (page + 1) + ChatColor.GRAY + " para ver mais.");
        }

        return true;
    }
}
