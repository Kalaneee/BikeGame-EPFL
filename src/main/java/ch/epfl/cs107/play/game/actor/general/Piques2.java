/*
 *	Author:      Valentin Kaelin
 *	Date:        11 déc. 2017
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

public class Piques2 extends GameEntity implements Actor {
	private ImageGraphics graphic;
	private ContactListener listener;

	public Piques2(ActorGame game, boolean fixed, Vector position,  float width, float height, String image) {
		super(game, fixed, position);
		

		PartBuilder partBuilder = getEntity().createPartBuilder();
		Polygon polygon = new Polygon(Vector.ZERO, new Vector(width, 0.0f), new Vector(width, height),
				new Vector(0.0f, height));
		partBuilder.setShape(polygon);
		partBuilder.build();
		graphic = new ImageGraphics(image, width, height);
		graphic.setParent(getEntity());
		getOwner().addActor(this);
		
		listener = new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Part other = contact.getOther();
				System.out.println(other.getEntity());
				other.getEntity().destroy();
			}

			@Override
			public void endContact(Contact contact) {
			}
		};
		getEntity().addContactListener(listener);
	}

	@Override
	public Transform getTransform() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector getVelocity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void draw(Canvas canvas) {
		graphic.draw(canvas);
		
	}

}
