/*
 *	Authors:      Valentin Kaelin - Giulia Murgia
 *	Date:        12 déc. 2017
 */
package ch.epfl.cs107.play.game.actor.general;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Ascenseur extends GameEntity implements Actor {

	private ImageGraphics graphic;
	private Polygon polygon;
	private Vector position;
	
	private float hMax;
	private float hIni;
	private float increment = 0;
	private float timer = 0;
	private boolean auSol = true;
	private boolean montee;
	private float waitTime;
	private float speed;

	public Ascenseur(ActorGame game, boolean fixed, Vector position, float width, float height, String image,
			float hMax, float waitTime, float speed) {
		super(game, fixed, position);

		if (height <= 0) {
			throw new IllegalArgumentException();
		}
		if (width <= 0) {
			throw new IllegalArgumentException();
		}
		if (image == null) {
			throw new NullPointerException();
		}
		if (hMax <= 0) {
			throw new IllegalArgumentException();
		}
		if (waitTime < 0) {
			throw new IllegalArgumentException();
		}
		if (speed <= 0) {
			throw new IllegalArgumentException();
		}
		
		this.position = position;
		hIni = position.getY();
		this.hMax = hIni + hMax;
		this.waitTime = waitTime;
		this.speed = speed;
		PartBuilder partBuilder = getEntity().createPartBuilder();
		polygon = new Polygon(Vector.ZERO, new Vector(width, 0.0f), new Vector(width, height),
				new Vector(0.0f, height));
		partBuilder.setShape(polygon);
		// Nous mettons beaucoup de frottements pour qu'il soit plus facile de 
		// rester sur l'ascenseur avec le velo
		partBuilder.setFriction(100.0f);
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

	@Override
	public void update(float deltaTime) {
		// Si la variable d'incrementation est a 0, on monte et on est au sol
		if (increment <= 0) {
			montee = true;
			auSol = true;
		}
		// Autrement, si atteint la hauteur maximale, on descend
		else if ((increment + hIni) >= hMax) {
			montee = false;
		}
		// Si on est au sol, on lance l'incrementation du timer
		if (auSol) 
			timer++;
		// Si le timer depasse le temps d'attente choisi, l'ascenseur decole
		if (timer > waitTime) {
			timer = 0;
			auSol = false;
		}
		if (!auSol) {
			if (montee) {
				increment = increment + speed;
			} else {
				increment = increment - speed;
			}
		}
		// On modifie la position de l'ascenseur
		Vector pos = new Vector(position.getX(), (position.getY() + increment));
		this.getEntity().setPosition(pos);
	}

}
