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
            Rank player_rank = null;
            try {
                player_rank = main.getDatabase().getRank(player);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if(!player_rank.isDefault()){
                player_rank.rankdown(player);
            }else{
                //send message
            }

        }

        return false;
    }
}
