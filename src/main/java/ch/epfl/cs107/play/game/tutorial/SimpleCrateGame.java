/*
 *	Authors:      Valentin Kaelin - Giulia Murgia
 *	Date:        20 nov. 2017
 */
package ch.epfl.cs107.play.game.tutorial;

import ch.epfl.cs107.play.game.Game;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Entity;
import ch.epfl.cs107.play.math.EntityBuilder;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.World;
import ch.epfl.cs107.play.window.Window;

public class SimpleCrateGame implements Game {

	// Store context
	private Window window;

	// We need our physics engine
	private World world;

	// And we need to keep references on our game objects
	private Entity block;

	private Entity crate;

	// graphical representation of the body
	private ImageGraphics imgBlock;
	private ImageGraphics imgCrate;

	// This event is raised when game has just started
	public boolean begin(Window window, FileSystem fileSystem) {

		this.window = window;

		// Le world
		world = new World();
		world.setGravity(new Vector(0.0f, -9.81f));
		EntityBuilder entityBuilder = world.createEntityBuilder();

		// BLOCK
		// nous creeons un corps physique pour notre bloc:
		entityBuilder.setFixed(true);
		entityBuilder.setPosition(new Vector(1.0f, 0.5f));
		block = entityBuilder.build();
		
		// on associe des fixtures (proprietes geometriques) a notre bloc:
		PartBuilder partBuilderBlock = block.createPartBuilder();
		Polygon polygonBlock = new Polygon(new Vector(0.0f, 0.0f), new Vector(1.0f, 0.0f), new Vector(1.0f, 1.0f),
				new Vector(0.0f, 1.0f));
		partBuilderBlock.setShape(polygonBlock);
		partBuilderBlock.setFriction(0.5f);
		partBuilderBlock.build();
		
		// representaton graphique associee au body:
		imgBlock = new ImageGraphics("stone.broken.4.png", 1, 1);
		imgBlock.setAlpha(1.0f);
		imgBlock.setDepth(0.0f);
		imgBlock.setParent(block);

		// CRATE
		// nous creeons un corps physique pour notre crate:
		entityBuilder.setFixed(false);
		entityBuilder.setPosition(new Vector(0.2f, 4.0f));
		crate = entityBuilder.build();
		
		// on associe des fixtures (proprietes geometriques) a notre crate:
		PartBuilder partBuilderCrate = crate.createPartBuilder();
		Polygon polygonCrate = new Polygon(new Vector(0.0f, 0.0f), new Vector(1.0f, 0.0f), new Vector(1.0f, 1.0f),
				new Vector(0.0f, 1.0f));
		partBuilderCrate.setShape(polygonCrate);
		partBuilderCrate.build();

		// representaton graphique associee au body:
		imgCrate = new ImageGraphics("box.4.png", 1, 1);
		imgCrate.setAlpha(1.0f);
		imgCrate.setDepth(0.0f);
		imgCrate.setParent(crate);

		return true;
	}

	// This event is called at each frame
	public void update(float deltaTime) {

		
		world.update(deltaTime);

		window.setRelativeTransform(Transform.I.scaled(10.0f));

		imgBlock.draw(window);
		imgCrate.draw(window);
	}

	
	public void end() {
		
	}
}