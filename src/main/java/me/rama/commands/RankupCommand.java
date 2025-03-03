package me.rama.commands;

import me.rama.Rankup;
import me.rama.ranks.Rank;
import me.rama.ranks.requirements.Requirement;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;


public class RankupCommand implements TabExecutor {

    private final Rankup main;

    public RankupCommand(Rankup main){
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if(args.length == 0 && sender instanceof Player player){

            if(!player.hasPermission(main.getSettings().getRankupPermission())){
                return false;
            }

            Rank player_rank;
            Rank next_rank;
            try {
                player_rank = main.getDatabase().getRank(player);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if(player_rank == null){
                player.sendMessage(main.colorized(main.getSettings().getErrorMessage()));
            }else {

                if(player_rank.isLast()){
                    player.sendMessage(main.colorized(main.getSettings().getLastRankMessage()));
                }else{
                    try {
                        next_rank = main.getNextRank(player);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    boolean eval = next_rank.eval(player);

                    if (eval) {
                        next_rank.rankup(player);

                        try {
                            main.getDatabase().updatePlayer(player, next_rank);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                        player.sendMessage(main.colorized(main.getSettings().getRankupSuccessMessage(next_rank)));

                        if(main.getSettings().broadcastRankMessage()){
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendMessage(main.colorized(main.getSettings().getRankupBroadcastMessage(player, next_rank)));
                            }
                        }

                    } else {
                        for(String m : main.getSettings().getRankupFail()){
                            if(m.contains("%requirements%")){
                                for(Requirement r : next_rank.getRequirements()){
                                    if(!r.eval(player)) {
                                        player.sendMessage(main.colorized(r.getPlayerNeed(player)));
                                    }
                                }
                            }else{
                                player.sendMessage(main.colorized(m.replaceAll("%rank%", next_rank.getDisplay())));
                            }
                        }

                    }

                }

            }

        }

        if(args.length == 1 && args[0].equals("reload")){

            main.reload();
            sender.sendMessage("Reloaded.");

        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        return null;
    }
}
