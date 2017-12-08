/*
 *	Author:      Valentin Kaelin
 *	Date:        25 nov. 2017
 */
package ch.epfl.cs107.play.game.actor.general;

import java.awt.Color;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.math.Contact;
import ch.epfl.cs107.play.math.ContactListener;
import ch.epfl.cs107.play.math.Entity;
import ch.epfl.cs107.play.math.Part;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polyline;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Terrain extends GameEntity implements Actor {
	private ShapeGraphics graphic;
	//private ContactListener listener;

	public Terrain(ActorGame game, boolean fixed, Vector position, Polyline terrain, Color color, Color colorLigne) {
		super(game, fixed, position);
		PartBuilder partBuilder = getEntity().createPartBuilder();
		partBuilder.setShape(terrain);
		partBuilder.build();
		graphic = new ShapeGraphics(terrain, color, colorLigne, .15f, 1.f, 0);
		graphic.setParent(getEntity());
		getOwner().addActor(this);
//		listener = new ContactListener() {
//			@Override
//			public void beginContact(Contact contact) {
//				Part other = contact.getOther();
//
//				// si contact avec les roues :
//				if (other == roueGauche.getParts().get(0) || other == roueDroite.getParts().get(0)) {
//					return;
//				}
//			}
//
//			@Override
//			public void endContact(Contact contact) {
//			}
//		};
//		getEntity().addContactListener(listener);
	}

	public Transform getTransform() {
		return getEntity().getTransform();
	}

	public Vector getVelocity() {
		return getEntity().getVelocity();
	}

	public void draw(Canvas canvas) {
		graphic.draw(canvas);
	}
	
	protected Entity getEntity() {
		return super.getEntity();
	}
}
