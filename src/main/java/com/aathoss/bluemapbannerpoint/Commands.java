package com.aathoss.bluemapbannerpoint;

import com.aathoss.bluemapbannerpoint.commands.*;
import com.aathoss.bluemapbannerpoint.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Commands implements CommandExecutor, TabCompleter {

    private final BlueMapBannerPoint plugin;
    private final Map<String, BaseCommand> commands;

    public Commands(BlueMapBannerPoint plugin) {
        this.plugin = plugin;
        this.commands = new HashMap<>();
        registerCommands();
    }

    /**
     * Enregistre toutes les commandes disponibles
     */
    private void registerCommands() {
        commands.put("reload", new ReloadCommand(plugin));
        commands.put("info", new InfoCommand(plugin));
        commands.put("debug", new DebugCommand(plugin));
        commands.put("markers", new MarkersCommand(plugin));
        commands.put("save", new SaveCommand(plugin));
        commands.put("reload-markers", new ReloadMarkersCommand(plugin));
        commands.put("clear-markers", new ClearMarkersCommand(plugin));
        commands.put("language", new LanguageCommand(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("bmp")) {
            return false;
        }

        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }

        String commandName = args[0].toLowerCase();
        BaseCommand cmd = commands.get(commandName);

        if (cmd == null) {
            sendHelpMessage(sender);
            return true;
        }

        // Créer un nouveau tableau d'arguments sans le nom de la commande
        String[] commandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, commandArgs, 0, args.length - 1);

        return cmd.execute(sender, commandArgs);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("bmp")) {
            return new ArrayList<>();
        }

        if (args.length == 1) {
            List<String> completions = new ArrayList<>();

            // Cache des permissions pour éviter les appels répétés
            boolean hasAdminPermission = sender.hasPermission("bluemapbannerpoint.admin");
            boolean hasBannerPermission = sender.hasPermission("bluemapbannerpoint.banner");

            for (BaseCommand cmd : commands.values()) {
                String permission = cmd.getPermission();
                if ((permission.equals("bluemapbannerpoint.admin") && hasAdminPermission) ||
                    (permission.equals("bluemapbannerpoint.banner") && hasBannerPermission)) {
                    completions.add(cmd.getName());
                }
            }

            return completions;
        }

        // Pour les sous-commandes
        if (args.length >= 2) {
            String commandName = args[0].toLowerCase();
            BaseCommand cmd = commands.get(commandName);

            if (cmd != null && sender.hasPermission(cmd.getPermission())) {
                // Créer un nouveau tableau d'arguments sans le nom de la commande
                String[] commandArgs = new String[args.length - 1];
                System.arraycopy(args, 1, commandArgs, 0, args.length - 1);

                return cmd.getTabCompletions(sender, commandArgs);
            }
        }

        return new ArrayList<>();
    }

    /**
     * Affiche le message d'aide principal
     */
    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(Messages.get("commands.help.title"));

        for (BaseCommand cmd : commands.values()) {
            if (sender.hasPermission(cmd.getPermission())) {
                sender.sendMessage(Messages.get("commands.help.format", new Object[]{cmd.getUsage(), cmd.getDescription()}));
            }
        }
    }
}
