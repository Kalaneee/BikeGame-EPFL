/*
 *	Authors:      Valentin Kaelin - Giulia Murgia
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

	private ImageGraphics Block;
	private ShapeGraphics Ball;

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {

		this.window = window;
		world = new World();
		world.setGravity(new Vector(0.0f, -9.81f));

		// BLOCK
		// Creer le corps physique "block"
		EntityBuilder entityBuilderBlock = world.createEntityBuilder();
		entityBuilderBlock.setFixed(true);
		entityBuilderBlock.setPosition(new Vector(1.f, 0.5f));
		block = entityBuilderBlock.build();

		// Creer une propriete geometrique pour block
		PartBuilder partBuilderBlock = block.createPartBuilder();
		Polygon polygon = new Polygon(new Vector(0.0f, 0.0f), new Vector(1.0f, 0.0f), new Vector(1.0f, 1.0f),
				new Vector(0.0f, 1.0f));
		partBuilderBlock.setShape(polygon);
		// on ajoute un coefficient de friction au block
		partBuilderBlock.setFriction(0.00000000005f);
		partBuilderBlock.build();

		// Donne une image graphique au block
		Block = new ImageGraphics("stone.broken.4.png", 1, 1);
		Block.setAlpha(1.0f);
		Block.setDepth(0.0f);
		Block.setParent(block);

		// BALL
		// Creer le corps physique "balle"
		EntityBuilder entityBuilderBall = world.createEntityBuilder();
		entityBuilderBall.setPosition(new Vector(0.2f, 4.0f));
		ball = entityBuilderBall.build();

		// Creer une propriete geometrique pour la balle
		PartBuilder partBuilderBall = ball.createPartBuilder();
		Circle circle = new Circle(0.6f, new Vector(0.3f, 4.0f));
		partBuilderBall.setShape(circle);
		partBuilderBall.build();

		// Donne une image graphique a la balle
		Ball = new ShapeGraphics(circle, Color.BLUE, Color.RED, .1f, 1.f, 0);
		Ball.setAlpha(1.0f);
		Ball.setDepth(0.0f);
		Ball.setParent(ball);

		// Construit une contrainte de type "corde" entre le bloc et la balle
		RopeConstraintBuilder ropeConstraintBuilder = world.createRopeConstraintBuilder();
		ropeConstraintBuilder.setFirstEntity(block);
		ropeConstraintBuilder.setFirstAnchor(new Vector(1.0f / 2, 1.0f / 2));
		ropeConstraintBuilder.setSecondEntity(ball);
		ropeConstraintBuilder.setSecondAnchor(Vector.ZERO);
		ropeConstraintBuilder.setMaxLength(3.0f);
		ropeConstraintBuilder.setInternalCollision(true);
		ropeConstraintBuilder.build();

		return true;
	}

	@Override
	public void update(float deltaTime) {

		world.update(deltaTime);

		// place la camera ou on veut
		window.setRelativeTransform(Transform.I.scaled(10.0f));
		Block.draw(window);
		Ball.draw(window);
	}

	// This event is raised after game ends, to release additional resources
	@Override
	public void end() {

	}
}