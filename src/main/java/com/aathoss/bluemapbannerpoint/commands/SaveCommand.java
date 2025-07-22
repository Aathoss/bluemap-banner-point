package com.aathoss.bluemapbannerpoint.commands;

import com.aathoss.bluemapbannerpoint.BlueMapBannerPoint;
import com.aathoss.bluemapbannerpoint.utils.Messages;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.List;

public class SaveCommand extends BaseCommand {

    public SaveCommand(BlueMapBannerPoint plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            sendPermissionError(sender);
            return true;
        }

        plugin.getBlueMapManager().saveMarkers();
        sender.sendMessage(Messages.get("commands.save.success"));
        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "save";
    }

    @Override
    public String getPermission() {
        return "bluemapbannerpoint.admin";
    }

    @Override
    public String getDescription() {
        return Messages.get("commands.save.description");
    }

    @Override
    public String getUsage() {
        return Messages.get("commands.save.usage");
    }
}
