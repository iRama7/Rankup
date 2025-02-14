package me.rama.ranks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Command {

    private enum Executor {CONSOLE, PLAYER}
    private Executor executor = null;
    private final String command;

    public Command(String s){

        String[] split = s.split(";");
        String executor_string = split[0];

        if(executor_string.equalsIgnoreCase("CONSOLE")){
            executor = Executor.CONSOLE;
        }else if(executor_string.equalsIgnoreCase("PLAYER")){
            executor = Executor.PLAYER;
        }

        command = split[1];

    }

    public void execute(Player p){

        if(executor.equals(Executor.CONSOLE)){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", p.getName()));
        }else if(executor.equals(Executor.PLAYER)){
            Bukkit.dispatchCommand(p, command.replaceAll("%player%", p.getName()));
        }

    }



}
