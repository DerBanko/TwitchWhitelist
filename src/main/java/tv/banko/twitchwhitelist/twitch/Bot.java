package tv.banko.twitchwhitelist.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.helix.domain.UserList;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import org.bukkit.Bukkit;
import tv.banko.twitchwhitelist.TwitchWhitelist;
import tv.banko.twitchwhitelist.config.TwitchConfig;
import tv.banko.twitchwhitelist.whitelist.WhitelistType;
import tv.banko.twitchwhitelist.whitelist.WhitelistUser;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class Bot {

    private TwitchClient twitchClient;
    private OAuth2Credential credential;
    private final String[] users;

    private final TwitchConfig config;

    public Bot() {

        config = new TwitchConfig();
        users = config.getUsers();

        setCredential();
        connect();
    }

    public void setCredential() {
        if(Objects.equals(config.getAccessToken(), "")) {
            TwitchWhitelist.getInstance().getLogger().warning("There is no access token set. Please set it to use the plugin.");
            return;
        }

        credential = new OAuth2Credential("twitch", config.getAccessToken());
    }

    public void connect() {

        if(credential == null) {
            return;
        }

        if(twitchClient != null) {
            return;
        }

        twitchClient = TwitchClientBuilder.builder()
                .withEnableChat(true)
                .withEnablePubSub(true)
                .withEnableHelix(true)
                .withDefaultEventHandler(SimpleEventHandler.class)
                .withChatAccount(credential)
                .build();

        for (String user : users) {
            twitchClient.getChat().joinChannel(user);
        }

        registerListeners();
    }

    public void disconnect() {

        if(twitchClient == null) {
            return;
        }

        twitchClient.close();
        twitchClient = null;
    }

    public void reconnect() {

        if(twitchClient == null) {
            return;
        }

        disconnect();
        connect();
    }

    private void registerListeners() {

        if(twitchClient == null) {
            return;
        }

        try {
            UserList resultList = twitchClient.getHelix().getUsers(config.getAccessToken(), null, Arrays.asList(users)).execute();
            resultList.getUsers().forEach(user -> twitchClient.getPubSub().listenForChannelPointsRedemptionEvents(credential, user.getId()));

            if (config.isWhitelistWithChannelPoints()) {
                twitchClient.getEventManager().onEvent(RewardRedeemedEvent.class, (rewardRedeemedEvent -> {

                    if (!rewardRedeemedEvent.getRedemption().getReward().getTitle().equalsIgnoreCase(config.getChannelPointsName())) {
                        return;
                    }

                    if (rewardRedeemedEvent.getRedemption().getUserInput() == null) {
                        return;
                    }

                    String user = rewardRedeemedEvent.getRedemption().getUser().getDisplayName();
                    String id = rewardRedeemedEvent.getRedemption().getUser().getId();
                    String playerName = rewardRedeemedEvent.getRedemption().getUserInput();

                    UUID uuid = TwitchWhitelist.getInstance().getRequest().getUUID(playerName);

                    if (uuid == null) {
                        twitchClient.getChat().sendPrivateMessage(user, "The Minecraft Account \"" + playerName + "\" does not exist!");
                        Bukkit.getLogger().log(Level.INFO, "(CHANNELPOINTS) Twitch User " + user + " [ID: " + id + "] whitelisted \"" +
                                playerName + "\" but does not exist.");
                        return;
                    }

                    if(TwitchWhitelist.getInstance().getWhitelist().hasTwitchWhitelist(id)) {
                        twitchClient.getChat().sendPrivateMessage(user, "You have already whitelisted an account. " +
                                "Please tell the administration to unwhitelist your minecraft account.");
                        Bukkit.getLogger().log(Level.INFO, "(CHANNELPOINTS) Twitch User " + user + " [ID: " + id + "] has already whitelisted " +
                                "an account.");
                        return;
                    }

                    TwitchWhitelist.getInstance().getWhitelist().addWhitelist(new WhitelistUser(uuid, user, id, System.currentTimeMillis(), WhitelistType.CHANNEL_POINTS));
                    twitchClient.getChat().sendPrivateMessage(user, "You have been successfully whitelisted with \"" + playerName + "\"!");
                    Bukkit.getLogger().log(Level.INFO, "(CHANNELPOINTS) Twitch User " + user + " [ID: " + id + "] successfully whitelisted \"" +
                            playerName + "\".");
                }));
            }

            if (config.isWhitelistWithCommand()) {
                twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, event -> {

                    String[] args = event.getMessage().split(" ");

                    if (!args[0].equalsIgnoreCase(config.getCommand())) {
                        return;
                    }

                    if (args.length != 2) {
                        return;
                    }

                    String user = event.getUser().getName();
                    String id = event.getUser().getId();
                    String playerName = args[1];

                    UUID uuid = TwitchWhitelist.getInstance().getRequest().getUUID(playerName);

                    if (uuid == null) {
                        twitchClient.getChat().sendPrivateMessage(user, "The Minecraft Account \"" + playerName + "\" does not exist!");
                        Bukkit.getLogger().log(Level.INFO, "(COMMAND) Twitch User " + user + " [ID: " + id + "] whitelisted \"" +
                                playerName + "\" but does not exist.");
                        return;
                    }

                    if(TwitchWhitelist.getInstance().getWhitelist().hasTwitchWhitelist(id)) {
                        twitchClient.getChat().sendPrivateMessage(user, "You have already whitelisted an account. " +
                                "Please tell the administration to unwhitelist your minecraft account.");
                        Bukkit.getLogger().log(Level.INFO, "(COMMAND) Twitch User " + user + " [ID: " + id + "] has already whitelisted " +
                                "an account.");
                        return;
                    }

                    TwitchWhitelist.getInstance().getWhitelist().addWhitelist(new WhitelistUser(uuid, user, id, System.currentTimeMillis(), WhitelistType.COMMAND));
                    twitchClient.getChat().sendPrivateMessage(user, "You have been successfully whitelisted with \"" + playerName + "\"!");
                    Bukkit.getLogger().log(Level.INFO, "(COMMAND) Twitch User " + user + " [ID: " + id + "] successfully whitelisted \"" +
                            playerName + "\".");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        config.save();
        disconnect();
    }

    public TwitchConfig getConfig() {
        return config;
    }
}