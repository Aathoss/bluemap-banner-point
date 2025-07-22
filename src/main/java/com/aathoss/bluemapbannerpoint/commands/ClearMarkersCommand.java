package com.aathoss.bluemapbannerpoint.commands;

import com.aathoss.bluemapbannerpoint.BlueMapBannerPoint;
import com.aathoss.bluemapbannerpoint.utils.Messages;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClearMarkersCommand extends BaseCommand {

    public ClearMarkersCommand(BlueMapBannerPoint plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            sendPermissionError(sender);
            return true;
        }

        if (args.length < 2 || !args[1].equalsIgnoreCase("confirm")) {
            sender.sendMessage("§c⚠️ ATTENTION: Cette action supprime définitivement tous les marqueurs !");
            sender.sendMessage("§cUtilisez /bmp clear-markers confirm pour confirmer.");
            return true;
        }

        plugin.getBlueMapManager().clearAllMarkers();
        sender.sendMessage(Messages.get("commands.clear_markers.success"));
        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Arrays.asList("confirm");
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "clear-markers";
    }

    @Override
    public String getPermission() {
        return "bluemapbannerpoint.admin";
    }

    @Override
    public String getDescription() {
        return Messages.get("commands.clear_markers.description");
    }

    @Override
    public String getUsage() {
        return Messages.get("commands.clear_markers.usage");
    }
}
