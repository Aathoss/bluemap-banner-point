package com.aathoss.bluemapbannerpoint.commands;

import com.aathoss.bluemapbannerpoint.BlueMapBannerPoint;
import com.aathoss.bluemapbannerpoint.utils.Messages;
import org.bukkit.command.CommandSender;

public abstract class BaseCommand {

    protected final BlueMapBannerPoint plugin;

    public BaseCommand(BlueMapBannerPoint plugin) {
        this.plugin = plugin;
    }

    /**
     * Exécute la commande
     */
    public abstract boolean execute(CommandSender sender, String[] args);

    /**
     * Retourne les suggestions pour l'autocomplétion
     */
    public abstract java.util.List<String> getTabCompletions(CommandSender sender, String[] args);

    /**
     * Retourne le nom de la commande
     */
    public abstract String getName();

    /**
     * Retourne la permission requise
     */
    public abstract String getPermission();

    /**
     * Retourne la description de la commande
     */
    public abstract String getDescription();

    /**
     * Retourne l'usage de la commande
     */
    public abstract String getUsage();

    /**
     * Vérifie si le sender a la permission
     */
    protected boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    /**
     * Envoie un message d'erreur de permission
     */
    protected void sendPermissionError(CommandSender sender) {
        sender.sendMessage(Messages.get("errors.permission"));
    }
}
