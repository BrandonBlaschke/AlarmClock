package View;

import static View.Constants.*;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import Controller.Controller;
import Main.Main;
import Model.Alarm;
import processing.core.PApplet;
import processing.core.PFont;

/** Represents a AlarmWidget on the screen.
 * @author Brandon Blaschke
 */
public class AlarmWidget {
	
	/*
	 * Parent object for PApplet.
	 */
	private PApplet parent;
	
	/**
	 * X coordinate of AlarmWidget.
	 */
	private int x;
	
	/**
	 * Y coordinate of AlarmWidget.
	 */
	private int y;
	
	/**
	 * ClockModel in order to manipulate clock.
	 */
	public Alarm alarm;
	
	/**
	 * Controller for alarm model.
	 */
	private Controller controller;
	
	/**
	 * Formatter for alarm
	 */
	private DateTimeFormatter alarmFormatter;
	
	/**
	 * Create a AlarmWidget
	 * @param proccessingObj PApplet from drawing screen.
	 * @param x X coordinate of triangle.
	 * @param y Y coordinate of triangle.
	 * @param Alarm Alarm object to manipulate.
	 */
	public AlarmWidget(PApplet proccessingObj, int x, int y, Alarm alarm, Controller controller) {
		parent = proccessingObj;
		this.x = x;
		this.y = y;
		this.alarm = alarm;
		this.controller = controller;
		alarmFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US);
	}
	
	/**
	 * Display the alarm widget for the alarm.
	 * @param font Font to use.
	 */
	public void displayAlarmWidget(PFont font) {
		try {
			checkAlarmToggle();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		parent.textFont(font, 35);
		if (alarm.isActive) {
			parent.fill(PRIMARY_COLOR[0], PRIMARY_COLOR[1], PRIMARY_COLOR[2]);
			parent.circle(x + 10, (y - 12), ALARM_RADIUS);	
		}
		parent.fill(255);
		parent.text(alarm.number, x, y);
		parent.text(String.valueOf(alarmFormatter.format(alarm.time)), x - ALARM_OFFSET, y + ALARM_OFFSET);
	}
	
	/**
	 * Displays the alarm edit display.
	 * @param font Font to use
	 * @return True if the user is done editing, false if not done.
	 */
	public boolean displayAlarmEdit(PFont font) {
		parent.textFont(font, 40);
		parent.text(String.format("Set Alarm %s", alarm.number), 280, 50);
		
		parent.textFont(font, 120);
		parent.text(String.valueOf(alarmFormatter.format(alarm.time)), 110, 290);
		

		// Triangle buttons
		parent.fill(PRIMARY_COLOR[0], PRIMARY_COLOR[1], PRIMARY_COLOR[2]);
		for (int i = 0; i < TRIANGLES_X.length; i++) {
			int x = TRIANGLES_X[i];
			TriangleButton t = new TriangleButton(x, TOP_TRIANGLE_Y, TRIANGLE_SIZE, false, i, (Main) parent);
			TriangleButton t2 = new TriangleButton(x, BOTTOM_TRIANGLE_Y, TRIANGLE_SIZE, true, i, (Main) parent);
			t.displayTriangle();
			t2.displayTriangle();
			
			t.change_value(alarm, controller);
			t2.change_value(alarm, controller);
		}
		
		// Back Arrow 
		parent.rect(20, 19, 35, 2);
		parent.triangle(15, 20, 20, 15, 20, 25);
		if (parent.mousePressed && withinRectangle(parent.mouseX, parent.mouseY, 0, 0, 50, 50)) {
			parent.fill(255);
			return true;
		}
		parent.fill(255);
		return false;
	}
	
	/**
	 * If alarm circle is clicked toggle alarm clock.
	 * @throws Exception If wrong alarm number is passed.
	 */
	private void checkAlarmToggle() throws Exception {
		if (parent.mousePressed) {
			double dist = Math.sqrt(Math.pow(x + 10 - parent.mouseX, 2) + Math.pow((y - 10) - parent.mouseY, 2));
			if (dist < ALARM_RADIUS)
				controller.setAlarm(alarm.number, !alarm.isActive);
		}
	}
	
	/**
	 * Checks if an alarm was clicked to change its settings.
	 */
	public boolean checkEditAlarmPressed() {
		boolean inText = withinRectangle(parent.mouseX, parent.mouseY, x - ALARM_OFFSET, y + ALARM_OFFSET - 25, 150, 50);
		return parent.mousePressed && inText;
	}
	
	/**
	 * Determines if a point is within a rectangle.
	 * @param x1 Point x position.
	 * @param y1 Point y position.
	 * @param x2 Rectangle x position.
	 * @param y2 Rectangle y position.
	 * @param width Width of the rectangle.
	 * @param height Height of the rectangle.
	 * @return True if point is within rectangle, false otherwise.
	 */
	private boolean withinRectangle(float x1, float y1, float x2, float y2, float width, float height) {
		
		if (x1 < x2 || x1 > (x2 + width))
			return false;
		
		if (y1 < y2 || y1 > (y2 + height))
			return false;
		
		return true;
	}
}

