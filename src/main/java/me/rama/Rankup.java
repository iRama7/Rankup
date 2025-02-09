package me.rama;

import me.rama.config.Config;
import me.rama.ranks.Command;
import me.rama.ranks.Rank;
import me.rama.ranks.requirements.PlaceholderRequirement;
import me.rama.ranks.requirements.Requirement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.Bukkit.getConsoleSender;

public final class Rankup extends JavaPlugin {

    private Rankup main;

    private List<Rank> ranks;


    private Config settings;

    @Override
    public void onEnable() {
        main = this;

        settings = new Config(this);

        loadPaPi();

        loadRanks();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadPaPi(){

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            log("&fPlaceholderAPI &adetected.", false);
        }else{
            log("&4[ERROR] &fPlaceholderAPI &cnot found, disabling plugin...", false);
            Bukkit.getPluginManager().disablePlugin(this);
        }

    }

    public void log(String message, boolean isDebug) {

        String prefix = colorized("&7[&bRankup&7] &r");
        message = colorized(message);

        if (isDebug) {
            if (settings.getDebugMode()) {
                prefix = colorized("&7[&bRankup&7] &e[DEBUG] &r");
                getConsoleSender().sendMessage(prefix + message);
            }
        } else {
            getConsoleSender().sendMessage(prefix + colorized(message));
        }


    }

    public String colorized(String s) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            String hexCode = s.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("&").append(c);
            }

            s = s.replace(hexCode, builder.toString());
            matcher = pattern.matcher(s);
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public Config getSettings(){
        return settings;
    }

    private void loadRanks(){

        for(String rank_section : settings.getRanks()){

            boolean isDefault = Integer.parseInt(rank_section) == 0;

            Rank rank = new Rank(settings.getID(rank_section),
                    settings.getPermission(rank_section),
                    getRequirements(rank_section),
                    getCommands(rank_section, "up"),
                    getCommands(rank_section, "down"),
                    isDefault);

            ranks.add(rank);
        }

    }

    private List<Requirement> getRequirements(String rank_section){

        List<Requirement> requirements = new ArrayList<>();

        for(String req_section : settings.getRequirements(rank_section)){

            String type = settings.getReqType(rank_section, req_section);

            if(type.equals("placeholder")){
                Requirement requirement = new PlaceholderRequirement(settings.getReqVariable(rank_section, req_section),
                        settings.getReqReturnType(rank_section, req_section),
                        settings.getReqRequirement(rank_section, req_section));
                requirements.add(requirement);
            }

        }

        return requirements;

    }

    private List<Command> getCommands(String rank_section, String type){

        List<Command> commands = new ArrayList<>();

        for(String command_string : settings.getCommands(rank_section, type)) {
            Command command = new Command(command_string);
            commands.add(command);
        }

        return commands;

    }


}
