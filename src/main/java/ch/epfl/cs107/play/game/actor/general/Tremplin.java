/*
 *	Author:      Valentin Kaelin
 *	Date:        8 déc. 2017
 */
package ch.epfl.cs107.play.game.actor.general;

import javax.print.attribute.standard.MediaSize.Other;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.math.Contact;
import ch.epfl.cs107.play.math.ContactListener;
import ch.epfl.cs107.play.math.Part;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Tremplin extends GameEntity implements Actor {
	private ContactListener listener;
	private ImageGraphics graphic;

	public Tremplin(ActorGame game, boolean fixed, Vector position, float width, float height, String image) {
		super(game, fixed, position);
		PartBuilder partBuilder = getEntity().createPartBuilder();
		Polygon polygon = new Polygon(Vector.ZERO, new Vector(width, 0.0f), new Vector(width, height),
				new Vector(0.0f, height));
		partBuilder.setShape(polygon);
		partBuilder.build();
		// Nous devons multiplier la height par 2 car l'image choisie est a
		// moitie vide
		graphic = new ImageGraphics(image, width, height * 2);
		graphic.setParent(getEntity());
		getOwner().addActor(this);

		listener = new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Part other = contact.getOther();
				float velocity = other.getEntity().getAngularVelocity();
				applyForce(other, velocity);
			}

			@Override
			public void endContact(Contact contact) {
			}
		};
		getEntity().addContactListener(listener);
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

	public void applyForce(Part other, float velocity) {
		System.out.println(velocity);
		other.getEntity().applyImpulse(new Vector(-velocity/4, 10f*(Math.abs(velocity)/10)), null);
	}

}
