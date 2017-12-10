/*
 *	Authors:      Valentin Kaelin - Giulia Murgia
 *	Date:        24 nov. 2017
 */
package ch.epfl.cs107.play.game.actor.crate;

import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Window;

public class CrateGame extends ActorGame {
	private Vector pos1 = new Vector(0.0f, 5.0f);
	private Vector pos2 = new Vector(0.2f, 7.0f);
	private Vector pos3 = new Vector(2.0f, 6.0f);
	private boolean fixed = false;
	private Crate c1;
	private Crate c2;
	private Crate c3;
	private float friction = 100.0f;
	
	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);
		c1 = new Crate(this, fixed, pos1, 1, 1, "stone.broken.4.png", friction);
		c2 = new Crate(this, fixed, pos2, 1, 1,"stone.broken.1.png", friction);
		c3 = new Crate(this, fixed, pos3, 1, 1, "stone.broken.2.png", friction);
		return true;
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
	}
}
