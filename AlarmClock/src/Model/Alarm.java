package Model;

import java.time.LocalTime;

/**
 * 
 * @author Brandon Blaschke
 * Represents an alarm for an alarm clock.
 */
public class Alarm {
	
	/**
	 * Number of the alarm.
	 */
	public int number;
	
	/**
	 * Time of day that the alarm is suppose to go off.
	 */
	public LocalTime time;
	
	/**
	 * Should the alarm ring when the clock reaches its given time.
	 */
	public boolean isActive;
	
	/**
	 * Creates an Alarm Object
	 * @param numberOfAlarm Alarm number.
	 * @param wakeTime Time for the alarm to wake up
	 */
	public Alarm(int numberOfAlarm, LocalTime wakeTime) {
		number = numberOfAlarm;
		time = wakeTime;
		isActive = false;
	}
}
