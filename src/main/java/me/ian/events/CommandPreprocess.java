package me.ian.events;

import me.ian.Commandlogger;
import me.ian.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Iterator;

public class CommandPreprocess implements Listener {


    @EventHandler
    public void on(PlayerCommandPreprocessEvent e) {
        final Player p = e.getPlayer();
        String msg = e.getMessage();
        String command;
        if (p.hasPermission("commandlog.bypass")) return;
        Iterator commands = Commandlogger.getInstance().getConfig().getStringList("commands-to-log").iterator();

        do {
            if (!commands.hasNext()) return;

            command = (String) commands.next();
        } while (!msg.substring(1).startsWith(command));

        ConfigUtil.logMessageToFile(Commandlogger.getInstance().getConfig().getString("LOG_MESSAGE").replace("%player%", p.getDisplayName()).replace("%command%", msg));
        Bukkit.getOnlinePlayers().forEach(o -> {
            if (o.hasPermission("commandlog.see")) {
                o.sendMessage(ChatColor.translateAlternateColorCodes('&', Commandlogger.getInstance().getConfig().getString("INGAME_MESSAGE")).replace("%prefix%", Commandlogger.getInstance().getConfig().getString("PREFIX").replace("%player%", p.getDisplayName()).replace("%command%", msg)));
            }
        });
    }
}
