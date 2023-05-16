package com.h14turkiye.bingo.scheduler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.h14turkiye.bingo.Bingo;

public class AlignedScheduler{
	
	public static long getDelayRequired(int wantedHour, int wantedMinute) {
		
		Date currentDate = new Date();

		int hour = currentDate.getHours();
		int minute = currentDate.getMinutes();

		if(hour >= wantedHour || minute > wantedMinute) 
			currentDate = new Date(currentDate.getTime() + 1000 * 60 * 60 * 24);
		
		int day = currentDate.getDate();
		int month = currentDate.getMonth()+1;
		int year = currentDate.getYear()+1900;

		String dayS = day+"";
		String monthS = month+"";
		String wantedHourS = wantedHour+"";
		String wantedMinuteS = wantedMinute+"";
		if(day < 10) dayS = "0"+day;
		if(month < 10) monthS = "0"+month;
		if(wantedHour < 10) wantedHourS = "0"+wantedHour;
		if(wantedMinute < 10) wantedMinuteS = "0"+wantedMinute;
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = null;
		try {
			date = dateFormatter .parse(year+"-"+monthS+"-"+dayS+" "+wantedHourS+":"+wantedMinuteS);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ((date.getTime() - new Date().getTime())/1000*20);
	}
	
	public BukkitTask runTaskTimer(Bingo plugin, BukkitRunnable task, int startingHour, int startingMinute, long period) {
		return task.runTaskTimer(plugin, getDelayRequired(startingHour, startingMinute), period);
	}
}
