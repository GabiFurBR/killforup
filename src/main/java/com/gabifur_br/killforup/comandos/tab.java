package com.gabifur_br.killforup.comandos;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gabifur_br.killforup.KillForUP;
import com.gabifur_br.killforup.uteis.vari;

public class tab {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    // Informações do jogador
                    double saldo = vari.getSaldo(player);
                    String saldoFormatado = formatSaldo(saldo);
                    String tag = vari.getTag(player);
                    String nivel = vari.getLevel(player);
                    int ping = player.getPing();

                    // TabList Name personalizado
                    String tabName = ChatColor.GOLD + "" + ChatColor.BOLD + tag 
                            + ChatColor.WHITE + player.getName() + " " 
                            + ChatColor.AQUA + "[" 
                            + ChatColor.DARK_GREEN + "R$: " + saldoFormatado 
                            + ChatColor.AQUA + " | " 
                            + ChatColor.LIGHT_PURPLE + ping + "ms" 
                            + ChatColor.AQUA + "]";

                    player.setPlayerListName(tabName);

                    // Header (Cabeçalho) bonito e traduzido
                    String header = 
                        ChatColor.GOLD + "" + ChatColor.BOLD + "» " +
                        ChatColor.YELLOW + "" + ChatColor.BOLD + "KillForUp " +
                        ChatColor.GOLD + "" + ChatColor.BOLD + "«" + "\n" +
                        ChatColor.DARK_GRAY + "────────────────────────────" + "\n" +
                        ChatColor.AQUA + "Jogadores Online: " + ChatColor.WHITE + Bukkit.getOnlinePlayers().size() + "\n" +
                        ChatColor.DARK_GRAY + "────────────────────────────";

                    // Footer (Rodapé) bonito e traduzido
                    String footer = 
                        ChatColor.DARK_GRAY + "────────────────────────────" + "\n" +
                        ChatColor.YELLOW + "Dinheiro: " + ChatColor.GREEN + saldoFormatado + "\n" +
                        ChatColor.YELLOW + "Nivel: " + ChatColor.AQUA + nivel + "\n" +
                        ChatColor.YELLOW + "Ping: " + ChatColor.LIGHT_PURPLE + ping + "ms\n" +
                        ChatColor.YELLOW + "Data: " + ChatColor.WHITE + LocalDateTime.now().format(formatter) + "\n" +
                        ChatColor.DARK_GRAY + "────────────────────────────";

                    player.setPlayerListHeaderFooter(header, footer);
                }
            }
        }.runTaskTimer(KillForUP.getInstance(), 0L, 20L);
    }

    private static String formatSaldo(double saldo) {
        String[] suffixes = {"", "K", "M", "B", "T", "Q", "QQ", "S", "SS", "O", "N", "D", "UD", "DD", "TD", "QD", "QTD", "SD", "SPD", "OD", "ND", "VD"};
        int index = 0;
        while (saldo >= 1000 && index < suffixes.length - 1) {
            saldo /= 1000.0;
            index++;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(saldo) + suffixes[index];
    }
}
