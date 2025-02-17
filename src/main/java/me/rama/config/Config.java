package me.rama.config;

import me.rama.Rankup;
import me.rama.ranks.Rank;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class Config {

    private final Rankup main;
    private FileConfiguration config;

    private final boolean debug_mode;

    public Config(Rankup main){

        this.main = main;
        config = main.getConfig();

        debug_mode = config.getBoolean("settings.debug-mode");

    }

    public void reload(){
        main.reloadConfig();
        config = main.getConfig();
    }

    public boolean getDebugMode(){
        return debug_mode;
    }

    public Set<String> getRanks(){
        return config.getConfigurationSection("ranks").getKeys(false);
    }

    public String getDisplay(String rank){
        return config.getString("ranks." + rank + ".display");
    }

    public String getPermission(String rank){
        return config.getString("ranks." + rank + ".permission");
    }

    public Set<String> getRequirements(String rank){
        return config.getConfigurationSection("ranks." + rank + ".requirements").getKeys(false);
    }

    public List<String> getCommands(String rank, String type){
        return config.getStringList("ranks." + rank + "." + type + "_commands");
    }

    public String getReqType(String rank, String requirement){
        return config.getString("ranks." + rank + ".requirements." + requirement + ".type");
    }

    public String getReqVariable(String rank, String requirement){
        return config.getString("ranks." + rank + ".requirements." + requirement + ".variable");
    }

    public String getReqReturnType(String rank, String requirement){
        return config.getString("ranks." + rank + ".requirements." + requirement + ".return");
    }

    public String getReqRequirement(String rank, String requirement){
        return config.getString("ranks." + rank + ".requirements." + requirement + ".requirement");
    }

    public String getErrorMessage(){
        return config.getString("lang.error_message");
    }


    public String getRankupSuccessMessage(Rank rank){
        return config.getString("lang.rankup_success").replaceAll("%rank%", rank.getDisplay());
    }

    public boolean broadcastRankMessage(){
        return config.getBoolean("settings.broadcast_rank_message");
    }

    public String getRankupBroadcastMessage(Player player, Rank rank){
        return config.getString("lang.rankup_broadcast").replaceAll("%rank%", rank.getDisplay()).replaceAll("%player%", player.getName());
    }

    public String getLastRankMessage() {
        return config.getString("lang.last_rankup");
    }

    public String getRankDownError() {
        return config.getString("lang.rankdown_error");
    }

    public String getRankDownSuccess(Rank rank){
        return config.getString("lang.rankdown_success").replaceAll("%rank%", rank.getDisplay());
    }

    public String getRankupPermission(){
        return config.getString("settings.permissions.rankup");
    }

    public String getRankDownPermission(){
        return config.getString("settings.permissions.rankdown");
    }

    public String getMoneyNeed(){
        return config.getString("lang.requirements.money_need");
    }

    public String getPlaytimeNeed(){
        return config.getString("lang.requirements.playtime_need");
    }

    public List<String> getRankupFail(){
        return config.getStringList("lang.requirements.message");
    }
}
