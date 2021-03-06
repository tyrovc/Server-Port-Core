/*******************************************************************************
 * Copyright (C) 2012 Raphfrk
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.raphfrk.bukkit.serverportcore;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.bukkit.Location;
import org.bukkit.World;

import com.raphfrk.bukkit.serverportcoreapi.ServerPortCoreAPI;
import com.raphfrk.bukkit.serverportcoreapi.ServerPortLocation;

@Entity
public class SPLocation implements Serializable, ServerPortLocation {

	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Column(unique=true, nullable=false)
	private String name;
	private String playerName;
	private String server;
	private String world;
	private Double x;
	private Double y;
	private Double z;
	private Float pitch;
	private Float yaw;

	public SPLocation() {
	}

	public SPLocation(String serverName, Location loc) {
		this(
				serverName, 
				(loc.getWorld()==null)?null:(loc.getWorld().getName()), 
						loc.getX(),
						loc.getY(),
						loc.getZ(),
						loc.getPitch(),
						loc.getYaw()
		);
	}

	public SPLocation(SPLocation loc) {
		this(loc.getServer(), loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	}

	public SPLocation(ServerPortLocation loc) {
		this(loc.getServer(), loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	}

	public SPLocation(String server, String world, Double x, Double y, Double z, Float yaw, Float pitch) {
		this.server = server;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getWorld() {
		return world;
	}

	public String getServer() {
		return server;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getX() {
		return this.x;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public Double getY() {
		return this.y;
	}

	public void setZ(Double z) {
		this.z = z;
	}

	public Double getZ() {
		return this.z;
	}

	public void setYaw(Float yaw) {
		this.yaw = yaw;
	}

	public Float getYaw() {
		return this.yaw;
	}

	public void setPitch(Float pitch) {
		this.pitch = pitch;
	}

	public Float getPitch() {
		return this.pitch;
	}

	Location getLocation() {
		return new Location(null, x, y, z, yaw, pitch);
	}

	Location getLocation(SPTeleportManager teleportManager) {

		String pluginServerName = teleportManager.getServerName();
		if(server != null && !server.equals(pluginServerName)) {
			return null;
		}
		World bukkitWorld = null;
		if(world != null) {
			bukkitWorld = teleportManager.getServer().getWorld(this.world);
			if(bukkitWorld == null) {
				return null;
			}
		}

		if(bukkitWorld == null) {
			bukkitWorld = teleportManager.p.getServer().getWorlds().get(0);
		}

		if(x==null || y==null || z==null) {
			Location spawn = bukkitWorld.getSpawnLocation();
			spawn.setX(spawn.getX() + 0.5);
			spawn.setZ(spawn.getZ() + 0.5);
			return spawn;
		} else if(pitch == null || yaw == null) {
			return new Location(bukkitWorld, x, y, z);
		} else {
			return new Location(bukkitWorld, x, y, z, yaw, pitch);
		}
	}

	public Location getLocation(ServerPortCoreAPI serverPortCoreAPI) {
		
		String pluginServerName = serverPortCoreAPI.getLocalServerName();
		
		if(server != null && !server.equals(pluginServerName)) {
			return null;
		}
		
		World bukkitWorld = null;
		
		if(world != null) {
			bukkitWorld = serverPortCoreAPI.getLocalServer().getWorld(this.world);
			if(bukkitWorld == null) {
				return null;
			}
		}

		if(bukkitWorld == null) {
			bukkitWorld = serverPortCoreAPI.getLocalServer().getWorlds().get(0);
		}

		if(x==null || y==null || z==null) {
			Location spawnPosition = bukkitWorld.getSpawnLocation();
			spawnPosition.setX(spawnPosition.getX() + 0.5);
			spawnPosition.setZ(spawnPosition.getZ() + 0.5);
			return spawnPosition;
		} else if(pitch == null || yaw == null) {
			return new Location(bukkitWorld, x, y, z);
		} else {
			return new Location(bukkitWorld, x, y, z, yaw, pitch);
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
