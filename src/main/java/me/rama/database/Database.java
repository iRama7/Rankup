package me.rama.database;

import me.rama.Rankup;
import me.rama.ranks.Rank;

import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Database {

    protected Connection connection;
    protected Rankup main;

    public Database(Rankup main) {
        this.main = main;
    }

    public void addPlayer(Player player, Rank rank) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ranks (player, rank_index) VALUES (?, ?)")) {
            preparedStatement.setString(1, player.getName());
            preparedStatement.setInt(2, rank.getIndex());
            preparedStatement.executeUpdate();
        }
    }

    public void updatePlayer(Player player, Rank rank) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE ranks SET rank_index = ? WHERE player = ?");
        statement.setInt(1, rank.getIndex());
        statement.setString(2, player.getName());

        statement.executeUpdate();

        statement.close();
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }


    public Rank getRank(Player player) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT * FROM ranks");

        ResultSet resultSet = statement.executeQuery();

        Rank rank = null;

        while (resultSet.next()) {
            String player_name = resultSet.getString("player");

            if(player.getName().equals(player_name)) {
                rank = main.getRank(resultSet.getInt("rank_index"));
            }
        }

        return rank;

    }

}
