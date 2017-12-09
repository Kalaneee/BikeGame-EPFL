/*
 *	Author:      Valentin Kaelin
 *	Date:        25 nov. 2017
 */
package ch.epfl.cs107.play.game.actor.general;

import java.awt.Color;
import java.util.ArrayList;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.game.actor.bike.Wheel;
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
	private ArrayList<ImageGraphics> bushList = new ArrayList<ImageGraphics>();
	private ContactListener listener;

	public Terrain(ActorGame game, boolean fixed, Vector position, Polyline terrain, Color color, Color colorLigne,
			float tailleLigne, Vector[] bushPos) {
		super(game, fixed, position);

		if (terrain == null) {
			throw new NullPointerException();
		}

		if (color == null) {
			throw new NullPointerException();
		}

		if (colorLigne == null) {
			throw new NullPointerException();
		}
		
		// la taile de la Ligne peut etre de 0
		if ((tailleLigne < 0)) {
			throw new IllegalArgumentException("Une epaisseur de ligne doit etre >= 0");
		}

		PartBuilder partBuilder = getEntity().createPartBuilder();
		partBuilder.setShape(terrain);
		partBuilder.build();
		graphic = new ShapeGraphics(terrain, color, colorLigne, tailleLigne, 1.f, 0);
		graphic.setParent(getEntity());
		// nous testons d'abord si nous volons des buissons pour eviter une erreur de compilation
		if (bushPos != null) {
			for (int i = 0; i < bushPos.length; i++) {
				bushList.add(new ImageGraphics("bush.png", 1.f, 1.0f, bushPos[i], 1, 0));
			}
		}

		getOwner().addActor(this);
		// listener = new ContactListener() {
		// @Override
		// public void beginContact(Contact contact) {
		// Part other = contact.getOther();
		// System.out.println("classe : "+other.getClass());
		// // si contact avec les roues :
		//// if (other instanceof Wheel || other ==
		// roueDroite.getParts().get(0)) {
		//// return;
		//// }
		// }
		//
		// @Override
		// public void endContact(Contact contact) {
		// }
		// };
		// getEntity().addContactListener(listener);
	}

	public Transform getTransform() {
		return getEntity().getTransform();
	}

	public Vector getVelocity() {
		return getEntity().getVelocity();
	}

	public void draw(Canvas canvas) {
		graphic.draw(canvas);
		for (ImageGraphics b : bushList) {
			b.draw(canvas);
		}
	}
	
	// Nous avons besoin de getEntity le terrain pour la Bascule, c'est pour cela
	// que nous l'avons redefinie ici
	protected Entity getEntity() {
		return super.getEntity();
	}
}
