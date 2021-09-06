package tv.banko.twitchwhitelist.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tv.banko.twitchwhitelist.TwitchWhitelist;

public class WhitelistSettingsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("twl.settings")) {
            sender.sendMessage(TwitchWhitelist.getPrefix() + "§cYou are not allowed to use this command.");
            return true;
        }

        if (args.length < 1) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "cpname": {

                if (args.length < 2) {
                    sender.sendMessage(TwitchWhitelist.getPrefix() + "§cUsage§8: §7/settings cpname <Channel Points Reward>");
                    break;
                }

                StringBuilder name = new StringBuilder();

                for (int i = 1; i < args.length; i++) {
                    if (i != 1) {
                        name.append(" ");
                    }
                    name.append(args[i]);
                }

                TwitchWhitelist.getInstance().getBot().getConfig().setChannelPointsName(name.toString());
                sender.sendMessage(TwitchWhitelist.getPrefix() + "§7The name of the channel points redemtion has been set to §6" + name + "§7.");
                sender.sendMessage(TwitchWhitelist.getPrefix() + "§aBot is reconnecting...");
                TwitchWhitelist.getInstance().getBot().reconnect();
                break;
            }
            case "token": {

                if (args.length != 2) {
                    sender.sendMessage(TwitchWhitelist.getPrefix() + "§cUsage§8: §7/settings token <Access token>");
                    break;
                }

                StringBuilder token = new StringBuilder();

                for (int i = 1; i < args.length; i++) {
                    token.append(args[i]);
                }

                TwitchWhitelist.getInstance().getBot().getConfig().setAccessToken(token.toString());
                sender.sendMessage(TwitchWhitelist.getPrefix() + "§7The access token has been set to §6§k" + token + "§7.");
                sender.sendMessage(TwitchWhitelist.getPrefix() + "§aBot is reconnecting...");
                TwitchWhitelist.getInstance().getBot().setCredential();
                TwitchWhitelist.getInstance().getBot().reconnect();
                break;
            }
            case "command": {

                if (args.length != 2) {
                    sender.sendMessage(TwitchWhitelist.getPrefix() + "§cUsage§8: §7/settings command <Command>");
                    break;
                }

                StringBuilder cmd = new StringBuilder();

                for (int i = 1; i < args.length; i++) {
                    cmd.append(args[i]);
                }

                TwitchWhitelist.getInstance().getBot().getConfig().setCommand(cmd.toString());
                sender.sendMessage(TwitchWhitelist.getPrefix() + "§7The command has been set to §6" + cmd + "§7.");
                sender.sendMessage(TwitchWhitelist.getPrefix() + "§aBot is reconnecting...");
                TwitchWhitelist.getInstance().getBot().reconnect();
                break;
            }
            case "users": {

                if (args.length < 2) {
                    sender.sendMessage(TwitchWhitelist.getPrefix() + "§cUsage§8: §7/settings users <Users>");
                    break;
                }

                StringBuilder users = new StringBuilder();

                for (int i = 1; i < args.length; i++) {
                    if (i != 1) {
                        users.append(" ");
                    }
                    users.append(args[i]);
                }

                TwitchWhitelist.getInstance().getBot().getConfig().setUsers(users.toString().split(" "));
                sender.sendMessage(TwitchWhitelist.getPrefix() + "§7The users has been set to §6" + users.toString().replace(" ", "§8, §6") + "§7.");
                sender.sendMessage(TwitchWhitelist.getPrefix() + "§aBot is reconnecting...");
                TwitchWhitelist.getInstance().getBot().reconnect();
                break;
            }
            case "togglecp": {

                if (args.length != 2) {
                    sender.sendMessage(TwitchWhitelist.getPrefix() + "§cUsage§8: §7/settings togglecp <true/false>");
                    break;
                }

                if (!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false")) {
                    sender.sendMessage(TwitchWhitelist.getPrefix() + "§cUsage§8: §7/settings togglecp <true/false>");
                    break;
                }

                boolean b = Boolean.parseBoolean(args[1]);

                TwitchWhitelist.getInstance().getBot().getConfig().setWhitelistWithChannelPoints(b);
                sender.sendMessage(TwitchWhitelist.getPrefix() + "§7The whitelist with channel points has been set to §6" + b + "§7.");
                sender.sendMessage(TwitchWhitelist.getPrefix() + "§aBot is reconnecting...");
                TwitchWhitelist.getInstance().getBot().reconnect();
                break;
            }
            case "togglecommand": {

                if (args.length != 2) {
                    sender.sendMessage(TwitchWhitelist.getPrefix() + "§cUsage§8: §7/settings togglecommand <true/false>");
                    break;
                }

                if (!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false")) {
                    sender.sendMessage(TwitchWhitelist.getPrefix() + "§cUsage§8: §7/settings togglecommand <true/false>");
                    break;
                }

                boolean b = Boolean.parseBoolean(args[1]);

                TwitchWhitelist.getInstance().getBot().getConfig().setWhitelistWithCommand(b);
                sender.sendMessage(TwitchWhitelist.getPrefix() + "§7The whitelist with command has been set to §6" + b + "§7.");
                sender.sendMessage(TwitchWhitelist.getPrefix() + "§aBot is reconnecting...");
                TwitchWhitelist.getInstance().getBot().reconnect();
                break;
            }
            case "reconnect": {

                if (args.length != 1) {
                    sender.sendMessage(TwitchWhitelist.getPrefix() + "§cUsage§8: §7/settings reconnect");
                    break;
                }

                sender.sendMessage(TwitchWhitelist.getPrefix() + "§aBot is reconnecting...");
                TwitchWhitelist.getInstance().getBot().reconnect();
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
        sender.sendMessage(TwitchWhitelist.getPrefix() + "§6/settings [help]§8: §fShows help");
        sender.sendMessage(TwitchWhitelist.getPrefix() + "§6/settings cpname <Channel Points Reward>§8: §fSet name of channel points reward");
        sender.sendMessage(TwitchWhitelist.getPrefix() + "§6/settings token <Access Token>§8: §fSet access token of twitch account");
        sender.sendMessage(TwitchWhitelist.getPrefix() + "§6/settings command <Command>§8: §fSet command for whitelist in twitch chat");
        sender.sendMessage(TwitchWhitelist.getPrefix() + "§6/settings users <Users>§8: §fSet twitch channels in which the bot should connect");
        sender.sendMessage(TwitchWhitelist.getPrefix() + "§6/settings togglecp <true/false>§8: §fSet if players can whitelist with channel points");
        sender.sendMessage(TwitchWhitelist.getPrefix() + "§6/settings togglecommand <true/false>§8: §fSet if players can whitelist with the command");
        sender.sendMessage(TwitchWhitelist.getPrefix() + "§6/settings reconnect§8: §fReconnect the bot");
    }
}
