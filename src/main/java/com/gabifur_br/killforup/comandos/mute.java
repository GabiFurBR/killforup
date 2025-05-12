package com.gabifur_br.killforup.comandos;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class mute implements CommandExecutor {

    private final Map<String, Boolean> mutedPlayers;

    public mute(Map<String, Boolean> mutedPlayers) {
        this.mutedPlayers = mutedPlayers;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("furry.mute")) {
            sender.sendMessage(Component.text("Você não tem permissão para isso.", NamedTextColor.RED));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(Component.text("Uso: /" + label + " <jogador>", NamedTextColor.RED));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Component.text("Jogador não encontrado!", NamedTextColor.RED));
            return true;
        }

        String uuid = target.getUniqueId().toString();

        if (label.equalsIgnoreCase("mute")) {
            if (mutedPlayers.getOrDefault(uuid, false)) {
                sender.sendMessage(Component.text(target.getName() + " já está mutado!", NamedTextColor.RED));
            } else {
                mutedPlayers.put(uuid, true);
                sender.sendMessage(Component.text("Você mutou " + target.getName() + "!", NamedTextColor.GOLD));
                target.sendMessage(Component.text("Você foi mutado por um administrador!", NamedTextColor.RED));
            }
        }

        if (label.equalsIgnoreCase("unmute")) {
            if (!mutedPlayers.getOrDefault(uuid, false)) {
                sender.sendMessage(Component.text(target.getName() + " não está mutado!", NamedTextColor.RED));
            } else {
                mutedPlayers.remove(uuid);
                sender.sendMessage(Component.text("Você desmutou " + target.getName() + "!", NamedTextColor.GREEN));
                target.sendMessage(Component.text("Você foi desmutado!", NamedTextColor.GREEN));
            }
        }

        return true;
    }
}
