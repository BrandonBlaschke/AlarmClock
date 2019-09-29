package View;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import Controller.Controller;
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
	 * Formatter for displaying time.
	 */
	private DateTimeFormatter timeFormatter;

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
		font = parent.createFont("Arial", 11, true);
		timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss", Locale.US);
	}
	
	/**
	 * Displays the clock screen on the canvas.
	 */
	public void display() {
		parent.background(0);
		
		time = clockModel.getTime();
		
		// Draw text		
		parent.textFont(font, 24);
		parent.fill(255);
		parent.text(String.valueOf(timeFormatter.format(time)), 560, 200);
		
	}
	
	
}
