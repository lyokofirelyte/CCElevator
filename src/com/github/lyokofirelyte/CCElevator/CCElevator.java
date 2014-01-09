package com.github.lyokofirelyte.CCElevator;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;


public class CCElevator extends JavaPlugin implements Listener, CommandExecutor {
	
	public Map<String, CCEP> players = new HashMap<>();
	public List<Location> locs = new ArrayList<Location>();

	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("elevator").setExecutor(this);
		getLogger().log(Level.INFO, "Dev mode enabled. @author Hugh_Jasses (lyokofirelyte)");
	}
	
	public void onDisable(){
		
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		
		if (e.getPlayer().hasPermission("cce.use") && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType().equals(Material.STONE_PLATE)){
			if (players.get(e.getPlayer().getName()) == null || players.get(e.getPlayer().getName()).getMode()){
				if (!locs.contains(e.getClickedBlock().getLocation())){
					locs.add(e.getClickedBlock().getLocation());
				}
			}
		} else if (e.getAction() == Action.PHYSICAL){
			if (e.getClickedBlock().getType().equals(Material.STONE_PLATE) && players.get(e.getPlayer().getName()).getMode()){
				Location l = e.getClickedBlock().getLocation();
				if (locs.contains(l)){
					if (players.get(e.getPlayer().getName()) == null){
						CCEP ccep = new CCEP(e.getPlayer().getName());
						players.put(e.getPlayer().getName(), ccep);
					}
					schedule(e.getPlayer());
				}
			}
		}
	}
	
	public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equals("elevator") && sender.hasPermission("cce.use")){
			Player p = (Player)sender;
			if (players.get(p.getName()) == null){
				CCEP ccep = new CCEP(p.getName());
				ccep.setMode(true);
				players.put(p.getName(), ccep);
			} else if (players.get(p.getName()).getMode()){
				players.get(p.getName()).setMode(false);
			} else {
				players.get(p.getName()).setMode(true);
			}
			sender.sendMessage("Updated mode.");
		}
		
		return true;
	}
	
	public void schedule(final Player p){
		
		p.setWalkSpeed(0); p.setFlySpeed(0); p.setAllowFlight(true); p.setFlying(true);
		
		final double goal = p.getLocation().getY()+10;
		
		int task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			
			public void run() { 
			  
			  if (p.getLocation().getY() < goal){
				  p.setVelocity(new Vector(0, 0.3, 0));
			  } else {
				  getServer().getScheduler().cancelTask(players.get(p.getName()).getTask());
				  p.setWalkSpeed((float) 0.2); p.setFlySpeed((float) 0.2); p.setAllowFlight(false); p.setFlying(false);
				  getServer().dispatchCommand(getServer().getConsoleSender(), "warp devtest " + p.getName());
			  }
			  
			} }, 0L, 1L);
		
		players.get(p.getName()).setTask(task);
	}
}