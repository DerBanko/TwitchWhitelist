package tv.banko.twitchwhitelist.whitelist;

import java.util.UUID;

public class WhitelistUser {

    private final UUID uuid;
    private final String twitchName;
    private final String id;
    private final long timestamp;
    private final WhitelistType type;

    public WhitelistUser(UUID uuid, String twitchName, String id, long timestamp, WhitelistType type) {
        this.uuid = uuid;
        this.twitchName = twitchName;
        this.id = id;
        this.timestamp = timestamp;
        this.type = type;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getTwitchName() {
        return twitchName;
    }

    public String getID() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public WhitelistType getType() {
        return type;
    }
}
