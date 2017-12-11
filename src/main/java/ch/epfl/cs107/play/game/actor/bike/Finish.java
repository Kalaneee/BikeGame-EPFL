/*
 *	Authors:      Valentin Kaelin - Giulia Murgia
 *	Date:        4 déc. 2017
 */
package ch.epfl.cs107.play.game.actor.bike;

import java.awt.Color;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.math.Circle;
import ch.epfl.cs107.play.math.Contact;
import ch.epfl.cs107.play.math.ContactListener;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Finish extends GameEntity implements Actor {
	private ImageGraphics graphic;
	private boolean win;
	private TextGraphics mainMsg;

	public Finish(ActorGame game, boolean fixed, Vector position, float rayon, String img) {
		super(game, fixed, position);

		// Un game et une position sont des arguments obligatoires pour une
		// GameEntity
		// On ne met pas de bloc catch pour que si la valeur est nulle on ait un
		// arret du programme.
		if (game == null) {
			throw new NullPointerException("Un game est obligatoire !");
		}
		if (position == null) {
			throw new NullPointerException("Une position est obligatoire !");
		}
		if ((rayon == 0f) || (rayon < 0)) {
			throw new IllegalArgumentException("rayon invalide !");
		}

		PartBuilder partBuilder = getEntity().createPartBuilder();
		// Nous mettons un rayon plus grand pour que la hitbox du drapeau touche
		// le Bike qui est Ghost et non pas les
		// uniquement les roues qui ne le sont pas
		Circle circle = new Circle(rayon * 4);
		partBuilder.setShape(circle);
		partBuilder.setGhost(true);
		partBuilder.build();
		graphic = new ImageGraphics(img, rayon * 2.0f, rayon * 2.0f, new Vector(rayon, rayon));
		graphic.setParent(getEntity());
		getOwner().addActor(this);
		mainMsg = new TextGraphics("", 0.1f, Color.RED, Color.BLACK, 0.02f, true, false, new Vector(0.5f, -2.0f), 1.0f,
				100.0f);
		mainMsg.setParent(getOwner().getCanvas());
		mainMsg.setRelativeTransform(Transform.I.translated(0.0f, -1.0f));

		ContactListener listener = new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				if (contact.getOther().isGhost()) {
					win = true;
					mainMsg.setText("LEVEL-PASSED");
				}
			}

			@Override
			public void endContact(Contact contact) {
			}
		};
		getEntity().addContactListener(listener);
	}

	/**
	 * Nous avons besoin de savoir si nous avons gagne ou pas dans BikeGame
	 * 
	 * @return une boolean, true = nous avons gagne
	 */
	public boolean getWin() {
		return win;
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
		if (win) {
			mainMsg.draw(canvas);
		}
	}
}
