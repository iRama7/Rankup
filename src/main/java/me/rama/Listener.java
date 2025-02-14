package me.rama;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class Listener implements org.bukkit.event.Listener {

    private final Rankup main;

    public Listener(Rankup main){
        this.main = main;
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) throws SQLException {

        if(main.getDatabase().getRank(e.getPlayer()) == null){
            main.getDatabase().addPlayer(e.getPlayer(), main.getRank(0));
            if(main.getSettings().getDebugMode()){
                main.log("&eAdding default rank to new player &f" + e.getPlayer().getName(), true);
            }
        }

    }

}
