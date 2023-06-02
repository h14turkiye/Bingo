package com.h14turkiye.bingo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class MaterialManager {
	private Bingo plugin;
	private List<Material> materials = new ArrayList<>();

	public MaterialManager(Bingo plugin) {
		this.plugin = plugin;
	}
	public List<Material> getMaterials() {
		return materials;
	}
	public void loadMaterials() {
		File dataFolder = plugin.getDataFolder();
		
		File child = new File(dataFolder, "assets/textures");
		for(String string :child.list()) {
			int index = string.lastIndexOf(".");
			Material material = Material.matchMaterial(string.substring(0, index));
			if(material != null) {
				materials.add(Material.matchMaterial(string.substring(0, index))); 
				plugin.getLogger().info(ChatColor.YELLOW + "Loading " + string);
			}
			else {
				
				plugin.getLogger().warning(ChatColor.YELLOW + "Failed loading " + string);
			}
		}
	}

	public boolean extractMaterials() {
		File dataFolder = plugin.getDataFolder();
		
		if (!dataFolder.exists()) {
			dataFolder.mkdir();
		}

		File parent = new File(dataFolder, "assets");

		if (!parent.exists()) {
			parent.mkdir();
			plugin.getLogger().info(ChatColor.GREEN + "Saving default (1.18) textures.");
		}
		else {
			return true;
		}

		File child = new File(dataFolder, "assets/textures");

		if (!child.exists()) {
			child.mkdir();

			try {
				JarFile jarFile = new JarFile(Bingo.class.getProtectionDomain().getCodeSource().getLocation().getFile());
				Enumeration<JarEntry> e = jarFile.entries();
				while (e.hasMoreElements()) {
					JarEntry candidat = e.nextElement();
					
					if (candidat.getName().matches("assets/textures/(.+)")) {
						plugin.getLogger().info(ChatColor.YELLOW + "Extracting " + candidat.getName()+" to "+dataFolder+"/assets/textures");
						plugin.saveResource(candidat.getName(), false);
						
					}

				}
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
