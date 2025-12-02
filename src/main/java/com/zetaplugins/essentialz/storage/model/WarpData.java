package com.zetaplugins.essentialz.storage.model;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class WarpData {
    private String name;
    private String world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public WarpData(String name, String world, double x, double y, double z, float yaw, float pitch) {
        this.name = name;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public WarpData(String name, Location location) {
        this.name = name;
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public String getName() {
        return name;
    }

    public WarpData setName(String name) {
        this.name = name;
        return this;
    }

    public String getWorld() {
        return world;
    }

    public WarpData setWorld(String world) {
        this.world = world;
        return this;
    }

    public double getX() {
        return x;
    }

    public WarpData setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public WarpData setY(double y) {
        this.y = y;
        return this;
    }

    public double getZ() {
        return z;
    }

    public WarpData setZ(double z) {
        this.z = z;
        return this;
    }

    public float getYaw() {
        return yaw;
    }

    public WarpData setYaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    public float getPitch() {
        return pitch;
    }

    public WarpData setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public Location getLocation() {
        return new Location(
                Bukkit.getWorld(world),
                x,
                y,
                z,
                yaw,
                pitch
        );
    }

    @Override
    public String toString() {
        return "WarpData{" +
                "name='" + name + '\'' +
                ", world='" + world + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                '}';
    }
}
