package me.rama.ranks.requirements;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PlaceholderRequirement extends Requirement{

    public PlaceholderRequirement(String variable, String returnType, String requirement) {
        super(variable, returnType, requirement);
    }

    @Override
    public boolean eval(Player player) {
        variable = PlaceholderAPI.setPlaceholders(player, variable);

        switch (return_type){
            case "integer":

                try {
                    int variableInt = Integer.parseInt(variable);
                    String[] split = requirement.split(";");

                    if (split.length != 2) return false;

                    String operator = split[0];
                    int value = Integer.parseInt(split[1]);

                    return switch (operator) {
                        case "<" -> variableInt < value;
                        case "<=" -> variableInt <= value;
                        case ">" -> variableInt > value;
                        case ">=" -> variableInt >= value;
                        default -> false;
                    };
                } catch (NumberFormatException e) {
                    return false;
                }

            default:
                return false;

        }

    }

}
