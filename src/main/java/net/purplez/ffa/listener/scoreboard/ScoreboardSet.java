package net.purplez.ffa.listener.scoreboard;

import net.purplez.ffa.stats.Stats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoreboardSet {


    public static void setBoard(Player p) {
        final Stats stats = (Stats) p.getMetadata("stats").get(0).value();
        String uuid = p.getUniqueId().toString();
        Date today = new Date();
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
        String date = DATE_FORMAT.format(today);
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("aaa", "bbb");


        obj.setDisplayName("§d§lFFA §7(§fBETA§7)");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score Placeholder0 = obj.getScore(ChatColor.DARK_PURPLE + " ");
        Score Placeholder6 = obj.getScore("§fSpieler: §e" + Bukkit.getOnlinePlayers().size());
        Score Placeholder7 = obj.getScore("§fKills: §e" + stats.getKills() );
        Score Placeholder8 = obj.getScore(ChatColor.BLACK + "");
        Score Placeholder9 = obj.getScore("§7" + date);
        Score Placeholder10 = obj.getScore("§5purplez.net");



        Placeholder0.setScore(6);
        Placeholder6.setScore(5);
        Placeholder7.setScore(4);
        Placeholder8.setScore(3);
        Placeholder9.setScore(2);
        Placeholder10.setScore(1);


        p.setScoreboard(board);

    }


}
