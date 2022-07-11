package net.purplez.ffa.database;

import net.purplez.ffa.stats.Stats;
import net.purplez.ffa.util.DoubleValue;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.plugin.Plugin;

public class PurplezFFAConnection extends DatabaseConnection {

    public PurplezFFAConnection(final Plugin plugin, final Connection connection, final String database, final String table) {
        super(plugin, connection, database, table);

        this.prepareStatement("CREATE TABLE IF NOT EXISTS _database._table(`uuid` varchar(36), `kills` int(11), `deaths` int(11));");
    }

    public void getStats(final UUID uniqueId, final Consumer<DoubleValue<Integer, Integer>> consumer) {
        this.runAsynchronously(() -> {
            try {
                final ResultSet resultSet = this.prepareStatement(
                        "SELECT kills, deaths FROM _database._table WHERE uuid = ?",
                        uniqueId.toString()
                );

                if (resultSet.next())
                    consumer.accept(new DoubleValue<>(resultSet.getInt("kills"), resultSet.getInt("deaths")));
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void createPlayer(final UUID uniqueId, final Runnable successfully) {
        this.runAsynchronously(() -> {
            this.updateStatement(
                    "INSERT INTO _database._table(`uuid`, `kills`, `deaths`) VALUES (?, ?, ?)",
                    uniqueId.toString(),
                    0,
                    0
            );
            successfully.run();
        });
    }

    public void isPlayerExists(final UUID uniqueId, final Consumer<Boolean> consumer) {
        this.runAsynchronously(() -> {
            consumer.accept(this.hasNext("SELECT uuid FROM _database._table WHERE uuid = ?", uniqueId.toString()));
        });
    }

    public void updateStats(final Stats stats, final Runnable successfully) {
        this.runAsynchronously(() -> {
            this.updateStatement(
                    "UPDATE _database._table SET kills = ?, deaths = ? WHERE uuid = ?",
                    stats.getKills(),
                    stats.getDeaths(),
                    stats.getUniqueId().toString()
            );
            successfully.run();
        });
    }
}
