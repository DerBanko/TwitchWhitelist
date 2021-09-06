package tv.banko.twitchwhitelist.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import tv.banko.twitchwhitelist.TwitchWhitelist;

public class PlayerLogin implements Listener {

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {

        if(event.getUniqueId() == null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, "§cCould not verify your account. §7Try again");
            return;
        }

        if(!TwitchWhitelist.getInstance().getWhitelist().isWhitelisted(event.getUniqueId())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, "§cYou are not whitelisted.");
            return;
        }

        event.allow();
    }

}
