package me.rama.commands;

import me.rama.Rankup;
import me.rama.ranks.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
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

                        player.sendMessage(main.colorized(main.getSettings().getRankupSuccessMessage(next_rank)));

                    } else {
                        player.sendMessage(main.colorized(main.getSettings().getRankupFailMessage()));
                    }

                }

            }

        }

        if(args.length == 1 && args[0].equals("reload")){



        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        return null;
    }
}
