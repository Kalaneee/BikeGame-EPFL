/*
 *	Authors:      Valentin Kaelin - Giulia Murgia
 *	Date:        21 nov. 2017
 */
package ch.epfl.cs107.play.game.tutorial;

import java.awt.Color;

import ch.epfl.cs107.play.game.Game;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.BasicContactListener;
import ch.epfl.cs107.play.math.Circle;
import ch.epfl.cs107.play.math.Entity;
import ch.epfl.cs107.play.math.EntityBuilder;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.World;
import ch.epfl.cs107.play.window.Window;

public class ContactGame implements Game {

	private Window window;

	private World world;

	private Entity block;
	private Entity ball;

	private ImageGraphics imgBlock;
	private ShapeGraphics imgBall;
	
	// pour etre a l'ecoute de collisions potentielles
	private BasicContactListener contactListener;

	public boolean begin(Window window, FileSystem fileSystem) {

		this.window = window;

		// Le world
		world = new World();
		world.setGravity(new Vector(0.0f, -9.81f));
		EntityBuilder entityBuilder = world.createEntityBuilder();

		// BLOCK
		entityBuilder.setFixed(true);
		entityBuilder.setPosition(new Vector(-5.0f, -1.0f));
		block = entityBuilder.build();

		PartBuilder partBuilderBlock = block.createPartBuilder();
		Polygon polygonBlock = new Polygon(new Vector(0.0f, 0.0f), new Vector(10.0f, 0.0f), new Vector(10.0f, 1.0f),
				new Vector(0.0f, 1.0f));
		partBuilderBlock.setShape(polygonBlock);
		partBuilderBlock.build();

		imgBlock = new ImageGraphics("metal.5.png", 10, 1);
		imgBlock.setAlpha(1.0f);
		imgBlock.setDepth(0.0f);
		imgBlock.setParent(block);

		// BALL
		entityBuilder.setFixed(false);
		entityBuilder.setPosition(new Vector(0.3f, 4.0f));
		ball = entityBuilder.build();

		float ballRadius = 0.5f;
		Vector ballPosition = new Vector(0.0f, 2.0f);
		PartBuilder partBuilderBall = ball.createPartBuilder();
		Circle circle = new Circle(ballRadius, ballPosition);
		partBuilderBall.setShape(circle);
		partBuilderBall.build();

		imgBall = new ShapeGraphics(circle, Color.BLUE, Color.BLUE, .1f, 1, 0);
		imgBall.setAlpha(1.0f);
		imgBall.setDepth(0.0f);
		imgBall.setParent(ball);

		// la balle est receptive aux collisions 
		contactListener = new BasicContactListener();
		ball.addContactListener(contactListener);

		return true;
	}

	// This event is called at each frame
	public void update(float deltaTime) {

		world.update(deltaTime);

		window.setRelativeTransform(Transform.I.scaled(10.0f));

		imgBlock.draw(window);
		imgBall.draw(window);

		// contactListener is associated to ball
		// contactListener.getEntities() returns the list of entities in
		// collision with ball
		int numberOfCollisions = contactListener.getEntities().size();
		if (numberOfCollisions > 0) {
			imgBall.setFillColor(Color.RED);
		}
		imgBall.draw(window);
	}

	// This event is raised after game ends, to release additional resources
	public void end() {
		
	}
}