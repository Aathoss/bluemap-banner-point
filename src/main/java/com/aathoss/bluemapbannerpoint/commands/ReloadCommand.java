package com.aathoss.bluemapbannerpoint.commands;

import com.aathoss.bluemapbannerpoint.BlueMapBannerPoint;
import com.aathoss.bluemapbannerpoint.utils.Messages;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends BaseCommand {

    public ReloadCommand(BlueMapBannerPoint plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            sendPermissionError(sender);
            return true;
        }

        plugin.getConfigManager().reloadConfig();
        sender.sendMessage(Messages.get("commands.reload.success"));
        plugin.getLogger().info(Messages.get("commands.reload.log", new Object[]{sender.getName()}));
        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return "bluemapbannerpoint.admin";
    }

    @Override
    public String getDescription() {
        return Messages.get("commands.reload.description");
    }

    @Override
    public String getUsage() {
        return Messages.get("commands.reload.usage");
    }
}
