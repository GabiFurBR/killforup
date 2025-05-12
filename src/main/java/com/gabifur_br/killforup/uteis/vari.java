package com.gabifur_br.killforup.uteis;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;

import ch.njol.skript.variables.Variables;

public class vari {
    public static void enviarStatus(Player player, double saldo, int stars, int nivel, int xp, int xpNecessario) {
        player.sendMessage("§6Seu status atual:");
        player.sendMessage("§eNome: §f" + player.getName());
        player.sendMessage("§eSaldo: §f" + formatSaldo(saldo) + " moedas");
        player.sendMessage("§eEstrelas: §f" + stars);
        player.sendMessage("§eNível: §f" + nivel);
        player.sendMessage("§eXP: §f" + xp + " / " + xpNecessario + " XP");
        player.sendMessage("§eProgresso: §f" + gerarBarraProgresso(xp, xpNecessario, 20));
    }

    // Método para gerar a barra de progresso de XP
    public static String gerarBarraProgresso(int atual, int total, int tamanho) {
        if (total <= 0) total = 1; // Evitar divisão por zero
        int preenchido = (int) ((double) atual / total * tamanho);
        return "▮".repeat(Math.max(0, preenchido)) + "▯".repeat(Math.max(0, tamanho - preenchido));
    }
    
    public static double getSaldo(Player player) {
        String saldoVar = "saldo::" + player.getUniqueId();
        Object saldoObj = Variables.getVariable(saldoVar, null, false);

        try {
            return saldoObj != null ? Double.parseDouble(saldoObj.toString()) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static String getTag(Player player) {
        String tagVar = "tag::" + player.getUniqueId();
        Object tagObj = Variables.getVariable(tagVar, null, false);
        return tagObj != null ? tagObj.toString() : "[Sem Tag]";
    }

    public static String getLevel(Player player) {
        String lvlVar = "nivel::" + player.getName();
        Object lvlObj = Variables.getVariable(lvlVar, null, false);
        return lvlObj != null ? lvlObj.toString() : "[Sem Nivel]";
    }

    // NOVO: Método para formatar o saldo abreviado
    public static String formatSaldo(double saldo) {
        String[] suffixes = {"", "K", "M", "B", "T", "Q", "QQ", "S", "SS", "O", "N", "D", "UD", "DD", "TD", "QD", "QTD", "SD", "SPD", "OD", "ND", "VD"};
        int index = 0;
        while (saldo >= 1000 && index < suffixes.length - 1) {
            saldo /= 1000.0;
            index++;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(saldo) + suffixes[index];
    }

    // NOVO: Método para obter as estrelas do jogador
    public static String getStars(Player player) {
        String starsVar = "stars::" + player.getUniqueId();  // Supondo que você armazene as estrelas dessa forma
        Object starsObj = Variables.getVariable(starsVar, null, false);
        return starsObj != null ? starsObj.toString() : "0";  // Retorna 0 caso não tenha estrelas
    }

    // NOVO: Método para obter o XP do jogador
    public static String getXP(Player player) {
        String xpVar = "xp::" + player.getUniqueId();  // Supondo que você armazene o XP dessa forma
        Object xpObj = Variables.getVariable(xpVar, null, false);
        return xpObj != null ? xpObj.toString() : "0";  // Retorna 0 caso não tenha XP
    }
}
