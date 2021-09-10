package tv.banko.twitchwhitelist;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tv.banko.twitchwhitelist.commands.WhitelistCommand;
import tv.banko.twitchwhitelist.commands.WhitelistSettingsCommand;
import tv.banko.twitchwhitelist.listeners.PlayerLogin;
import tv.banko.twitchwhitelist.twitch.Bot;
import tv.banko.twitchwhitelist.utils.MojangRequest;
import tv.banko.twitchwhitelist.whitelist.Whitelist;

public final class TwitchWhitelist extends JavaPlugin {

    private static TwitchWhitelist instance;

    private Whitelist whitelist;
    private Bot bot;

    private MojangRequest request;

    public TwitchWhitelist() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.whitelist = new Whitelist();
        this.bot = new Bot();
        this.request = new MojangRequest();

        Bukkit.getPluginManager().registerEvents(new PlayerLogin(), this);

        getCommand("wl").setExecutor(new WhitelistCommand());
        getCommand("settings").setExecutor(new WhitelistSettingsCommand());
    }

    @Override
    public void onDisable() {
        whitelist.save();
        bot.stop();
    }

    public static String getPrefix() {
        return "§8[§5Twitch§lWL§8] §7";
    }

    public Whitelist getWhitelist() {
        return whitelist;
    }

    public Bot getBot() {
        return bot;
    }

    public MojangRequest getRequest() {
        return request;
    }

    public static TwitchWhitelist getInstance() {
        return instance;
    }
}
