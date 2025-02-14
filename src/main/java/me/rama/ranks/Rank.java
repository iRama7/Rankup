package me.rama.ranks;

import me.rama.Rankup;
import me.rama.ranks.requirements.Requirement;
import org.bukkit.entity.Player;

import java.util.List;

public class Rank {

    private final int index;
    private final String display;
    private final List<Requirement> requirements;
    private final List<Command> up_commands;
    private final List<Command> down_commands;
    private final Rankup main;


    public Rank(int index, String display, String permission, List<Requirement> requirements, List<Command> upCommands, List<Command> downCommands, Rankup main) {
        this.index = index;
        this.display = display;
        this.requirements = requirements;
        this.main = main;
        up_commands = upCommands;
        down_commands = downCommands;

    }

    public boolean eval(Player p){

        boolean eval = true;

        for(int i = 0; i < requirements.size() && eval; i++){
            eval = requirements.get(i).eval(p);
        }

        return eval;

    }

    public void rankup(Player player){

        for(Command command : up_commands){
            command.execute(player);
        }


    }

    public void rankdown(Player player){

        for(Command command : down_commands){
            command.execute(player);
        }

    }

    public boolean isDefault() {
        return index == 0;
    }

    public boolean isLast() {
        return index == main.getRanks().size() - 1;
    }

    public int getIndex() {
        return index;
    }

    public String getDisplay() {
        return display;
    }
}
