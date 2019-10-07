package Controller;

import java.time.LocalTime;

import Model.ClockModel;

/**
 * Controller class to control model.
 * @author Brandon Blaschke.
 *
 */
public class Controller {
	
	/**
	 * Reference to the clock model in main.
	 */
	private ClockModel clockModel;
	
	/**
	 * Create a Controller.
	 * @param theModel
	 */
	public Controller(ClockModel theModel) {
		clockModel = theModel;
	}
	
	/**
	 * Sets the time for the clock model.
	 * @param hour Hour of the day.
	 * @param min Minute of the hour.
	 * @param second Second of the minute.
	 * @param timeOfDay Time of day, e.g "AM" or "PM"
	 */
	public void setTime(String hour, String min, String second, String timeOfDay) {
		String newTime = String.format("%s:%s:%s %s", hour, min, second, timeOfDay);
		clockModel.setTime(newTime);
	}
	
	/**
	 * Sets the date for the clock model.
	 * @param year New year.
	 * @param month The month.
	 * @param day The day.
	 */
	public void setDate(String year, String month, String day) {
		String newDate = String.format("%s-%s-%s", year, month, day);
		clockModel.setDate(newDate);	
	}
	
	/**
	 * Sets the alarm to the time.
	 * @param alarmNum Number of the alarm.
	 * @param hour Hour to set.
	 * @param min Minute to set to.
	 * @param second Second to set to.
	 * @param timeOfDay Time of day to set alarm, "AM" or "PM".
	 * @throws Exception
	 */
	public void setAlarm(int alarmNum, String hour, String min, String second, String timeOfDay) throws Exception {
		int hourTo24 = timeOfDay == "AM" ? Integer.valueOf(hour) : Integer.valueOf(hour) + 12;
		LocalTime newTime = LocalTime.of(hourTo24, Integer.valueOf(min), Integer.valueOf(second));
		clockModel.setAlarm(alarmNum, newTime);
	}
	
	/**
	 * Sets the alarm to be active or not.
	 * @param alarmNum Number of the alarm to change.
	 * @param isActive True to activate alarm clock, False otherwise.
	 * @throws Exception 
	 */
	public void setAlarm(int alarmNum, boolean isActive) throws Exception {
		clockModel.setAlarm(alarmNum, isActive);
	}
}
