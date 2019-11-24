package Main;

import Controller.Controller;
import Model.ClockModel;
import View.View;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class Main extends PApplet {
	
	public static ClockModel clockModel;
	
	public static Controller controller;
	
	public static View view;
	
	private static boolean mousePressed = false;
	
	private static boolean mouseReleased = false;

	public static void main(String[] args) {	
		PApplet.main("Main.Main");
	}
	
	/**
	 * Set display for screen
	 */
	public void settings() {
		// Size of screen
		size(800, 480);
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
		
		try {
			view.display();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void mousePressed() {
		mousePressed = true;
	}
	
	public void mouseReleased() {
		mouseReleased = true;
	}
	
	public void mouseMoved() {
		mousePressed = false;
		mouseReleased = false;
	}
	
	/**
	 * Check if mouse clicked or not.
	 */
	public boolean getMouseClicked() {
		if (mousePressed && mouseReleased) {
			mousePressed = false;
			mouseReleased = false;
			return true;
		}
		return false;
	}

}
