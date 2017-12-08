/*
 *	Author:      Valentin Kaelin
 *	Date:        8 déc. 2017
 */
package ch.epfl.cs107.play.game.actor.general;

import java.awt.Color;

import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.game.actor.bike.Wheel;
import ch.epfl.cs107.play.game.actor.crate.Crate;
import ch.epfl.cs107.play.math.Polyline;
import ch.epfl.cs107.play.math.RopeConstraintBuilder;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import jdk.nashorn.internal.ir.BlockLexicalContext;

public class Pendule extends Wheel {
	private Crate c1;
	float blockWidth = 2.0f;
	float blockHeight = 0.5f;

	public Pendule(ActorGame game, boolean fixed, Vector position, float hautDepart, float rayonBoule) {
		super(game, false, new Vector(position.getX() - hautDepart, position.getY()), rayonBoule, Color.DARK_GRAY,
				Color.BLACK, false);
		c1 = new Crate(getOwner(), true, new Vector(position.getX(), position.getY()), blockWidth, blockHeight,
				"stone.broken.4.png");
		RopeConstraintBuilder ropeConstraintBuilder = getOwner().getRopeConstraintBuilder();
		ropeConstraintBuilder.setFirstEntity(c1.getEntity());
		ropeConstraintBuilder.setFirstAnchor(new Vector(blockWidth / 2, blockHeight / 2));
		ropeConstraintBuilder.setSecondEntity(super.getEntity());
		ropeConstraintBuilder.setSecondAnchor(Vector.ZERO);
		ropeConstraintBuilder.setMaxLength(3.0f);
		ropeConstraintBuilder.setInternalCollision(true);
		ropeConstraintBuilder.build();
		getOwner().addActor(this);
	}

	public void draw(Canvas canvas) {
		new ShapeGraphics(new Polyline(new Vector(c1.getPosition().getX() + blockWidth / 2, c1.getPosition().getY()),
				super.getPosition()), null, Color.GRAY, .1f, 1.f, 0).draw(canvas);
		super.draw(canvas);
		c1.draw(canvas);
	}
}
