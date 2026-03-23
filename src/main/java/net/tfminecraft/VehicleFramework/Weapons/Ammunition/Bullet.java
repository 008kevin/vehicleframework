package net.tfminecraft.VehicleFramework.Weapons.Ammunition;

import org.bukkit.configuration.ConfigurationSection;

public class Bullet extends Ammunition{
	
	private double range;
	
	public Bullet (String key, ConfigurationSection config) {
		super(key, config);
		range = config.getDouble("range", 80.0);
	}
	
	public double getRange() {
		return range;
	}
	
}
