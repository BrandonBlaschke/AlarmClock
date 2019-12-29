package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

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
	 * Formats the dates for the date command.
	 */
	private DateTimeFormatter dateFormatCmd;
	
	/**
	 * Formats the time for the date time command.
	 */
	private DateTimeFormatter timeFormatCmd;
	
	/**
	 * True if alarm is on, False if off.
	 */
	private boolean alarmOn;
	
	
	/**
	 * Play Audio for alarm.
	 */
	private AudioPlayer audioPlayer;
	
	
	/**
	 * Create a ClockModel.
	 * @param numberOfAlarms The number of alarms to have.
	 */
	public ClockModel(int numberOfAlarms) {
		LocalTime deafultTime = LocalTime.parse("12:00"); 
		alarms = new ArrayList<Alarm>();
		for(int i = 0; i < numberOfAlarms; i++) {
			alarms.add(new Alarm(i+1, deafultTime));
		}
		dateFormatCmd = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US);
		timeFormatCmd = DateTimeFormatter.ofPattern("hh:mm:ss", Locale.US);
		// Location of alarm soundfx file by GowlerMusic
		try {
			audioPlayer = new AudioPlayer("src/Model/alarm_sound.wav");
		} catch (Exception e) {
			e.printStackTrace();
		}
		alarmOn = false;
	}
	
	/**
	 * Set the time for the system clock to the new time.
	 * Should be in the format of hh:mm:ss.sss AM
	 * @param newTime Time to be set on system clock
	 */
	public void setTime(String newTime) {
		try {
			String currentDate = String.valueOf(dateFormatCmd.format(getTime()));
			System.out.println(String.format("%s %s", currentDate, newTime));
			Process p = Runtime.getRuntime().exec(new String[] {"date", "--set", String.format("%s %s", currentDate, newTime)});
			
			// Debugging purposes
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s = null;
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}
			
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			String ss = null;
			while ((ss = stdError.readLine()) != null) {
				System.out.println(ss);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set the date for the system clock to the new time.
	 * Should be in the format of yyyy-mm-dd.
	 * @param newDate Date to be set on system clock.
	 */
	public void setDate(String newDate) {
		try {
			String currentTime = String.valueOf(timeFormatCmd.format(getTime()));
			System.out.println(currentTime);
			Process p = Runtime.getRuntime().exec(new String[] {"date", "--set", String.format("%s %s",newDate, currentTime)});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Current LocalDateTime of 
	 * @return Current time and date.
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
				return;
			}
		}
		throw new Exception("Alarm does not exist, Number: " + String.valueOf(alarmNum));
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
				return;
			}
		}
		throw new Exception("Alarm does not exist, Number: " + String.valueOf(alarmNum));
	}
	
	/**
	 * Checks if the alarms match the time. If one of them does the alarm goes off.
	 */
	public void checkAlarm() {
		for (Alarm alarm : alarms) {
			String currentTime = String.valueOf(timeFormatCmd.format(getTime()));
			String alarmTime = String.valueOf(timeFormatCmd.format(alarm.time));
			if (alarmTime.equals(currentTime) && alarm.isActive) {
				audioPlayer.play();
				alarmOn = true;
			}
		}
	}
	
	/**
	 * Stops the alarm.
	 */
	public void stopAlarm() {
		audioPlayer.stop();
		alarmOn = false;
	}
	
	/**
	 * Check if alarm is current on.
	 * @return True if alarm is on, false if off.
	 */
	public boolean isAlarmOn() {
		return alarmOn;
	}
}
