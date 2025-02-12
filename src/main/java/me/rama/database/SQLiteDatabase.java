package me.rama.database;

import me.rama.Rankup;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDatabase extends Database{

    public SQLiteDatabase(String path, Rankup main) throws SQLException {
        super(main);
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS ranks
                    (player TEXT PRIMARY KEY,
                     rank_index INTEGER NOT NULL)
                    """);
        }
    }

}
