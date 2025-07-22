package com.aathoss.bluemapbannerpoint.commands;

import com.aathoss.bluemapbannerpoint.BlueMapBannerPoint;
import com.aathoss.bluemapbannerpoint.utils.Messages;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MarkersCommand extends BaseCommand {

    public MarkersCommand(BlueMapBannerPoint plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            sendPermissionError(sender);
            return true;
        }

        if (args.length < 2) {
            sendHelpMessage(sender);
            return true;
        }

        switch (args[1].toLowerCase()) {
            case "list":
                listAllMarkers(sender);
                break;
            case "count":
                showMarkerCount(sender);
                break;
            case "player":
                if (args.length < 3) {
                    sender.sendMessage(Messages.get("commands.markers.player_usage"));
                    return true;
                }
                listPlayerMarkers(sender, args[2]);
                break;
            case "world":
                if (args.length < 3) {
                    sender.sendMessage(Messages.get("commands.markers.world_usage"));
                    return true;
                }
                listWorldMarkers(sender, args[2]);
                break;
            default:
                sender.sendMessage(Messages.get("commands.markers.unknown_subcommand"));
                break;
        }

        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Arrays.asList("list", "count", "player", "world");
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "markers";
    }

    @Override
    public String getPermission() {
        return "bluemapbannerpoint.admin";
    }

    @Override
    public String getDescription() {
        return Messages.get("commands.markers.description");
    }

    @Override
    public String getUsage() {
        return Messages.get("commands.markers.usage");
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(Messages.get("commands.markers.help.title"));
        sender.sendMessage(Messages.get("commands.markers.help.list"));
        sender.sendMessage(Messages.get("commands.markers.help.count"));
        sender.sendMessage(Messages.get("commands.markers.help.player"));
        sender.sendMessage(Messages.get("commands.markers.help.world"));
    }

    private void listAllMarkers(CommandSender sender) {
        var markers = plugin.getBlueMapManager().getAllMarkers();
        if (markers.isEmpty()) {
            sender.sendMessage(Messages.get("commands.markers.no_markers"));
            return;
        }

        sender.sendMessage(Messages.get("commands.markers.all_markers_title", new Object[]{markers.size()}));
        for (var marker : markers) {
            sender.sendMessage(Messages.get("commands.markers.marker_format", new Object[]{
                marker.playerName, marker.markerName, marker.world, marker.x, marker.y, marker.z}));
        }
    }

    private void showMarkerCount(CommandSender sender) {
        int total = plugin.getBlueMapManager().getTotalMarkers();
        sender.sendMessage(Messages.get("commands.markers.stats_title"));
        sender.sendMessage(Messages.get("commands.markers.total", new Object[]{total}));

        // Compter par monde
        var worlds = plugin.getBlueMapManager().getMarkersByWorld();
        if (!worlds.isEmpty()) {
            sender.sendMessage(Messages.get("commands.markers.by_world"));
            for (var entry : worlds.entrySet()) {
                sender.sendMessage(Messages.get("commands.markers.world_format", new Object[]{entry.getKey(), entry.getValue().size()}));
            }
        }
    }

    private void listPlayerMarkers(CommandSender sender, String playerName) {
        var markers = plugin.getBlueMapManager().getMarkersByPlayer(playerName);
        if (markers.isEmpty()) {
            sender.sendMessage(Messages.get("commands.markers.no_player_markers", new Object[]{playerName}));
            return;
        }

        sender.sendMessage(Messages.get("commands.markers.player_markers_title", new Object[]{playerName, markers.size()}));
        for (var marker : markers) {
            sender.sendMessage(Messages.get("commands.markers.player_marker_format", new Object[]{
                marker.markerName, marker.world, marker.x, marker.y, marker.z}));
        }
    }

    private void listWorldMarkers(CommandSender sender, String worldName) {
        var markers = plugin.getBlueMapManager().getMarkersByWorld(worldName);
        if (markers.isEmpty()) {
            sender.sendMessage(Messages.get("commands.markers.no_world_markers", new Object[]{worldName}));
            return;
        }

        sender.sendMessage(Messages.get("commands.markers.world_markers_title", new Object[]{worldName, markers.size()}));
        for (var marker : markers) {
            sender.sendMessage(Messages.get("commands.markers.world_marker_format", new Object[]{
                marker.playerName, marker.markerName, marker.x, marker.y, marker.z}));
        }
    }
}
