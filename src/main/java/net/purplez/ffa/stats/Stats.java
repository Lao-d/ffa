package net.purplez.ffa.stats;

import net.purplez.ffa.PurplezFFA;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@AllArgsConstructor @Getter
public class Stats {

    private final UUID uniqueId;

    @Setter private int kills, deaths, killStreak;

    public Stats(final UUID uniqueId) {
        this(uniqueId, 0, 0, 0);

        PurplezFFA.getInstance().getPurplezFFAConnection().isPlayerExists(uniqueId, aBoolean -> {
            if (aBoolean) {
                PurplezFFA.getInstance().getPurplezFFAConnection().getStats(uniqueId, integerIntegerDoubleValue -> {
                    this.deaths = integerIntegerDoubleValue.getSecond();
                    this.kills = integerIntegerDoubleValue.getFirst();
                });
            } else {
                PurplezFFA.getInstance().getPurplezFFAConnection().createPlayer(uniqueId, () -> {
                    // Here you can send the player a message, call an event, no idea. This is just carried out when the player has been entered in the database.
                });
            }
        });
    }

    public double calculateKd() {
        return (double) (this.kills / this.deaths);
    }

    public void addKills(final int kills) {
        this.kills += kills;
        this.killStreak += kills;

        this.checkKillStreak();
    }

    public void addDeaths(final int deaths) {
        this.deaths += deaths;
        this.killStreak = 0;
    }

    private void checkKillStreak() {
        final Player player = Bukkit.getPlayer(this.uniqueId);

        switch (this.killStreak) {

            case 5: {
                player.getInventory().clear();
                player.getInventory().setContents(PurplezFFA.getInstance().getItemStorage().getInventory("5-kills-inventory").getContents());

                player.updateInventory();
                break;
            }

            case 10: {
                player.getInventory().clear();
                player.getInventory().setContents(PurplezFFA.getInstance().getItemStorage().getInventory("10-kills-inventory").getContents());

                player.updateInventory();
            }

        }

    }

}
