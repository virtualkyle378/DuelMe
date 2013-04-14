package me.kyle.DuelMe.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import me.kyle.DuelMe.Arena;
import me.kyle.DuelMe.DuelMe;

public class DuelMeLogListener implements Listener {

	private DuelMe plugin;

	public DuelMeLogListener(DuelMe plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void playeLoginEvent(PlayerJoinEvent event){ 
		for(Arena i: plugin.arenas.values()){
			if(withinRange(event.getPlayer(), i))
				event.getPlayer().teleport(plugin.lobbyspawn);
		}
	}
	
	private boolean withinRange(Entity i, Arena arena){//this is a copy. the other one is in DuelMeInteractListener
		return i.getLocation().getBlockX() <= arena.pos[1].getBlockX() &&
				i.getLocation().getBlockX() >= arena.pos[0].getBlockX() &&
				i.getLocation().getBlockZ() <= arena.pos[1].getBlockZ() &&
				i.getLocation().getBlockZ() >= arena.pos[0].getBlockZ();
	}
	
	@EventHandler
	public void playerLogoutEvent(PlayerQuitEvent event){ 
		for(Arena i: plugin.arenas.values()){
			if(i.player[0].equalsIgnoreCase(event.getPlayer().getName()) || i.player[1].equalsIgnoreCase(event.getPlayer().getName()) && i.running){
				if(i.player[0].equalsIgnoreCase(event.getPlayer().getName())){
					plugin.getServer().getPlayerExact(i.player[1]).teleport(plugin.lobbyspawn);
					plugin.getServer().getPlayerExact(i.player[1]).sendMessage(ChatColor.RED + "[DuelMe] " + i.player[0] + " left the game!");
					plugin.halt.remove(i.player[0]);
					plugin.halt.remove(i.player[1]);
					manageInvs(plugin.getServer().getPlayerExact(i.player[1]), event.getPlayer());
				}else{
					plugin.getServer().getPlayerExact(i.player[0]).teleport(plugin.lobbyspawn);
					plugin.getServer().getPlayerExact(i.player[0]).sendMessage(ChatColor.RED + "[DuelMe] " + i.player[1] + " left the game!");
					plugin.halt.remove(i.player[0]);
					plugin.halt.remove(i.player[1]);
					manageInvs(plugin.getServer().getPlayerExact(i.player[0]), event.getPlayer());
				}
				plugin.getServer().getScheduler().cancelTask(plugin.countdownid.get(i));
				//this line is for if a player logs just before the "go" signal
				plugin.util.resetArena(i);
			}
			if(i.running)
				continue;
			if(i.player[0].equalsIgnoreCase(event.getPlayer().getName())) {
				resetArena(i);
				if(!i.player[1].equals(""))
					plugin.getServer().getPlayerExact(i.player[0]).sendMessage(ChatColor.RED + "[DuelMe] Other player logged off");
				break;
			} if(i.player[1].equalsIgnoreCase(event.getPlayer().getName())){
				resetArena(i);
				if(!i.player[0].equals(""))
					plugin.getServer().getPlayerExact(i.player[0]).sendMessage(ChatColor.RED + "[DuelMe] Other player logged off");
				break;
			}
		}
	}
	
	private void manageInvs(Player player, Player player2){
//		player.getInventory().clear();
//		player2.getInventory().clear();
//		player2.getInventory().setArmorContents(null);
//		player.getInventory().setArmorContents(null);
	}
	
	private void resetArena(Arena i){
		i.player[0] = "";
		i.pendingaccept = false;
		i.block[0].getBlock().setType(Material.AIR);
		i.block[1].getBlock().setType(Material.AIR);
		i.accepted[0] = false;
		i.accepted[1] = false;
	}
}
