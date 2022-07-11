package net.purplez.ffa.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.bukkit.configuration.ConfigurationSection;

public class MySQLConnectorClient {

    private ConfigurationSection configurationSection;
    private String hostName, database, userName, password;
    private Connection connection;

    public MySQLConnectorClient(final ConfigurationSection configurationSection) {
        this.configurationSection = configurationSection;

        this.hostName = configurationSection.getString("host-name") + ":" + configurationSection.getInt("port");
        this.database = configurationSection.getString("database");
        this.userName = configurationSection.getString("user-name");
        this.password = configurationSection.getString("password");
    }

    public Connection getMySQLInstance() {
        try {
            if (this.connection != null && !this.connection.isClosed()) return this.connection;

            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://" + this.hostName + "/" + this.database + "?autoReconnect=true",
                    this.userName,
                    this.password
            );
        } catch (final ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return this.connection;
    }

    public void close() {
        try {
            if (this.connection != null && !this.connection.isClosed()) this.connection.close();
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

}
