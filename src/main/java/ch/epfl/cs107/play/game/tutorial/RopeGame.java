/*
 *	Author:      Valentin Kaelin
 *	Date:        20 nov. 2017
 */
package ch.epfl.cs107.play.game.tutorial;

import java.awt.Color;

import ch.epfl.cs107.play.game.Game;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Circle;
import ch.epfl.cs107.play.math.Entity;
import ch.epfl.cs107.play.math.EntityBuilder;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.RopeConstraintBuilder;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.World;
import ch.epfl.cs107.play.window.Window;

public class RopeGame implements Game {

	private Window window;

	private World world;

	private Entity block;
	private Entity ball;

	private ImageGraphics imgBlock;
	private ShapeGraphics imgBall;

	public boolean begin(Window window, FileSystem fileSystem) {

		this.window = window;

		// Le world
		world = new World();
		world.setGravity(new Vector(0.0f, -9.81f));
		EntityBuilder entityBuilder = world.createEntityBuilder();

		// BLOCK
		entityBuilder.setFixed(true);
		entityBuilder.setPosition(new Vector(1.0f, 0.5f));
		block = entityBuilder.build();

		PartBuilder partBuilderBlock = block.createPartBuilder();
		Polygon polygonBlock = new Polygon(new Vector(0.0f, 0.0f), new Vector(1.0f, 0.0f), new Vector(1.0f, 1.0f),
				new Vector(0.0f, 1.0f));
		partBuilderBlock.setShape(polygonBlock);
		partBuilderBlock.setFriction(0.5f);
		partBuilderBlock.build();

		imgBlock = new ImageGraphics("stone.broken.4.png", 1, 1);
		imgBlock.setAlpha(1.0f);
		imgBlock.setDepth(0.0f);
		imgBlock.setParent(block);

		// BALL
		entityBuilder.setFixed(false);
		entityBuilder.setPosition(new Vector(0.3f, 4.0f));
		ball = entityBuilder.build();

		float ballRadius = 0.6f;
		Vector ballPosition = new Vector(0.3f, 4.0f);
		PartBuilder partBuilderBall = ball.createPartBuilder();
		Circle circle = new Circle(ballRadius, ballPosition);
		partBuilderBall.setShape(circle);
		partBuilderBall.build();

		imgBall = new ShapeGraphics(circle, Color.BLUE, Color.RED, .1f, 1.f, 0);
		imgBall.setAlpha(1.0f);
		imgBall.setDepth(0.0f);
		imgBall.setParent(ball);

		// LIER LA BALLE AU BLOCK
		float blockWidth = 1.0f;
		float blockHeight = 1.0f;
		RopeConstraintBuilder ropeConstraintBuilder = world.createRopeConstraintBuilder();
		ropeConstraintBuilder.setFirstEntity(block);
		ropeConstraintBuilder.setFirstAnchor(new Vector(blockWidth / 2, blockHeight / 2));
		ropeConstraintBuilder.setSecondEntity(ball);
		ropeConstraintBuilder.setSecondAnchor(Vector.ZERO);
		ropeConstraintBuilder.setMaxLength(3.0f);
		ropeConstraintBuilder.setInternalCollision(true);
		ropeConstraintBuilder.build();

		return true;
	}

	// This event is called at each frame
	public void update(float deltaTime) {

		// The actual rendering will be done now, by the program loop

		// Game logic comes here
		// Nothing to do, yet

		// Simulate physics
		// Our body is fixed, though, nothing will move
		world.update(deltaTime);

		// we must place the camera where we want
		// We will look at the origin (identity) and increase the view size a
		// bit
		window.setRelativeTransform(Transform.I.scaled(10.0f));

		// We can render our scene now,
		imgBlock.draw(window);
		imgBall.draw(window);
	}

	// This event is raised after game ends, to release additional resources
	public void end() {
		// Empty on purpose, no cleanup required yet
	}

}
