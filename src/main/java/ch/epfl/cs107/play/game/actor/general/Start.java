/*
 *	Author:      Valentin Kaelin
 *	Date:        8 déc. 2017
 */
package ch.epfl.cs107.play.game.actor.general;

import java.awt.Color;

import com.sun.glass.events.KeyEvent;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Start extends GameEntity implements Actor {
	private TextGraphics mainMsg;
	private TextGraphics secondMsg;
	private boolean playerChoice;
	private ImageGraphics player1;
	private ImageGraphics player2;
	private ImageGraphics player3;

	public Start(ActorGame game, boolean fixed, Vector position) {
		super(game, fixed, position);
		mainMsg = new TextGraphics("", 0.1f, Color.RED, Color.BLACK, 0.02f, true, false, new Vector(0.5f, -2.0f), 1.0f,
				100.0f);
		mainMsg.setParent(getOwner().getCanvas());
		mainMsg.setRelativeTransform(Transform.I.translated(0.0f, -1.0f));
		mainMsg.setText("CHOOSE-COLOR");

		secondMsg = new TextGraphics("", 0.05f, Color.RED, Color.BLACK, 0.02f, true, false, new Vector(0.5f, -6.0f),
				1.0f, 100.0f);
		secondMsg.setParent(getOwner().getCanvas());
		secondMsg.setRelativeTransform(Transform.I.translated(0.0f, -1.0f));
		secondMsg.setText("TEST-2");
		player1 = new ImageGraphics("arrow.png", 2.25f, 0.5f);
		player1.setAnchor(new Vector(0.0f, -5.0f));
		player1.setParent(getEntity());
		getOwner().addActor(this);
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
		player1.draw(canvas);
	}

	public boolean playerChoice() {
		return playerChoice;
	}

	public void afficheText() {
		mainMsg.draw(getOwner().getCanvas());
		secondMsg.draw(getOwner().getCanvas());
	}

	@Override
	public void update(float deltaTime) {
		System.out.println("im iinn");
		afficheText();
		player1.draw(getOwner().getCanvas());
		if (getOwner().getKeyboard().get(KeyEvent.VK_J).isReleased()) {
			playerChoice = true;
		}
	}
}
