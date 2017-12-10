/*
 *	Authors:      Valentin Kaelin - Giulia Murgia
 *	Date:        24 nov. 2017
 */
package ch.epfl.cs107.play.game.actor.crate;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.math.Entity;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Crate extends GameEntity implements Actor {
	private ImageGraphics graphic;
	
	public Crate(ActorGame game, boolean fixed, Vector position, float width, float height, String image, float friction) {
		super(game, fixed, position);
		
		if ((height == 0f) || (height < 0)) {
			throw new IllegalArgumentException();
		}
		if ((width == 0f) || (width < 0)) {
			throw new IllegalArgumentException();
		}
		if (image == null) {
			throw new NullPointerException();
		}
		if (friction <= 0f) {
			throw new IllegalArgumentException("Une friction doit etre >= 0");
		}
		
		PartBuilder partBuilder = getEntity().createPartBuilder();
		Polygon polygon = new Polygon(Vector.ZERO, new Vector(width, 0.0f), new Vector(width, height),
				new Vector(0.0f, height));
		partBuilder.setShape(polygon);
		partBuilder.setFriction(friction);
		partBuilder.build();
		graphic = new ImageGraphics(image, width, height);
		graphic.setParent(getEntity());
		getOwner().addActor(this);
	}
	
	@Override
	public Transform getTransform() {
		return getEntity().getTransform();
	}
	
	@Override
	public Vector getVelocity() {
		return getEntity().getVelocity();
	}
	
	@Override
	public void draw(Canvas canvas) {
		graphic.draw(canvas);
	}
	
	// Nous devons mettre cette methode public car nous l'utilisons pour créer le pendule qui n'est 
	// pas das le meme packet
	public Entity getEntity() {
		return super.getEntity();
	}
}
