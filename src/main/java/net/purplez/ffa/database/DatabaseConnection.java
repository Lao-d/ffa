package net.purplez.ffa.database;

import net.purplez.ffa.PurplezFFA;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.IntStream;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

public class DatabaseConnection {

    @Getter private String database, shortcut, table;
    @Getter private Connection connection;
    @Getter private Plugin plugin;

    public DatabaseConnection(final Plugin plugin, final Connection connection) {
        this.connection = connection;
        this.shortcut = "[" + plugin.getName() + "]";
        this.plugin = plugin;
    }

    public DatabaseConnection(final Plugin plugin, final Connection connection, final String database) {
        this(plugin, connection);
        this.database = database;
    }

    public DatabaseConnection(final Plugin plugin, final Connection connection, final String database, final String table) {
        this(plugin, connection, database);
        this.table = table;
    }

    public ResultSet prepareStatement(String query, final Object... parameters) {
        query = this.replaceGlobalVariables(query);

        try {
            final PreparedStatement preparedStatement = this.connection.prepareStatement(query);

            if (parameters.length > 0) {
                IntStream.rangeClosed(1, parameters.length).forEach(i -> {
                    try {
                        preparedStatement.setObject(i, parameters[i - 1]);
                    } catch (final SQLException e) {
                        e.printStackTrace();
                    }
                });
            }

            preparedStatement.execute();
            return preparedStatement.getResultSet();
        } catch (final SQLException e) {
            System.err.println(this.shortcut + " Failed statement: " + query);
            e.printStackTrace();
        }

        return null;
    }

    public void updateStatement(String query, final Object... parameters) {
        query = this.replaceGlobalVariables(query);

        try {
            final PreparedStatement preparedStatement = this.connection.prepareStatement(query);

            if (parameters.length > 0) {
                IntStream.rangeClosed(1, parameters.length).forEach(i -> {
                    try {
                        preparedStatement.setObject(i, parameters[i - 1]);
                    } catch (final SQLException e) {
                        e.printStackTrace();
                    }
                });
            }

            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            System.err.println(this.shortcut + " Failed statement: " + query);
            e.printStackTrace();
        }

    }

    public boolean hasNext(String query, final Object... parameters) {
        query = this.replaceGlobalVariables(query);

        try {
            return this.prepareStatement(query, parameters).next();
        } catch (final SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void runAsynchronously(final Runnable runnable) {
        PurplezFFA.getInstance().getExecutorService().execute(runnable);
    }

    private String replaceGlobalVariables(final String s) {
        return s.replaceAll("_database", this.database ).replaceAll("_table", this.table);
    }

}
