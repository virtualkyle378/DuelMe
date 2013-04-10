package me.kyle.DuelMe.Commands;

import me.kyle.DuelMe.Arena;
import me.kyle.DuelMe.DuelMe;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DuelMeArenaCreator {

	private Player player;
	private DuelMeCommandExecutor commandexecutor;
	private DuelMe plugin;
	private String name;
	public Arena a = new Arena();
	public Step step = Step.confirmStart;
	
	public DuelMeArenaCreator(Player player, DuelMe plugin, DuelMeCommandExecutor commandexecutor, String name) {
		this.player = player;
		this.commandexecutor = commandexecutor;
		this.plugin = plugin;
		this.name = name;
		init();
	}
	
	public void init(){
		plugin.util.sendMessage("You are trying to edit a value to an arena that does not yet exist!", ChatColor.RED, player);
		plugin.util.sendMessage("Would you like to create a new arena?", ChatColor.GREEN, player);
		plugin.util.sendMessage("Type " + ChatColor.ITALIC + "/dm confirm " + ChatColor.GREEN + "to accept or " + ChatColor.ITALIC + "/dm cancel " + ChatColor.GREEN + "to cancel the add", ChatColor.GREEN, player);
	}
	
	public void proccess(String[] args){
		String[] arg = plugin.util.safeArgs(args, 2);
		if(arg[0].equals("confirm")){
			if(step.equals(Step.confirmStart)){
				confirmStart();
			} else if(step.equals(Step.confirmEntry)){
				confirmEntry();
			} else {
				plugin.util.sendMessage("Invalid command!", ChatColor.RED, player);
			}
		} else if(arg[0].equals("continue")){
			if(step.equals(Step.pos1)){
				setPos1();
			} else if(step.equals(Step.pos2)){
				setPos2();
			} else if(step.equals(Step.spawn1)){
				setSpawn1();
			} else if(step.equals(Step.spawn2)){
				setSpawn2();
			} else if(step.equals(Step.block1)){
				setBlock1();
			} else if(step.equals(Step.block2)){
				setBlock2();
			} else if(step.equals(Step.door11) || step.equals(Step.door12)){
				setDoor1();
			} else if(step.equals(Step.door21) || step.equals(Step.door22)){
				setDoor2();
			} else if(step.equals(Step.inventory)){
				setInv();
			} else if(step.equals(Step.stands)){
				setStands();
			} else {
				plugin.util.sendMessage("Invalid command!", ChatColor.RED, player);
			}
		} else if(arg[0].equals("cancel")){
			cancel();
		} else {
			plugin.util.sendMessage("Invalid command! You are creating an arena!", ChatColor.RED, player);
			plugin.util.sendMessage("If you want to cancel, type " + ChatColor.ITALIC + "/dm cancel", ChatColor.RED, player);
		}
	}

	public void confirmStart(){
		plugin.util.sendMessage("You have started configuring a new arena!", ChatColor.GREEN, player);
		plugin.util.sendMessage("Please select first position to surround arena.", ChatColor.GREEN, player);
		plugin.util.sendMessage("When you are done, type " + ChatColor.ITALIC + "/dm continue", ChatColor.GREEN, player);
		step = Step.pos1;
	}
	
	private void confirmEntry() {
		//plugin.util.sendMessage("When you are done, type " + ChatColor.ITALIC + "/dm continue", ChatColor.GREEN, player);
		plugin.util.resetArena(a);
		plugin.arenas.put(name, a);
		plugin.util.sendMessage("Arena added as " + name, ChatColor.GREEN, player);
		commandexecutor.arenacreations.remove(player.getName());
	}
	
	public void cancel(){
		commandexecutor.arenacreations.remove(player.getName());
		plugin.util.sendMessage("Arena creation cancelled", ChatColor.RED, player);
	}
	
	private void setPos1() {
		a.pos[0] = player.getLocation();
		plugin.util.sendMessage("Pos1 set!", ChatColor.GREEN, player);
		plugin.util.sendMessage("set pos2", ChatColor.GREEN, player);
		step = Step.pos2;
	}
	
	private void setPos2() {
		a.pos[1] = player.getLocation();
		plugin.util.sendMessage("Pos2 set!", ChatColor.GREEN, player);
		if(plugin.util.checkCoords(a.pos[0], a.pos[1])){
			plugin.util.sendMessage("set spawn1", ChatColor.GREEN, player);
			step = Step.spawn1;
		}else{
			plugin.util.sendMessage("Coordnates are not representing the cube correctly", ChatColor.RED, player);
			plugin.util.sendMessage("Please try representing the figure differently", ChatColor.RED, player);
			plugin.util.sendMessage("If it is not correct YOU WILL BREAK THINGS", ChatColor.RED, player);
			plugin.util.sendMessage("re-set pos 1", ChatColor.RED, player);
			step = Step.pos1;
		}
	}
	
	private void setSpawn1() {
		a.spawn[0] = player.getLocation();
		plugin.util.sendMessage("Spawn1 set!", ChatColor.GREEN, player);
		plugin.util.sendMessage("Set block1", ChatColor.GREEN, player);
		step = Step.block1;
	}
	
	private void setSpawn2() {
		a.spawn[1] = player.getLocation();
		plugin.util.sendMessage("Spawn2 set!", ChatColor.GREEN, player);
		plugin.util.sendMessage("Set block2", ChatColor.GREEN, player);
		step = Step.block2;
	}
	
	private void setBlock1() {
		try {
			a.block[0] = player.getTargetBlock(null, 5).getLocation();
		} catch (NullPointerException e){
			plugin.util.sendMessage("No block in sight broski. try again", ChatColor.RED, player);
			return;
		}
		plugin.util.sendMessage("Block1 set!", ChatColor.GREEN, player);
		plugin.util.sendMessage("Set door1 sub 1", ChatColor.GREEN, player);
		step = Step.door11;
	}
	
	private void setBlock2() {
		try {
			a.block[1] = player.getTargetBlock(null, 5).getLocation();
		} catch (NullPointerException e){
			plugin.util.sendMessage("No block in sight broski. try again", ChatColor.RED, player);
			return;
		}
		plugin.util.sendMessage("Block2 set!", ChatColor.GREEN, player);
		plugin.util.sendMessage("Set door 2 sub 1", ChatColor.GREEN, player);
		step = Step.door21;
	}

	private void setDoor1() {
		int sub;
		if(step.equals(Step.door11)){
			sub = 1;
		} else {
			sub = 2;
		}
		try {
			a.door[0][sub - 1] = player.getTargetBlock(null, 5).getLocation();
		} catch (NullPointerException e){
			plugin.util.sendMessage("No block in sight broski. try again", ChatColor.RED, player);
			return;
		}
		plugin.util.sendMessage("Door1 sub" + sub + " set!", ChatColor.GREEN, player);
		if(step.equals(Step.door11)){
			step = Step.door12;
			plugin.util.sendMessage("Set door sub 2", ChatColor.GREEN, player);
		} else {
			step = Step.spawn2;
			plugin.util.sendMessage("Set spawn2", ChatColor.GREEN, player);
		}
	}
	
	private void setDoor2() {
		int sub;
		if(step.equals(Step.door21))
			sub = 1;
		else
			sub = 2;
		try {
			a.door[1][sub - 1] = player.getTargetBlock(null, 5).getLocation();
		} catch (NullPointerException e){
			plugin.util.sendMessage("No block in sight broski. try again", ChatColor.RED, player);
			return;
		}
		plugin.util.sendMessage("Door2 sub" + sub + " set!", ChatColor.GREEN, player);
		if(step.equals(Step.door21)){
			step = Step.door22;
			plugin.util.sendMessage("Set door sub 2", ChatColor.GREEN, player);
		} else {
			step = Step.inventory;
			plugin.util.sendMessage("Set the inventory (its a snapshot of your current inv)", ChatColor.GREEN, player);
		}
	}
	
	private void setInv(){
		a.inventory = player.getInventory().getContents();
		a.armor = player.getInventory().getArmorContents();
		plugin.util.sendMessage("Inv set", ChatColor.GREEN, player);
		step = Step.stands;
		plugin.util.sendMessage("Set the stands. just set em plz. (in later release you will type confirm for no stands)", ChatColor.GREEN, player);
	}
	
	private void setStands(){
		a.stands = player.getLocation();
		plugin.util.sendMessage("Stands set!", ChatColor.GREEN, player);
		plugin.util.sendMessage("Use /dm confirm to finish!", ChatColor.GREEN, player);
		step = Step.confirmEntry;
	}
}

enum Step{//in order of setting
	confirmStart,//in future realease have each have a display message and one a set message. so in each step it wont do repetitive messages..
	pos1,
	pos2,
	spawn1,
	block1,
	door11,
	door12,
	spawn2,
	door21,
	door22,
	block2,
	inventory,
	stands,
	confirmEntry;
}

