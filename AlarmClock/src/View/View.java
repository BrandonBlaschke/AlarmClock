package View;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import Controller.Controller;
import Main.Main;
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
	 *  List of AlarmWidgets
	 */
	private ArrayList<AlarmWidget> alarmWidgets;
	
	/** 
	 * Current AlarmWidget
	 */
	private AlarmWidget currentAlarmWidget;
	
	/**
	 * If the main display should be shown.
	 */
	private boolean showMain; 

	/**
	 * Create a new View object.
	 * @param theModel Clock Model.
	 * @param theController Controller to control model.
	 * @param proccessingObj PApplet object that is being used to draw items.
	 */
	public View(ClockModel theModel, Controller theController, Main proccessingObj) {
		clockModel = theModel;
		controller = theController;
		parent = proccessingObj;
		currentAlarmWidget = null;
		showMain = true;
		
		// TODO: Change font 
		font = parent.createFont("Arial", 11, true);
		
		timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss", Locale.US);
		timeOfDayFormatter = DateTimeFormatter.ofPattern("a", Locale.US);
		dateFormatter = DateTimeFormatter.ofPattern("MMM / dd / yy", Locale.US);
		weekDayFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.US);
		
		alarms = clockModel.getAlarms();
		alarmWidgets = new ArrayList<AlarmWidget>();
		for (int i = 0; i < alarms.size(); i++) {
			alarmWidgets.add(new AlarmWidget(proccessingObj, 80, ALARM_POS[i], alarms.get(i), theController));
		}
	}
	
	/**
	 * Displays the clock screen on the canvas.
	 * @throws Exception 
	 */
	public void display() {
		// TODO: Refactor with something other than if statements maybe.
		parent.background(0);
		
		if (showMain)
			mainDisplay();
		
		if (!showMain)
			alarmSettingsDisplay();
	}
	
	
	/**
	 * Displays the main view for the clock.
	 */
	private void mainDisplay() {
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
		for (AlarmWidget aw : alarmWidgets) {
			aw.displayAlarmWidget(font);
		}
		
		// -------------- Draw Date
		parent.textFont(font, 40);
		parent.text(String.valueOf(weekDayFormatter.format(time)), WEEK_POS[0], WEEK_POS[1]);
		parent.textFont(font, 30);
		parent.text(String.valueOf(dateFormatter.format(time)), DATE_POS[0], DATE_POS[1]); 
		
		// -------------- Checking inputs
		try {
			checkAlarmSettingsPressed();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Displays the alarm settings for a particular alarm.
	 */
	private void alarmSettingsDisplay() {
		showMain = currentAlarmWidget.displayAlarmEdit(font);
	}
	
	/**
	 * Checks if an alarm was clicked to change its settings.
	 */
	private void checkAlarmSettingsPressed() {
		for (AlarmWidget aw : alarmWidgets) {
			if (aw.checkEditAlarmPressed()) {
				showMain = false;
				currentAlarmWidget = aw;
			}
		}
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
					if (temp != 12)
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
