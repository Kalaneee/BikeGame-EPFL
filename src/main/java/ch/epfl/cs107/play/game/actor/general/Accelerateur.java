/*
 *	Authors:      Valentin Kaelin - Giulia Murgia
 *	Date:        12 déc. 2017
 */
package ch.epfl.cs107.play.game.actor.general;

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

public class Accelerateur extends GameEntity implements Actor {
	private ContactListener listener;
	private ImageGraphics graphic;

	public Accelerateur(ActorGame game, boolean fixed, Vector position, float width, float height, String image) {
		super(game, fixed, position);

		PartBuilder partBuilder = getEntity().createPartBuilder();
		Polygon polygon = new Polygon(Vector.ZERO, new Vector(width, 0.0f), new Vector(width, height),
				new Vector(0.0f, height));
		partBuilder.setShape(polygon);
		// Pour ne pas entrer en collision avec
		partBuilder.setGhost(true);
		partBuilder.build();

		graphic = new ImageGraphics(image, width, height);
		graphic.setParent(getEntity());
		getOwner().addActor(this);

		listener = new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Part other = contact.getOther();
				// Nous mettons cette condition pour que l'accelerateur ne
				// fonctionne que dans un sens (de gauche à droite).
				if (other.getEntity().getAngularVelocity() < 0) {
					// nous multiplions sa vitesse angulaire par 3
					other.getEntity().setAngularVelocity((float) (3.0 * other.getEntity().getAngularVelocity()));
				}
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

}
