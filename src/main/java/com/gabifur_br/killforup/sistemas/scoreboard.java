package com.gabifur_br.killforup.sistemas;

import com.gabifur_br.killforup.KillForUP;
import com.gabifur_br.killforup.uteis.vari;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class scoreboard {

    public static void startScoreboard() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updateScoreboard(player);
                }
            }
        }.runTaskTimer(KillForUP.getInstance(), 0L, 20L);
    }

    public static void updateScoreboard(Player player) {
        double saldo = vari.getSaldo(player);
        int stars = Integer.parseInt(vari.getStars(player));
        int nivel = Integer.parseInt(vari.getLevel(player));
        int xp = Integer.parseInt(vari.getXP(player));
        int xpNecessario = getXPForNextLevel(nivel);
        String tag = vari.getTag(player);
        String saldoFormatted = vari.formatSaldo(saldo);

        player.setPlayerListName(tag + " " + player.getName());

        // Criar novo scoreboard
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("KillForUP", "dummy", "§6§lKillForUP");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore("§eSaldo: §f" + saldoFormatted).setScore(5);
        objective.getScore("§eEstrelas: §f" + stars).setScore(4);
        objective.getScore("§eNível: §f" + nivel).setScore(3);
        objective.getScore("§eXP: §f" + xp + " / " + xpNecessario).setScore(2);
        objective.getScore("§eProgresso: " + vari.gerarBarraProgresso(xp, xpNecessario, 20)).setScore(1);

        player.setScoreboard(scoreboard);
    }

    private static int getXPForNextLevel(int nivel) {
        return nivel * 100;
    }
}
