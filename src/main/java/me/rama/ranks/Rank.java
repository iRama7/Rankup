package me.rama.ranks;

import me.rama.ranks.requirements.Requirement;

import java.util.List;

public class Rank {

    private final String id;
    private final String permission;
    private final List<Requirement> requirements;
    private final List<Command> up_commands;
    private final List<Command> down_commands;
    private final boolean isDefault;


    public Rank(String id, String permission, List<Requirement> requirements, List<Command> upCommands, List<Command> downCommands, boolean isDefault) {
        this.id = id;
        this.permission = permission;
        this.requirements = requirements;
        up_commands = upCommands;
        down_commands = downCommands;
        this.isDefault = isDefault;
    }







}
