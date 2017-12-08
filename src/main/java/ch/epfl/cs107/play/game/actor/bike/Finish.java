/*
 *	Author:      Valentin Kaelin
 *	Date:        4 déc. 2017
 */
package ch.epfl.cs107.play.game.actor.bike;

import java.awt.Color;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.actor.bike.BikeGame;
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
		PartBuilder partBuilder = getEntity().createPartBuilder();
		//Je mets un rayon plus grand pour que la hitbox du drapeau touche le Bike qui est Ghost et non pas les 
		// uniquement les roues qui ne le sont pas
		Circle circle = new Circle(rayon * 4);
		partBuilder.setShape(circle);
		partBuilder.setGhost(true);
		partBuilder.build();
		graphic = new ImageGraphics(img, rayon * 2.0f, rayon * 2.0f, new Vector(rayon, rayon));
		graphic.setParent(getEntity());
		getOwner().addActor(this);
		mainMsg = new TextGraphics("", 0.1f, Color.RED, Color.BLACK, 0.02f, true, false, new Vector(0.5f, -2.0f), 1.0f, 100.0f);
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
	
	public boolean getWin() {
		return win;
	}
	
	public void afficheText() {
		mainMsg.draw(getOwner().getCanvas());
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
//		if (win)
//			BikeGame.mainMsg.draw(canvas);
			//message.draw(getOwner().getCanvas()));
	}
}
