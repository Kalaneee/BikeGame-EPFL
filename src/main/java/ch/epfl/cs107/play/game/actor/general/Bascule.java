/*
 *	Authors:      Valentin Kaelin - Giulia Murgia
 *	Date:        8 déc. 2017
 */
package ch.epfl.cs107.play.game.actor.general;

import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.crate.Crate;
import ch.epfl.cs107.play.math.RevoluteConstraintBuilder;
import ch.epfl.cs107.play.math.Vector;

public class Bascule extends Crate {
	public Bascule(ActorGame game, boolean fixed, Vector position, float width, float height, String image,
			Terrain terrain) {
		super(game, fixed, position, width, height, image);
		RevoluteConstraintBuilder revoluteConstraintBuilder = getOwner().getRevoluteConstraintBuilder();
		revoluteConstraintBuilder.setFirstEntity(terrain.getEntity());
		revoluteConstraintBuilder.setFirstAnchor(new Vector(position.getX() + width / 2, position.getY() + height / 2));
		revoluteConstraintBuilder.setSecondEntity(super.getEntity());
		revoluteConstraintBuilder.setSecondAnchor(new Vector(width / 2, height / 2));
		revoluteConstraintBuilder.setInternalCollision(true);
		revoluteConstraintBuilder.build();
	}
}
