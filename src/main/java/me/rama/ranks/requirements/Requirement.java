package me.rama.ranks.requirements;

import org.bukkit.entity.Player;

public abstract class Requirement {

    protected String variable;

    protected final String return_type;
    protected final String requirement;

    protected Requirement(String variable, String returnType, String requirement) {
        this.variable = variable;
        return_type = returnType;
        this.requirement = requirement;
    }

    public abstract boolean eval(Player player);

}
