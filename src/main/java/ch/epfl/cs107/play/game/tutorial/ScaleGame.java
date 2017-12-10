/*
 *	Authors:      Valentin Kaelin - Giulia Murgia
 *	Date:        20 nov. 2017
 */
package ch.epfl.cs107.play.game.tutorial;

import com.sun.glass.events.KeyEvent;

import ch.epfl.cs107.play.game.Game;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Circle;
import ch.epfl.cs107.play.math.Entity;
import ch.epfl.cs107.play.math.EntityBuilder;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.RevoluteConstraintBuilder;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.World;
import ch.epfl.cs107.play.window.Window;

public class ScaleGame implements Game {

	private Window window;

	private World world;

	private Entity block;
	private Entity ball;
	private Entity plank;

	private ImageGraphics imgBlock;
	private ImageGraphics imgPlank;
	private ImageGraphics imgBall;

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
		partBuilderBlock.setFriction(0.5f);
		partBuilderBlock.build();

		imgBlock = new ImageGraphics("stone.broken.4.png", 10, 1);
		imgBlock.setAlpha(1.0f);
		imgBlock.setDepth(0.0f);
		imgBlock.setParent(block);

		// PLANK
		entityBuilder.setFixed(false);
		entityBuilder.setPosition(new Vector(-2.5f, 0.8f));
		plank = entityBuilder.build();

		PartBuilder partBuilderPlank = plank.createPartBuilder();
		Polygon polygonPlank = new Polygon(new Vector(0.0f, 0.0f), new Vector(5.0f, 0.0f), new Vector(5.0f, 0.2f),
				new Vector(0.0f, 0.2f));
		partBuilderPlank.setShape(polygonPlank);
		partBuilderPlank.setFriction(1.0f);
		partBuilderPlank.build();

		imgPlank = new ImageGraphics("wood.3.png", 5, 0.2f);
		imgPlank.setAlpha(1.0f);
		imgPlank.setDepth(0.0f);
		imgPlank.setParent(plank);

		// BALL
		entityBuilder.setFixed(false);
		entityBuilder.setPosition(new Vector(0.5f, 4.f));
		ball = entityBuilder.build();

		float ballRadius = 0.5f;
		PartBuilder partBuilderBall = ball.createPartBuilder();
		Circle circle = new Circle(ballRadius, Vector.ZERO);
		partBuilderBall.setShape(circle);
		partBuilderBall.setFriction(1.0f);
		partBuilderBall.build();

		imgBall = new ImageGraphics("explosive.11.png", 2.0f * ballRadius, 2.0f * ballRadius, new Vector(0.5f, 0.5f));
		imgBall.setAlpha(1.0f);
		imgBall.setDepth(0.0f);
		imgBall.setParent(ball);

		// On lie la planche au block avec la contrainte suivante
		float plankWidth = 5f;
		float plankHeight = 0.2f;
		float blockWidth = 10f;
		float blockHeight = 1.0f;
		RevoluteConstraintBuilder revoluteConstraintBuilder = world.createRevoluteConstraintBuilder();
		revoluteConstraintBuilder.setFirstEntity(block);
		revoluteConstraintBuilder.setFirstAnchor(new Vector(blockWidth / 2, (blockHeight * 7) / 4));
		revoluteConstraintBuilder.setSecondEntity(plank);
		revoluteConstraintBuilder.setSecondAnchor(new Vector(plankWidth / 2, plankHeight / 2));
		revoluteConstraintBuilder.setInternalCollision(true);
		revoluteConstraintBuilder.build();

		return true;
	}

	// This event is called at each frame
	public void update(float deltaTime) {

		// commandes permettant d agir sur la balle
		if (window.getKeyboard().get(KeyEvent.VK_LEFT).isDown()) {
			ball.applyAngularForce(10.0f);

		} else if (window.getKeyboard().get(KeyEvent.VK_RIGHT).isDown()) {
			ball.applyAngularForce(-10.0f);
		}

		world.update(deltaTime);

		window.setRelativeTransform(Transform.I.scaled(10.0f));

		imgBlock.draw(window);
		imgBall.draw(window);
		imgPlank.draw(window);
	}

	// This event is raised after game ends, to release additional resources
	public void end() {

	}
}