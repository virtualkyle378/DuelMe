package me.kyle.DuelMe.Listeners;

import me.kyle.DuelMe.Arena;
import me.kyle.DuelMe.DuelMe;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

public class DuelMeListener implements Listener {
	/*
	 * account for logging players
	 * this is the main listener
	 * all other ones require more space
	 */
	private DuelMe plugin;

	public DuelMeListener(DuelMe plugin){
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		//create sub-listeners
		new DuelMeInteractListener(plugin);
		new DuelMeMoveListener(plugin);
		new DuelMeLogListener(plugin);
	}
	
	//MODE2 BEGIN
	@EventHandler
	public void playerDeathEvent(EntityDamageEvent event){
		Player player;
		if(event.getEntity() instanceof Player)
			player = (Player)event.getEntity();
		else
			return;
		System.out.println("one");
		if(!(event.getDamage() >= player.getHealth()))
			return;
		System.out.println("two");
		for(Arena i: plugin.arenas.values()){
			if(i.player[0].equalsIgnoreCase(player.getName()) || i.player[1].equalsIgnoreCase(player.getName()) && i.running){
				System.out.println("three");
				event.setCancelled(true);//TODO what if thewy kill emselves
				if(i.player[0].equalsIgnoreCase(player.getName())){
					manageWinner(plugin.getServer().getPlayerExact(i.player[1]), plugin.getServer().getPlayerExact(i.player[0]));//TODO use player inseaad of searcking
					System.out.println("four");
				} else {
					System.out.println("five");
					manageWinner(plugin.getServer().getPlayerExact(i.player[0]), plugin.getServer().getPlayerExact(i.player[1]));
				}
				plugin.util.resetArena(i);
				return;
			}
		}
		return;
	}
	
	private void manageWinner(Player winner, Player loser){
		winner.sendMessage(ChatColor.GREEN + "[DuelMe] You won the duel!");
		winner.setHealth(20);
		loser.sendMessage(ChatColor.RED + "[DuelMe] You lost the duel!");//add a method that has this code rather than copy/paste
		loser.teleport(plugin.lobbyspawn);
		loser.setHealth(20);
		loser.setVelocity(new Vector());
		loser.setFireTicks(0);
		//disabled inventory stuff
//		winner.getInventory().clear();
//		loser.getInventory().clear();
//		loser.getInventory().setArmorContents(null);
//		winner.getInventory().setArmorContents(null);
	}
	//MODE2 END
}