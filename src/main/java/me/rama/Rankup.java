package me.rama;

import me.rama.commands.RankdownCommand;
import me.rama.commands.RankupCommand;
import me.rama.config.Config;
import me.rama.database.Database;
import me.rama.database.SQLiteDatabase;
import me.rama.ranks.Command;
import me.rama.ranks.Rank;
import me.rama.ranks.requirements.PlaceholderRequirement;
import me.rama.ranks.requirements.Requirement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.Bukkit.getConsoleSender;

public final class Rankup extends JavaPlugin {

    private List<Rank> ranks = new ArrayList<>();
    private Database database;


    private Config settings;

    @Override
    public void onEnable() {

        this.saveDefaultConfig();
        settings = new Config(this);

        loadPaPi();

        loadRanks();

        initDatabase();

        registerCommands();

        registerEvents();
    }

    @Override
    public void onDisable() {
        if (database != null) {

            try {
                database.closeConnection();
            } catch (SQLException e) {
                log("&4[ERROR] &cError closing the database connection: " + e.getMessage(), false);
                throw new RuntimeException(e);
            }

        }
    }

    public void reload(){
        settings.reload();
        ranks = new ArrayList<>();
        loadRanks();
    }

    private void registerCommands(){
        this.getCommand("rankup").setExecutor(new RankupCommand(this));
        this.getCommand("rankdown").setExecutor(new RankdownCommand(this));
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

    public List<Rank> getRanks() {
        return ranks;
    }

    private void loadRanks(){

        for(String rank_section : settings.getRanks()){

            int index = Integer.parseInt(rank_section);

            Rank rank = new Rank(index,
                    settings.getDisplay(rank_section),
                    settings.getPermission(rank_section),
                    getRequirements(rank_section),
                    getCommands(rank_section, "up"),
                    getCommands(rank_section, "down"), this);

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
                        settings.getReqRequirement(rank_section, req_section), this);
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

    public Database getDatabase() {
        return database;
    }

    private void registerEvents(){
        Bukkit.getPluginManager().registerEvents(new Listener(this), this);
    }

    private void initDatabase() {
            log("&eInitializing &fSQLite &edatabase...", false);
            try {
                database = new SQLiteDatabase(getDataFolder().getAbsolutePath() + "/ranks.db", this);
                log("&aDatabase created successfully.", false);
            } catch (SQLException e) {
                log("&4[ERROR] &cCould not create the database: &f" + e.getMessage(), false);
                getServer().getPluginManager().disablePlugin(this);
                throw new RuntimeException(e);
            }
    }

    public Rank getRank(int rank_index){

        Rank rank = null;

        for(Rank r : ranks){
            if(r.getIndex() == rank_index){
                rank = r;
            }
        }

        return rank;

    }

    public Rank getNextRank(Player player) throws SQLException {

        Rank rank = getDatabase().getRank(player);

        return ranks.get(rank.getIndex() + 1);
    }

    public Rank getPrevRank(Player player) throws SQLException {

        Rank rank = getDatabase().getRank(player);

        return ranks.get(rank.getIndex() - 1);

    }

}
