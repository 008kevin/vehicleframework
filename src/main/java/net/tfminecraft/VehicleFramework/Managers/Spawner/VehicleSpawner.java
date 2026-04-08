package net.tfminecraft.VehicleFramework.Managers.Spawner;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import net.tfminecraft.VehicleFramework.VFLogger;
import net.tfminecraft.VehicleFramework.Cache.Cache;
import net.tfminecraft.VehicleFramework.Database.IncompleteVehicle;
import net.tfminecraft.VehicleFramework.Managers.VehicleManager;
import net.tfminecraft.VehicleFramework.Vehicles.ActiveVehicle;
import net.tfminecraft.VehicleFramework.Vehicles.Vehicle;
import org.jetbrains.annotations.Nullable;

public class VehicleSpawner {
	
	public ActiveVehicle spawn(Location loc, Vehicle v, VehicleManager manager, IncompleteVehicle i) {
		Entity e = null;
		e = spawnEntity(loc);
		if (e == null) return null;

		ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity(e);
		ActiveModel m = ModelEngineAPI.createActiveModel(v.getSkinHandler().getCurrentSkin().getModel());
		modeledEntity.addModel(m, true);
		m.getMountManager().get().setCanRide(true);
		return new ActiveVehicle(v, e, m, manager, i);
	}

	@Nullable
	private static Entity spawnEntity(Location loc) {
		Entity e;
		if(!Cache.mythicMob.equalsIgnoreCase("none")) {
			MythicMob mob = MythicBukkit.inst().getMobManager().getMythicMob(Cache.mythicMob).orElse(null);
			if(mob != null){
				ActiveMob activeMob = mob.spawn(BukkitAdapter.adapt(loc),1);
				e = activeMob.getEntity().getBukkitEntity();
			} else {
				VFLogger.log(" could not find the " + Cache.mythicMob + " mythicmob");
				return null;
			}
		} else {
			ArmorStand a = loc.getWorld().spawn(loc, ArmorStand.class);
			a.setVisible(false);
			e = a;
		}
		return e;
	}

	public void respawn(ActiveVehicle v, Location l) {
		IncompleteVehicle iv = v.toIncompleteVehicle();
		Vector velocity = v.getEntity().getVelocity();
		
		// Remove old model and entity
		ModelEngineAPI.removeModeledEntity(v.getModel().getModeledEntity().getBase().getEntityId());
		v.getEntity().remove();

		// Spawn new entity and create new model
		Entity e = spawnEntity(l);
		e.setVelocity(velocity);
		ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity(e);
		ActiveModel m = ModelEngineAPI.createActiveModel(v.getSkinHandler().getCurrentSkin().getModel());
		modeledEntity.addModel(m, true);
		m.setModeledEntity(modeledEntity);
		m.getMountManager().get().setCanRide(true);

		// Re-initialize ActiveVehicle with new entity and model
		v.setEntity(e);
		v.setModel(m);
		
		// We need to re-link handlers to the new model
		v.updateModel(m);
		
		// Load state from IncompleteVehicle
		v.loadIncomplete(iv);
	}
}
