package tv.banko.twitchwhitelist.whitelist;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import tv.banko.twitchwhitelist.TwitchWhitelist;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Whitelist {

    private final File file;
    private final YamlConfiguration config;

    private final Map<UUID, WhitelistUser> map;

    public Whitelist() {
        File dir = TwitchWhitelist.getInstance().getDataFolder();

        if(!dir.exists()) {
            dir.mkdirs();
        }

        file = new File(dir, "whitelist.yml");

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

        map = new HashMap<>();

        load();
    }

    public void addWhitelist(WhitelistUser user) {
        if(map.containsKey(user.getUUID())) {
            return;
        }

        map.put(user.getUUID(), user);
    }

    public void removeWhitelist(UUID uuid) {
        if(!map.containsKey(uuid)) {
            return;
        }

        Player player = Bukkit.getPlayer(uuid);

        if(player != null) {
            player.kickPlayer("§cYou are no longer whitelisted.");
        }

        map.remove(uuid);
    }

    public boolean isWhitelisted(UUID uuid) {
        return map.containsKey(uuid);
    }

    public List<UUID> getUUIDs() {
        return new ArrayList<>(map.keySet());
    }

    public WhitelistUser getWhitelistUser(UUID uuid) {
        return map.getOrDefault(uuid, null);
    }

    public boolean hasTwitchWhitelist(String id) {
        for(WhitelistUser user : map.values()) {
            if(user.getID().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public void load() {
        List<String> uuids = new ArrayList<>();

        if(config.contains("uuids")) {
            uuids.addAll(config.getStringList("uuids"));
        }

        for(String uuidString : uuids) {
            try {
                String id = config.getString(uuidString + ".id");
                long timestamp = config.getLong(uuidString + ".timestamp");
                String twitch = config.getString(uuidString + ".twitch");
                WhitelistType type = WhitelistType.valueOf(config.getString(uuidString + ".type"));
                UUID uuid = UUID.fromString(uuidString);

                map.put(uuid, new WhitelistUser(uuid, twitch, id, timestamp, type));
            } catch (Exception ignored) { }
        }
    }

    public void save() {
        for(UUID uuid : map.keySet()) {

            WhitelistUser user = map.get(uuid);

            config.set(uuid + ".id", user.getID());
            config.set(uuid + ".timestamp", user.getTimestamp());
            config.set(uuid + ".twitch", user.getTwitchName());
            config.set(uuid + ".type", user.getType().name());
        }

        List<String> uuids = new ArrayList<>();

        for(UUID uuid : map.keySet()) {
            uuids.add(uuid.toString());
        }

        config.set("uuids", uuids);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
