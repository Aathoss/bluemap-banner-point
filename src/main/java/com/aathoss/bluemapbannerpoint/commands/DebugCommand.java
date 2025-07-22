package com.aathoss.bluemapbannerpoint.commands;

import com.aathoss.bluemapbannerpoint.BlueMapBannerPoint;
import com.aathoss.bluemapbannerpoint.utils.Messages;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DebugCommand extends BaseCommand {

    public DebugCommand(BlueMapBannerPoint plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            sendPermissionError(sender);
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(Messages.get("commands.debug.usage_error"));
            return true;
        }

        boolean debugEnabled = args[1].equalsIgnoreCase("on");

        // Modifier la configuration
        plugin.getConfigManager().setDebugEnabled(debugEnabled);

        sender.sendMessage(debugEnabled ? Messages.get("commands.debug.enabled") : Messages.get("commands.debug.disabled"));
        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Arrays.asList("on", "off");
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "debug";
    }

    @Override
    public String getPermission() {
        return "bluemapbannerpoint.admin";
    }

    @Override
    public String getDescription() {
        return Messages.get("commands.debug.description");
    }

    @Override
    public String getUsage() {
        return Messages.get("commands.debug.usage");
    }
}
