package com.h14turkiye.bingo.util;

import java.util.ArrayList;
import java.util.List;

public class ChatColor {
	private ChatColor() {
	    throw new IllegalStateException("Utility class");
	  }
    public static List<String> translateAlternateColorCodes(char c, List<String> list){
    	List<String> returnList = new ArrayList<>();
    	for(String part : list) {
    		String newPart = org.bukkit.ChatColor.translateAlternateColorCodes(c, part);
    		returnList.add(newPart);
    	}
    	return returnList;
    }
}
