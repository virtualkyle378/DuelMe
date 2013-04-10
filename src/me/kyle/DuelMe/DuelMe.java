  package me.kyle.DuelMe;

import java.util.ArrayList;
import java.util.HashMap;
import me.kyle.DuelMe.Commands.DuelMeCommandExecutor;
import me.kyle.DuelMe.Listeners.DuelMeListener;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class DuelMe extends JavaPlugin{
	
	public HashMap<String, Arena> arenas = new HashMap<String, Arena>();
	public ArrayList<String> halt = new ArrayList<String>();
	public HashMap<Arena, Integer> countdownid = new HashMap<Arena, Integer>();
	public ArrayList<String> deadplayers = new ArrayList<String>();
	public Location lobbyspawn;
	public DuelMeUtil util;
	@Override
	public void onEnable(){
		getCommand("dm").setExecutor(new DuelMeCommandExecutor(this));
		new DuelMeListener(this);
		util = new DuelMeUtil(this);
		new DuelMeGetConfig(this);
		util.globalReset();
	}
	
	@Override
	public void onDisable(){
		new DuelMeSetConfig(this);
	}
}
