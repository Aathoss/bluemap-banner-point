package com.aathoss.bluemapbannerpoint.commands;

import com.aathoss.bluemapbannerpoint.BlueMapBannerPoint;
import com.aathoss.bluemapbannerpoint.utils.Messages;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.List;

public class InfoCommand extends BaseCommand {

    public InfoCommand(BlueMapBannerPoint plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            sendPermissionError(sender);
            return true;
        }

        sendInfoMessage(sender);
        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getPermission() {
        return "bluemapbannerpoint.admin";
    }

    @Override
    public String getDescription() {
        return Messages.get("commands.info.description");
    }

    @Override
    public String getUsage() {
        return Messages.get("commands.info.usage");
    }

    private void sendInfoMessage(CommandSender sender) {
        String version = plugin.getDescription().getVersion();
        String author = plugin.getDescription().getAuthors().get(0);

        sender.sendMessage(Messages.get("commands.info.title"));
        sender.sendMessage(Messages.get("commands.info.version", new Object[]{version}));
        sender.sendMessage(Messages.get("commands.info.author", new Object[]{author}));
        sender.sendMessage(Messages.get("commands.info.debug_mode", new Object[]{
            plugin.getConfigManager().isDebugEnabled() ? Messages.get("commands.info.enabled") : Messages.get("commands.info.disabled")}));
        sender.sendMessage(Messages.get("commands.info.auto_remove", new Object[]{
            plugin.getConfigManager().isAutoRemoveEnabled() ? Messages.get("commands.info.enabled_f") : Messages.get("commands.info.disabled_f")}));
    }
}
