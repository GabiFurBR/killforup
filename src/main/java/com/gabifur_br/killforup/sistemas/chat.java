package com.gabifur_br.killforup.sistemas;

import static com.gabifur_br.killforup.uteis.vari.formatSaldo;
import com.gabifur_br.killforup.uteis.vari;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gabifur_br.killforup.KillForUP;

import ch.njol.skript.variables.Variables;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;

public class chat implements Listener {
    @EventHandler
    public void aoEntrar(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String nome = player.getName();

        if (!player.hasPlayedBefore()) {
            e.setJoinMessage(ChatColor.YELLOW + "⭐ " + nome + " entrou pela primeira vez no servidor!");
        } else {
            e.setJoinMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.WHITE + nome);
        }
    }

    @EventHandler
    public void aoSair(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        String nome = player.getName();

        e.setQuitMessage(ChatColor.GRAY + "[" + ChatColor.RED + "-" + ChatColor.GRAY + "] " + ChatColor.WHITE + nome);
    }
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();

        // Verifica se o jogador está mutado (usando UUID)
        String uuid = player.getUniqueId().toString();
        if (KillForUP.mutedPlayers.getOrDefault(uuid, false)) {
            player.sendMessage(Component.text("Você não poderá enviar mensagens, pois está mutado", NamedTextColor.RED));
            event.setCancelled(true);
            return;
        }

        // Busca saldo e tag personalizados
        String saldoVar = "saldo::" + player.getUniqueId();
        String tagVar = "tag::" + player.getUniqueId();

        double saldo = parseLong(Variables.getVariable(saldoVar, null, false), 0);
        String tag = (String) Variables.getVariable(tagVar, null, false);

        // Formata a mensagem do chat
        event.setFormat(tag + " §f" + player.getName() + " §7(§e" + formatSaldo(saldo) + "§7) §8➜ §f" + event.getMessage());

        // Mencionar outros jogadores
        String message = event.getMessage();
        for (Player p : event.getRecipients()) {
            if (!p.equals(player) && message.contains(p.getName())) {
                p.sendActionBar(Component.text("§eO Jogador " + player.getName() + " te mencionou!", NamedTextColor.YELLOW));
                p.playSound(p.getLocation(), "block.note_block.pling", 1f, 1f);
            }
        }
    }

    private long parseLong(Object obj, long fallback) {
        if (obj instanceof Number number) return number.longValue();
        try {
            return Long.parseLong(String.valueOf(obj), 16);
        } catch (Exception e) {
            return fallback;
        }
    }
}
