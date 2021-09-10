package tv.banko.twitchwhitelist.config;

import org.bukkit.configuration.file.YamlConfiguration;
import tv.banko.twitchwhitelist.TwitchWhitelist;

import java.io.File;
import java.io.IOException;

public class TwitchConfig {

    private final File file;
    private final YamlConfiguration config;

    private String accessToken;
    private String[] users;
    private String command;
    private String channelPointsName;

    private boolean whitelistWithCommand;
    private boolean whitelistWithChannelPoints;

    public TwitchConfig() {
        File dir = TwitchWhitelist.getInstance().getDataFolder();

        if(!dir.exists()) {
            dir.mkdirs();
        }

        file = new File(dir, "config.yml");

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

        load();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getChannelPointsName() {
        return channelPointsName;
    }

    public void setChannelPointsName(String channelPointsName) {
        this.channelPointsName = channelPointsName;
    }

    public boolean isWhitelistWithCommand() {
        return whitelistWithCommand;
    }

    public void setWhitelistWithCommand(boolean whitelistWithCommand) {
        this.whitelistWithCommand = whitelistWithCommand;
    }

    public boolean isWhitelistWithChannelPoints() {
        return whitelistWithChannelPoints;
    }

    public void setWhitelistWithChannelPoints(boolean whitelistWithChannelPoints) {
        this.whitelistWithChannelPoints = whitelistWithChannelPoints;
    }

    private void load() {
        if(config.contains("twitch.accessToken")) {
            accessToken = config.getString("twitch.accessToken");
        } else {
            accessToken = "";
        }

        if(config.contains("twitch.users")) {
            users = config.getStringList("twitch.users").toArray(new String[]{});
        } else {
            users = new String[] { };
        }

        if(config.contains("twitch.command")) {
            command = config.getString("twitch.command");
        } else {
            command = "!whitelist";
        }

        if(config.contains("twitch.channelPointsName")) {
            channelPointsName = config.getString("twitch.channelPointsName");
        } else {
            channelPointsName = "Whitelist";
        }

        if(config.contains("twitch.whitelistWithCommand")) {
            whitelistWithCommand = config.getBoolean("twitch.whitelistWithCommand");
        } else {
            whitelistWithCommand = true;
        }

        if(config.contains("twitch.whitelistWithChannelPoints")) {
            whitelistWithChannelPoints = config.getBoolean("twitch.whitelistWithChannelPoints");
        } else {
            whitelistWithChannelPoints = false;
        }
    }

    public void save() {
        config.set("twitch.accessToken", accessToken);
        config.set("twitch.users", users);
        config.set("twitch.command", command);
        config.set("twitch.channelPointsName", channelPointsName);
        config.set("twitch.whitelistWithCommand", whitelistWithCommand);
        config.set("twitch.whitelistWithChannelPoints", whitelistWithChannelPoints);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
