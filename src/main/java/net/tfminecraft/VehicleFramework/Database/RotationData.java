package net.tfminecraft.VehicleFramework.Database;

import org.json.simple.JSONObject;

public class RotationData {
    private String rotator;
    private float x;
    private float y;
    private float z;
    private float w;
    

    public RotationData(String rotator, float x, float y, float z, float w) {
        this.rotator = rotator;
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public RotationData(String rotator, JSONObject json) {
        this.rotator = rotator;
        this.x = ((Number) json.get("x")).floatValue();
        this.y = ((Number) json.get("y")).floatValue();
        this.z = ((Number) json.get("z")).floatValue();
        this.w = ((Number) json.get("w")).floatValue();
    }

    public String getRotator() {
        return this.rotator;
    }
    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public float getW() {
        return this.w;
    }
}