class TriangleButton {
	
	// X location of triangle.
	int x;
	
	// Y location of triangle.
	int y;
	
	// Size of triangle.
	int size;
	
	// 0 for Hour, 1 for Minute, & 2 for time of day.
	int value;
	
	// True if triangle should be pointing down, false otherwise.
	boolean reverse;
	
	// The third point on the triangle.
	float verticalPoint;
		
	Main parent;
	
	public TriangleButton(int x, int y, int size, boolean reverse, int value, Main proccessingObj) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.value = value;
		this.reverse = reverse;
		verticalPoint = reverse ? y + size : y - size;
		parent = proccessingObj;
	}
	
	/**
	 * Display a triangle on screen.
	 * @param x: X position for triangle starting at the leftmost point
	 * @param y: Y position for triangle starting at the leftmost point
	 * @param size: Size of the triangle.
	 * @param reverse: True to draw the triangle upside, false otherwise.
	 */
	public void displayTriangle() {
		parent.noStroke();
		parent.triangle(x, y, x + size, y, ((x + x + size) / 2), verticalPoint);
		parent.stroke(PRIMARY_COLOR[0], PRIMARY_COLOR[1], PRIMARY_COLOR[2]);
	}
	
	/**
	 * When triangle is clicked changed its value for the alarm.
	 * @param alarm Alarm to manipulate.
	 * @param controller Controller.
	 */
	public void change_value(Alarm alarm, Controller controller) {
		// Have a square around triangle to check if clicked near/inside triangle. 
		float newY = reverse ? verticalPoint - size : verticalPoint;
		boolean isIn = withinRectangle(parent.mouseX, parent.mouseY, x, newY, size, size);
		int inc_dec = reverse ? -1 : 1;
		
		if (isIn && parent.getMouseClicked()) {
			switch(value) {
			case 0:
				controller.setAlarm(alarm.number, alarm.time.plusHours(inc_dec));	
				break;
			case 1:
				controller.setAlarm(alarm.number, alarm.time.plusMinutes(inc_dec));
				break;
			case 2:
				controller.setAlarm(alarm.number, alarm.time.plusHours(12 * inc_dec));
				break;
			}
		}
	}
	
	/**
	 * Determines if a point is within a rectangle.
	 * @param x1 Point x position.
	 * @param y1 Point y position.
	 * @param x2 Rectangle x position.
	 * @param y2 Rectangle y position.
	 * @param width Width of the rectangle.
	 * @param height Height of the rectangle.
	 * @return True if point is within rectangle, false otherwise.
	 */
	private boolean withinRectangle(float x1, float y1, float x2, float y2, float width, float height) {
		
		if (x1 < x2 || x1 > (x2 + width))
			return false;
		
		if (y1 < y2 || y1 > (y2 + height))
			return false;
		
		return true;
	}
}
