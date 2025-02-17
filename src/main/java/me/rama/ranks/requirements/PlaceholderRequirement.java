package me.rama.ranks.requirements;

import me.clip.placeholderapi.PlaceholderAPI;
import me.rama.Rankup;
import org.bukkit.entity.Player;

public class PlaceholderRequirement extends Requirement{

    public PlaceholderRequirement(String variable, String returnType, String requirement, Rankup main) {
        super(variable, returnType, requirement, main);
    }

    @Override
    public boolean eval(Player player) {
        String translatedVariable = PlaceholderAPI.setPlaceholders(player, variable);

        switch (return_type){
            case "money", "playtime":

                try {
                    int variableInt = Integer.parseInt(translatedVariable);

                    int value = Integer.parseInt(requirement);

                    return variableInt >= value;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return false;
                }

            default:
                return false;

        }

    }

    public String getPlayerNeed(Player player){

        String message;
        String translatedVariable = PlaceholderAPI.setPlaceholders(player, variable);

        switch(return_type){
            case "money":

                message = main.getSettings().getMoneyNeed();
                int playerMoney = Integer.parseInt(translatedVariable);
                int moneyRequirement = Integer.parseInt(requirement);

                return message.replaceAll("%money%", String.valueOf(moneyRequirement - playerMoney));

            case "playtime":

                message = main.getSettings().getPlaytimeNeed();
                int playerTime = Integer.parseInt(translatedVariable);
                int timeRequirement = Integer.parseInt(requirement);
                int secondsNeeded = timeRequirement - playerTime;

                int days = secondsNeeded / 86400;
                secondsNeeded -= days * 86400;
                int hours = secondsNeeded / 3600;
                secondsNeeded -= hours * 3600;
                int minutes = secondsNeeded / 60;
                secondsNeeded -= minutes * 60;

                return message.replaceAll("%d%", String.valueOf(days))
                        .replaceAll("%h%", String.valueOf(hours))
                        .replaceAll("%m%", String.valueOf(minutes))
                        .replaceAll("%s%", String.valueOf(secondsNeeded));
            default:
                return null;

        }

    }

}
