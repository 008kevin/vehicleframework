package net.tfminecraft.VehicleFramework.Vehicles.Controller;

import me.m56738.smoothcoasters.api.SmoothCoastersAPI;
import net.tfminecraft.VehicleFramework.VehicleFramework;
import net.tfminecraft.VehicleFramework.Vehicles.Seat.Seat;
import org.bukkit.entity.Player;

import net.tfminecraft.VehicleFramework.Bones.BoneRotator;
import net.tfminecraft.VehicleFramework.Bones.ConvertedAngle;
import net.tfminecraft.VehicleFramework.Enums.Animation;
import net.tfminecraft.VehicleFramework.Enums.SeatType;
import net.tfminecraft.VehicleFramework.Vehicles.ActiveVehicle;
import org.joml.Quaternionf;

public class RotateController {
	
	private void turnLocal(BoneRotator rotator, ActiveVehicle v, boolean reverse) {
		float turn = (float) v.getAccessPanel().getTurnRate();
		if(reverse) turn = turn*-1;
		if(turn != 0) {
			rotator.rotateSmoothed(0, turn, 0);
			updateSmoothCoasters(rotator, v);
		}
	}
	private void turn(BoneRotator rotator, ActiveVehicle v, boolean reverse) {
		float turn = (float) v.getAccessPanel().getTurnRate();
		if(reverse) turn = turn*-1;
		turn *= 5;
		ConvertedAngle a = new ConvertedAngle(rotator.getAnimator().getRotation());

		if(turn != 0) {
			rotator.setRotation(a.getYaw() + turn, a.getPitch(), a.getRoll(), true, false, false);
			updateSmoothCoasters(rotator, v);
		}
	}
	
	public void turnLeftLocal(BoneRotator rotator, ActiveVehicle v, Player p) {
		if(v.getSeat(p).getType().equals(SeatType.CAPTAIN)) {
			v.animate(Animation.LEFT);
	    	turnLocal(rotator, v, false);     
		}
	}

	public void turnRightLocal(BoneRotator rotator, ActiveVehicle v, Player p) {
		if(v.getSeat(p).getType().equals(SeatType.CAPTAIN)) {
			v.animate(Animation.RIGHT);
	    	turnLocal(rotator, v, true);
		}
	}
	
	public void turnLeft(BoneRotator rotator, ActiveVehicle v, Player p) {
		if(v.getSeat(p).getType().equals(SeatType.CAPTAIN)) {
			v.animate(Animation.LEFT);
	    	turn(rotator, v, false);     
		}
	}
	
	public void turnRight(BoneRotator rotator, ActiveVehicle v, Player p) {
		if(v.getSeat(p).getType().equals(SeatType.CAPTAIN)) {
			v.animate(Animation.RIGHT);
	    	turn(rotator, v, true);     
		}
	}
	
	public void pitchUp(BoneRotator rotator, ActiveVehicle v, Player p, float rate) {
		if(v.getSeat(p).getType().equals(SeatType.CAPTAIN)) {
			rotator.rotateSmoothed(-rate, 0, 0);
			updateSmoothCoasters(rotator, v);
		}
	}

	public void pitchDown(BoneRotator rotator, ActiveVehicle v, Player p, float rate) {
		if(v.getSeat(p).getType().equals(SeatType.CAPTAIN)) {
			rotator.rotateSmoothed(rate, 0, 0);
			updateSmoothCoasters(rotator, v);
		}
	}
	public void rollLeft(BoneRotator rotator, ActiveVehicle v, Player p, float rate) {
		if(v.getSeat(p).getType().equals(SeatType.CAPTAIN)) {
			rotator.rotateSmoothed(0, 0, -rate);
			updateSmoothCoasters(rotator, v);
		}
	}

	public void rollRight(BoneRotator rotator, ActiveVehicle v, Player p, float rate) {
		if(v.getSeat(p).getType().equals(SeatType.CAPTAIN)) {
			rotator.rotateSmoothed(0, 0, rate);
			updateSmoothCoasters(rotator, v);
		}
	}

	private void updateSmoothCoasters(BoneRotator rotator, ActiveVehicle v) {
		SmoothCoastersAPI sc = VehicleFramework.getSmoothCoastersAPI();
		for (Seat s : v.getSeatHandler().getSeats()) {
			if (s.isOccupied()) {
				if (s.getEntity() instanceof Player p) {
					Quaternionf q = new Quaternionf(rotator.getAnimator().getRotation());
					sc.setRotation(null, p, q.x, q.y, q.z, q.w, (byte)3);
				}
			}
		}
	}
}
