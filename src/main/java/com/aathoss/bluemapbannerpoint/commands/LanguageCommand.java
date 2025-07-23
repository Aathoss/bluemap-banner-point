package com.aathoss.bluemapbannerpoint.commands;

import com.aathoss.bluemapbannerpoint.BlueMapBannerPoint;
import com.aathoss.bluemapbannerpoint.utils.Messages;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LanguageCommand extends BaseCommand {

    public LanguageCommand(BlueMapBannerPoint plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            sendPermissionError(sender);
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(Messages.get("commands.language.current", new Object[]{plugin.getConfigManager().getLanguage()}));
            sender.sendMessage(Messages.get("commands.language.usage"));
            return true;
        }

        String language = args[0].toLowerCase();
        if (!language.equals("fr") && !language.equals("en")) {
            sender.sendMessage(Messages.get("commands.language.invalid"));
            return true;
        }

        // Afficher la langue actuelle pour confirmation
        String currentLanguage = plugin.getConfigManager().getLanguage();
        sender.sendMessage(Messages.get("commands.language.current", new Object[]{currentLanguage}));

        // Changer la langue
        plugin.getConfigManager().setLanguage(language);

        // Afficher le message de confirmation avec la nouvelle langue
        String changedMessage = Messages.get("commands.language.changed", new Object[]{language});
        sender.sendMessage(changedMessage);

        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("fr", "en");
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "language";
    }

    @Override
    public String getPermission() {
        return "bluemapbannerpoint.admin";
    }

    @Override
    public String getDescription() {
        return Messages.get("commands.language.description");
    }

    @Override
    public String getUsage() {
        return Messages.get("commands.language.usage");
    }
}
