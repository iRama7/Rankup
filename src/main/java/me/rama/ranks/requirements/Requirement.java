package me.rama.ranks.requirements;

import me.rama.Rankup;
import org.bukkit.entity.Player;

public abstract class Requirement {

    protected String variable;

    protected final String return_type;
    protected final String requirement;
    protected final Rankup main;

    protected Requirement(String variable, String returnType, String requirement, Rankup main) {
        this.variable = variable;
        return_type = returnType;
        this.requirement = requirement;
        this.main = main;
    }

    public abstract boolean eval(Player player);

}
