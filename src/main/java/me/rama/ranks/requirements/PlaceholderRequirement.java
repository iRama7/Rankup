package me.rama.ranks.requirements;

import me.clip.placeholderapi.PlaceholderAPI;
import me.rama.Rankup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlaceholderRequirement extends Requirement{

    public PlaceholderRequirement(String variable, String returnType, String requirement, Rankup main) {
        super(variable, returnType, requirement, main);
    }

    @Override
    public boolean eval(Player player) {
        String translatedVariable = PlaceholderAPI.setPlaceholders(player, variable);
        Bukkit.getLogger().info(translatedVariable + " " + variable);

        switch (return_type){
            case "integer":

                try {
                    int variableInt = Integer.parseInt(translatedVariable);
                    String[] split = requirement.split(";");

                    if (split.length != 2) return false;

                    String operator = split[0];
                    int value = Integer.parseInt(split[1]);

                    main.log(variableInt + " " + operator + " " + value, true);

                    return switch (operator) {
                        case "<" -> variableInt < value;
                        case "<=" -> variableInt <= value;
                        case ">" -> variableInt > value;
                        case ">=" -> variableInt >= value;
                        default -> false;
                    };
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return false;
                }

            default:
                return false;

        }

    }

}
