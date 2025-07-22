package com.aathoss.bluemapbannerpoint.commands;

import com.aathoss.bluemapbannerpoint.BlueMapBannerPoint;
import com.aathoss.bluemapbannerpoint.utils.Messages;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.List;

public class ReloadMarkersCommand extends BaseCommand {

    public ReloadMarkersCommand(BlueMapBannerPoint plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            sendPermissionError(sender);
            return true;
        }

        plugin.getBlueMapManager().reloadMarkers();
        sender.sendMessage(Messages.get("commands.reload_markers.success"));
        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "reload-markers";
    }

    @Override
    public String getPermission() {
        return "bluemapbannerpoint.admin";
    }

    @Override
    public String getDescription() {
        return Messages.get("commands.reload_markers.description");
    }

    @Override
    public String getUsage() {
        return Messages.get("commands.reload_markers.usage");
    }
}
