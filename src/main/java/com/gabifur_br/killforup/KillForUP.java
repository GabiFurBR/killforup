package com.gabifur_br.killforup;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import com.gabifur_br.killforup.comandos.changelog;
import com.gabifur_br.killforup.comandos.tab;
import com.gabifur_br.killforup.comandos.mute;
import com.gabifur_br.killforup.sistemas.chat;
import com.gabifur_br.killforup.sistemas.scoreboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class KillForUP extends JavaPlugin {

    private static KillForUP instance;
    public static final Map<String, Boolean> mutedPlayers = new HashMap<>();

    public static KillForUP getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        saveResource("changelog.yml", false);

        // Registrar comandos com verificação
        registerCommand("changelog", new changelog(this));
        registerCommand("mute", new mute(mutedPlayers));
        registerCommand("unmute", new mute(mutedPlayers)); // Você pode considerar separar isso também

        // Sistemas
        tab.start();
        scoreboard.startScoreboard();
        getServer().getPluginManager().registerEvents(new chat(), this);

        // Tela de carregamento
        showLoadingScreen();

        getLogger().info("[KillForUP] Plugin ativado com sucesso!");
    }

    @Override
    public void onDisable() {
        getLogger().info("[KillForUP] Plugin desativado.");
        instance = null;
    }

    private void showLoadingScreen() {
        String line = "===================================";
        TextColor yellow = TextColor.color(0xFFFF00);

        Component bar = Component.text(line, yellow);
        Component loading1 = Component.text("     KillForUP Carregando...", yellow);
        Component loading2 = Component.text("     Versão: 1.4.0", yellow);
        Component loading3 = Component.text("     Por: GabiFur_BR", yellow);
        Component step1 = Component.text("[KillForUP] Inicializando...", yellow);
        Component step2 = Component.text("[KillForUP] Aguardando eventos...", yellow);

        var console = getServer().getConsoleSender();
        console.sendMessage(bar);
        console.sendMessage(loading1);
        console.sendMessage(loading2);
        console.sendMessage(loading3);
        console.sendMessage(bar);
        console.sendMessage(step1);
        console.sendMessage(step2);
    }

    // Método auxiliar para evitar código repetido
    private void registerCommand(String name, Object executor) {
        PluginCommand command = getCommand(name);
        if (command != null) {
            command.setExecutor((org.bukkit.command.CommandExecutor) executor);
        } else {
            getLogger().warning("O comando '/" + name + "' não foi encontrado no plugin.yml!");
        }
    }
}
