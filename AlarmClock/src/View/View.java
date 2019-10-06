package View;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import Controller.Controller;
import Model.Alarm;
import Model.ClockModel;
import processing.core.PApplet;
import processing.core.PFont;

/** Displays the time and other information as well as buttons.
 * @author Brandon Blaschke
 *
 */
public class View {
	
	/**
	 * Model for the clock.
	 */
	private ClockModel clockModel;
	
	/**
	 * Controller for the clock.
	 */
	private Controller controller;
	
	/**
	 * Parent for PApplet to draw items to the screen.
	 */
	private PApplet parent;
	
	/**
	 * Font type.
	 */
	private PFont font;
	
	/** 
	 * LocalTime Object.
	 */
	private LocalDateTime time;
	
	/**
	 * Long to hold seconds timer.
	 */
	private long seconds;
	
	/**
	 * Formatter for displaying time.
	 */
	private DateTimeFormatter timeFormatter;
	
	/**
	 * Formatter to get time of day.
	 */
	private DateTimeFormatter timeOfDayFormatter;
	
	/**
	 * Formatter for alarms
	 */
	private DateTimeFormatter alarmFormatter;
	
	/**
	 * List of alarms.
	 */
	private ArrayList<Alarm> alarms;

	/**
	 * Create a new View object.
	 * @param theModel Clock Model.
	 * @param theController Controller to control model.
	 * @param proccessingObj PApplet object that is being used to draw items.
	 */
	public View(ClockModel theModel, Controller theController, PApplet proccessingObj) {
		clockModel = theModel;
		controller = theController;
		parent = proccessingObj;
		
		// TODO: Change font 
		font = parent.createFont("Arial", 11, true);
		
		timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss", Locale.US);
		timeOfDayFormatter = DateTimeFormatter.ofPattern("a", Locale.US);
		alarmFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US);
	}
	
	/**
	 * Displays the clock screen on the canvas.
	 */
	public void display() {
		// TODO: Refactor into different functions.
		parent.background(0);
		
		// Get current time
		time = clockModel.getTime();
		seconds = timeToSeconds(String.valueOf(timeFormatter.format(time)),
				String.valueOf(timeOfDayFormatter.format(time)));
		
		// Create the circle around the time.
		parent.fill(0);
		parent.stroke(128,0,128);
		parent.strokeWeight(8);
		
		// -------------- Drawing Arc 
		
		// Mapping seconds to degrees and getting radians.
		float degrees = map(1, 86400, -90, 270, seconds);
		float arcSpread = parent.radians(degrees);
		
		parent.arc(550, 250, 370, 370, -parent.HALF_PI, arcSpread, parent.OPEN);
		
		parent.filter(parent.BLUR, 6);
		parent.arc(550, 250, 370, 370, -parent.HALF_PI, arcSpread, parent.OPEN);
		parent.filter(parent.BLUR, 0);
		
		// -------------- Draw text		
		parent.textFont(font, 48);
		parent.fill(255);
		parent.text(String.valueOf(timeFormatter.format(time)), 450, 250);
		parent.text(String.valueOf(timeOfDayFormatter.format(time)), 510, 350);
		
		// -------------- Draw Alarms
		alarms = clockModel.getAlarms();
		parent.textFont(font, 25);
		for(int i = 0; i < alarms.size(); i++) {
			parent.text(alarmString(alarms.get(i)) , 65, 65*(i + 1));
		}
	}
	
	/**
	 * Formats the alarm into a string.
	 * @param alarm Alarm to format.
	 * @return Formatted alarm in a string.
	 */
	private String alarmString(Alarm alarm) {
		return String.valueOf(alarmFormatter.format(alarm.time));
	}
	
	/**
	 * Converts the standard time into seconds.
	 * @param time Time of the day in HH:mm:ss.
	 * @param timeOfDay Time of the day in "PM" or "AM".
	 * @return Total seconds passed in the day.
	 */
	private long timeToSeconds(String time, String timeOfDay) {
		String[] times = time.split(":");
		long totalTime = 0;
		int temp;
		
		for (int i = 0; i < times.length; i++) {
			temp = Integer.valueOf(times[i]);
			
			// Convert the hour, min, or second and add to totalTime.
			switch(i) {
				case 0:
					totalTime += temp * 60 * 60;
					break;
				case 1:
					totalTime += temp * 60;
					break;
				case 2:
					totalTime += temp;
			}
		}
		
		if (timeOfDay.equals("PM")) {
			totalTime += 43200;
		}
		
		return totalTime;
	}
	
	/**
	 * Maps a value from one range to another range.
	 * @param a1 Low value of the first range.
	 * @param a2 High value of the first range.
	 * @param b1 Low value of the second range.
	 * @param b2 High value of the second range.
	 * @param val Value to be mapped to the second range.
	 * @return Value mapped from the first range to the second range.
	 */
	private float map(double a1, double a2, double b1, double b2, double val) {
		return (float) (b1 + ((val - a1) * (b2-b1))/(a2-a1));
	}
	
}
