
import Controller.Controller;
import Model.ClockModel;
import View.View;
import processing.core.PApplet;

public class Main extends PApplet {
	
	public static ClockModel clockModel;
	
	public static Controller controller;
	
	public static View view;

	public static void main(String[] args) {	
		PApplet.main("Main");
	}
	
	/**
	 * Set display for screen
	 */
	public void settings() {
		// Size of screen
		size(800,400);
	}
	
	/**
	 * Setup variables
	 */
	public void setup() {
		clockModel = new ClockModel(3);
		controller = new Controller(clockModel);
		view = new View(clockModel, controller, this);
	}
	
	/**
	 * Draws objects on screen 
	 */
	public void draw() {
		clear();
		
		view.display();
	}

}
