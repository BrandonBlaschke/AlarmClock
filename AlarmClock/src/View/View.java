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
import static View.Constants.*;

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
	 * Formatter for the date
	 */
	private DateTimeFormatter dateFormatter;
	
	/**
	 * Formatter for the week day name.
	 */
	private DateTimeFormatter weekDayFormatter;
	
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
		dateFormatter = DateTimeFormatter.ofPattern("MMM / dd / yy", Locale.US);
		weekDayFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.US);
	}
	
	/**
	 * Displays the clock screen on the canvas.
	 * @throws Exception 
	 */
	public void display() throws Exception {
		// TODO: Refactor into different functions.
		parent.background(0);
		
		// Get current time
		time = clockModel.getTime();
		seconds = timeToSeconds(String.valueOf(timeFormatter.format(time)),
				String.valueOf(timeOfDayFormatter.format(time)));
		
		// Create the circle around the time.
		parent.fill(0);
		parent.stroke(PRIMARY_COLOR[0], PRIMARY_COLOR[1], PRIMARY_COLOR[2]);
		parent.strokeWeight(STROKE_WEIGHT);
		
		// -------------- Drawing Arc 
		
		// Mapping seconds to degrees and getting radians.
		float degrees = map(1, TOTAL_SECONDS, -90, 270, seconds);
		float arcSpread = parent.radians(degrees);
		
		parent.arc(ARC_POS[0], ARC_POS[1], ARC_SIZE, ARC_SIZE, -parent.HALF_PI, arcSpread, parent.OPEN);
		
		parent.filter(parent.BLUR, 6);
		parent.arc(ARC_POS[0], ARC_POS[1], ARC_SIZE, ARC_SIZE, -parent.HALF_PI, arcSpread, parent.OPEN);
		parent.filter(parent.BLUR, 0);
		
		// -------------- Draw time		
		parent.textFont(font, 55);
		parent.fill(255);
		
		parent.text(String.valueOf(timeFormatter.format(time)), TIME_POS[0], TIME_POS[1]);
		parent.fill(PRIMARY_COLOR[0], PRIMARY_COLOR[1], PRIMARY_COLOR[2]);
		parent.text(String.valueOf(timeOfDayFormatter.format(time)), TOD_POS[0], TOD_POS[1]);
		
		// -------------- Draw Alarms
		alarms = clockModel.getAlarms();
		parent.textFont(font, 35);
		for(int i = 0; i < alarms.size(); i++) {
			if (alarms.get(i).isActive) {
				parent.fill(PRIMARY_COLOR[0], PRIMARY_COLOR[1], PRIMARY_COLOR[2]);
				parent.circle(94, (ALARM_POS[1]*(i + 1) - 62), 45);	
			}
			
			parent.fill(255);
			parent.text(alarms.get(i).number, 83, (ALARM_POS[1]*(i + 1) - 50));
			parent.text(alarmString(alarms.get(i)) , ALARM_POS[0], ALARM_POS[1]*(i + 1));
		}
		
		// -------------- Draw Date
		parent.textFont(font, 50);
		parent.text(String.valueOf(weekDayFormatter.format(time)), WEEK_POS[0], WEEK_POS[1]);
		parent.textFont(font, 30);
		parent.text(String.valueOf(dateFormatter.format(time)), DATE_POS[0], DATE_POS[1]); 
		
		// -------------- Checking inputs
		checkAlarmToggle(alarms);
	}
	
	/**
	 * Checks if user pressed within a alarm button toggle and toggles that alarm.
	 * @param alarms List of alarm clocks
	 * @throws Exception If wrong alarm number is passed.
	 */
	private void checkAlarmToggle(ArrayList<Alarm> alarms) throws Exception {
		// TODO: Make it for all alarm clocks and remove magic numbers. 
		if (parent.mousePressed) {
			double dist = Math.sqrt(Math.pow(94 - parent.mouseX, 2) + Math.pow(75 - parent.mouseY, 2));
			if (dist < 45) 
				controller.setAlarm(1, !alarms.get(0).isActive);
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
