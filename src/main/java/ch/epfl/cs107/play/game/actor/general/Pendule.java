/*
 *	Authors:      Valentin Kaelin - Giulia Murgia
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

public class Pendule extends Wheel {
	private Crate c1;
	float blockWidth = 2.0f;
	float blockHeight = 0.5f;

	public Pendule(ActorGame game, boolean fixed, Vector position, float hautDepart, float rayonBoule, float friction) {
		super(game, false, new Vector(position.getX() - hautDepart, position.getY()), rayonBoule, Color.DARK_GRAY,
				Color.BLACK, false);
		
		// Nous créons une crate qui sera le soutien du pendule
		c1 = new Crate(getOwner(), true, new Vector(position.getX(), position.getY()), blockWidth, blockHeight,
				"stone.broken.4.png", friction);
		
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
		// Nous creeons un polyline qui represente la corde entre la wheel et la crate, le polyline est redefini 
		// a chaque draw et bouge donc en suivant le mouvement de la wheel
		new ShapeGraphics(new Polyline(new Vector(c1.getPosition().getX() + blockWidth / 2, c1.getPosition().getY()),
				super.getPosition()), null, Color.GRAY, .1f, 1.f, 0).draw(canvas);
		super.draw(canvas);
		c1.draw(canvas);
	}
}
