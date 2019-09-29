package Model;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * 
 * @author Brandon Blaschke
 * Model to represent the internal clock of the device.
 */
public class ClockModel {
	
	/**
	 * List of alarms.
	 */
	private ArrayList<Alarm> alarms;
	
	
	/**
	 * Create a ClockModel.
	 * @param numberOfAlarms The number of alarms to have.
	 */
	public ClockModel(int numberOfAlarms) {
		LocalTime deafultTime = LocalTime.parse("12:00"); 
		for(int i = 0; i < numberOfAlarms; i++) {
			alarms.add(new Alarm(i+1, deafultTime));
		}
	}
	
	/**
	 * Set the time for the clock.
	 * @param newTime
	 */
	public void setTime(String newTime) {
		try {
			Process p = Runtime.getRuntime().exec(new String[] {"date", "--set", newTime});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Current LocalDateTime of 
	 * @return
	 */
	public static LocalDateTime getTime() {
		return LocalDateTime.now();
	}
	
	/**
	 * Get the list of alarms.
	 * @return ArrayList of Alarms.
	 */
	public ArrayList<Alarm> getAlarms() {
		return alarms;
	}
	
	
	/**
	 * Set an alarm for one of the three alarms.
	 * @param alarmNum Alarm to change.
	 * @param newTime New time that the alarm should wake up to.
	 * @throws Exception Throws Exception if alarm number doesn't exist. 
	 */
	public void setAlarm(int alarmNum, LocalTime newTime) throws Exception {
		for(Alarm alarm : alarms) {
			if (alarm.number == alarmNum) {
				alarm.time = newTime;
			}
		}
		throw new Exception("Alarm does not exist");
	}
	
	/**
	 * Make the given alarm active or inactive.
	 * @param alarmNum Alarm to change.
	 * @param activate True if active, false if inactive.
	 * @throws Exception 
	 */
	public void setAlarm(int alarmNum, boolean activate) throws Exception {
		for(Alarm alarm : alarms) {
			if (alarm.number == alarmNum) {
				alarm.isActive = activate;
			}
		}
		throw new Exception("Alarm does not exist");
	}
}
