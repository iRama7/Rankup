package me.rama.commands;

import me.rama.Rankup;
import me.rama.ranks.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class RankdownCommand implements CommandExecutor {

    private final Rankup main;

    public RankdownCommand(Rankup main){
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if(args.length == 0 && sender instanceof Player player){

            if(!player.hasPermission(main.getSettings().getRankDownPermission())){
                return false;
            }

            Rank player_rank;

            try {
                player_rank = main.getDatabase().getRank(player);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            //if player rank is not first or null

            if(player_rank != null && !player_rank.isDefault()){

                //Rank down possible
                Rank prev_rank;
                try {
                    prev_rank = main.getPrevRank(player);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                //Execute rank down if not null
                if(prev_rank != null){
                    player_rank.rankdown(player);

                    try {
                        main.getDatabase().updatePlayer(player, prev_rank); //Update database
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    player.sendMessage(main.colorized(main.getSettings().getRankDownSuccess(prev_rank)));
                }else{
                    player.sendMessage(main.colorized(main.getSettings().getErrorMessage()));
                }

            }else{
                player.sendMessage(main.colorized(main.getSettings().getRankDownError()));
            }

        }

        return false;
    }
}
