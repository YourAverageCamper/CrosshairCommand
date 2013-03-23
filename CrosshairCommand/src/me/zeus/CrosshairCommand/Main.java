
package me.zeus.CrosshairCommand;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;



public class Main extends JavaPlugin implements Listener {

    List<Entity>nearby;

    @Override
    public void onEnable()
    {

        final File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists())
        {
            final List<String> cmdlist = new ArrayList<String>();
            getConfig().addDefault("Commands", cmdlist);
            getConfig().options().copyDefaults(true);
            saveConfig();
        }

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onUseCommand(final PlayerCommandPreprocessEvent e)
    {
        final Player player = e.getPlayer();
        final String command = e.getMessage();

        for (final String cmds : getConfig().getStringList("Commands"))
        {
            if (command.startsWith(cmds))
            {
                if(!hasPlayerInSight(player)){
                    if(!player.hasPermission("CrosshairCommand.Override")){
                        e.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "You must be looking at the player to use this command!");
                    } else {
                        player.sendMessage(ChatColor.GREEN + "Bypassed!");
                    }
                    
                } else {
                    e.setCancelled(false);
                }
            }
        }
    }


    public boolean hasPlayerInSight(Player p){
        Block targetBlock = p.getTargetBlock(null, 50);
        Location blockLoc = targetBlock.getLocation();
        double bx = blockLoc.getX();
        double by = blockLoc.getY();
        double bz = blockLoc.getZ();
        nearby = p.getNearbyEntities(100, 100, 100);

        for (Entity entity : nearby) {
            if(entity instanceof Player){
                Location loc = entity.getLocation();
                double ex = loc.getX();
                double ey = loc.getY();
                double ez = loc.getZ();

                if ((bx-1.5 <= ex && ex <= bx+2) && (bz-1.5 <= ez && ez <= bz+2) && (by-1 <= ey && ey <= by+2.5)) {
                    return true;
                }
            }
        }
        return false;
    }


}


