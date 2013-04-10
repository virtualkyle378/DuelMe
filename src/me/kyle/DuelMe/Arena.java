package me.kyle.DuelMe;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class Arena {
	//all need to be public as listeners are in a seperate folder
	//and therefore need to be imported
	
	public Location[] pos = new Location[2];
	public Location[] spawn = new Location[2];
	public Location stands;//list for spectators to be told who wins???? by request..
	public Location[][] door = new Location[2][2];
	public Location[] block = new Location[2];
	public ItemStack[] inventory;
	public ItemStack[] armor;
	
	public String[] player = new String[2];
	public boolean pendingaccept = false;
	public boolean[] accepted = new boolean[2];
	public boolean running = false; 
}
