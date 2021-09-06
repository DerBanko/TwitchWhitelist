package tv.banko.twitchwhitelist.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tv.banko.twitchwhitelist.TwitchWhitelist;
import tv.banko.twitchwhitelist.whitelist.WhitelistType;
import tv.banko.twitchwhitelist.whitelist.WhitelistUser;

import java.util.Date;
import java.util.UUID;

public class WhitelistCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("twl.whitelist")) {
            sender.sendMessage(TwitchWhitelist.getPrefix() + "§cYou are not allowed to use this command.");
            return true;
        }

        if(args.length < 1) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "list": {

                if(args.length != 1) {
                    sender.sendMessage(TwitchWhitelist.getPrefix() + "§cUsage§8: §7/wl list");
                    break;
                }

                int playersWhitelisted = TwitchWhitelist.getInstance().getWhitelist().getUUIDs().size();

                sender.sendMessage(TwitchWhitelist.getPrefix() + "§7There " + (playersWhitelisted == 1 ? "is" : "are") +
                        " currently §6" + playersWhitelisted +
                        " player" + (playersWhitelisted == 1 ? "" : "s") + " §7whitelisted.");

                StringBuilder s = new StringBuilder(TwitchWhitelist.getPrefix() + "§6Players§8: ");
                boolean comma = false;

                for(UUID uuid : TwitchWhitelist.getInstance().getWhitelist().getUUIDs()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                    s.append("§7").append(offlinePlayer.getName());

                    if(comma) {
                        s.append("§8, ");
                    }

                    comma = true;
                }

                sender.sendMessage(s.toString());
                break;
            }
            case "add": {
                if(args.length != 2) {
                    sender.sendMessage(TwitchWhitelist.getPrefix() + "§cUsage§8: §7/wl add <Player>");
                    break;
                }

                UUID uuid = TwitchWhitelist.getInstance().getRequest().getUUID(args[1]);

                if(uuid == null) {
                    sender.sendMessage(TwitchWhitelist.getPrefix() + "§cThe player does not exist or too many requests have been made.");
                    break;
                }

                if(TwitchWhitelist.getInstance().getWhitelist().isWhitelisted(uuid)) {
                    sender.sendMessage(TwitchWhitelist.getPrefix() + "§cThe player is already whitelisted.");
                    break;
                }

                TwitchWhitelist.getInstance().getWhitelist().addWhitelist(new WhitelistUser(uuid, "", "", System.currentTimeMillis(), WhitelistType.INGAME));
                sender.sendMessage(TwitchWhitelist.getPrefix() + "§7The player §6" + args[1] + " §7has been successfully whitelisted.");
                break;
            }
            case "remove": {
                if(args.length != 2) {
                    sender.sendMessage(TwitchWhitelist.getPrefix() + "§cUsage§8: §7/wl remove <Player>");
                    break;
                }

                UUID uuid = TwitchWhitelist.getInstance().getRequest().getUUID(args[1]);

                if(uuid == null) {
                    sender.sendMessage(TwitchWhitelist.getPrefix() + "§cThe player does not exist or too many requests have been made.");
                    break;
                }

                if(!TwitchWhitelist.getInstance().getWhitelist().isWhitelisted(uuid)) {
                    sender.sendMessage(TwitchWhitelist.getPrefix() + "§cThe player is not whitelisted.");
                    break;
                }

                TwitchWhitelist.getInstance().getWhitelist().removeWhitelist(uuid);
                sender.sendMessage(TwitchWhitelist.getPrefix() + "§7The player §6" + args[1] + " §7has been successfully removed from whitelist.");
                break;
            }
            case "user": {
                if(args.length != 2) {
                    sender.sendMessage(TwitchWhitelist.getPrefix() + "§cUsage§8: §7/wl user <Player>");
                    break;
                }

                UUID uuid = TwitchWhitelist.getInstance().getRequest().getUUID(args[1]);

                if(uuid == null) {
                    sender.sendMessage(TwitchWhitelist.getPrefix() + "§cThe player does not exist or too many requests have been made.");
                    break;
                }

                if(!TwitchWhitelist.getInstance().getWhitelist().isWhitelisted(uuid)) {
                    sender.sendMessage(TwitchWhitelist.getPrefix() + "§cThe player is not whitelisted.");
                    break;
                }

                WhitelistUser user = TwitchWhitelist.getInstance().getWhitelist().getWhitelistUser(uuid);

                sender.sendMessage(TwitchWhitelist.getPrefix() + "§7Data of §6" + args[1] + "§8:");
                sender.sendMessage("§8- §7UUID§8: §6" + user.getUUID());
                sender.sendMessage("§8- §7Twitch Name§8: §6" + user.getTwitchName());
                sender.sendMessage("§8- §7Twitch ID§8: §6" + user.getID());
                sender.sendMessage("§8- §7Whitelisted on§8: §6" + new Date(user.getTimestamp()));
                sender.sendMessage("§8- §7Whitelist type§8: §6" + user.getType().name());
                break;
            }
            default: {
                sendHelp(sender);
                break;
            }
        }
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(TwitchWhitelist.getPrefix() + "§6/wl [help]§8: §fShows help");
        sender.sendMessage(TwitchWhitelist.getPrefix() + "§6/wl list§8: §fShows the whitelisted players");
        sender.sendMessage(TwitchWhitelist.getPrefix() + "§6/wl add <Player>§8: §fAdd player to whitelist");
        sender.sendMessage(TwitchWhitelist.getPrefix() + "§6/wl remove <Player>§8: §fRemove player from whitelist");
        sender.sendMessage(TwitchWhitelist.getPrefix() + "§6/wl user <Player>§8: §fShows the data of player");
    }
}
