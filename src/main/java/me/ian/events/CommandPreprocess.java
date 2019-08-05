package me.ian.events;

import me.ian.Commandlogger;
import me.ian.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

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

    @EventHandler
    public void on(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.isOp() && Commandlogger.getInstance().updateAvailable) {
            Bukkit.getScheduler().runTaskLater(Commandlogger.getInstance(), new Runnable() {
                @Override
                public void run() {
                    player.sendMessage(Commandlogger.getInstance().getConfig().getString("PREFIX") + " §aA new version of TheTowers is Out!");
                    player.sendMessage(Commandlogger.getInstance().getConfig().getString("PREFIX") + " §7Version §6" + Commandlogger.getInstance().pluginVERSION + "§7, current version running is version §6" + Commandlogger.getInstance().getDescription().getVersion() + "§7.");
                    player.sendMessage(Commandlogger.getInstance().getConfig().getString("PREFIX") + " §a§lIt is recommended to update.");
                }
            }, 40L);
        }
    }
}
