/*
 *	Authors:      Valentin Kaelin - Giulia Murgia
 *	Date:        8 déc. 2017
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

public class Tremplin extends GameEntity implements Actor {
	private ContactListener listener;
	private ImageGraphics graphic;
	private ImageGraphics graphicExt;
	private float animTimer = 0;
	private boolean hit;

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
				hit = true;
				// Nous prenons ici la vitesse de l'objet qui rencontre le tremplin
				float velocity = other.getEntity().getAngularVelocity();
				impulseTremplin(other, velocity);
				graphicExt = new ImageGraphics("jumper.extended.png", width, height * 2f);
				graphicExt.setParent(getEntity());
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
		// Nous avons deux ImageGraphics differentes pour faire une animation au tremplin
		// lorsque celu-ci entre en contact avec quelque chose
		if (hit && animTimer <= 100) {
			graphicExt.draw(canvas);
			animTimer++;
			if (animTimer == 100) {
				animTimer = 0;
				hit = false;
			}
		} else {
			graphic.draw(canvas);
		}
	}
	
	/**
	 * Cette methode donne un impulse a l'objet qui rencontre le tremplin
	 * @param other : Part en contact avec le tremplin
	 * @param velocity : vitesse de la Part en contact avec le tremplin
	 */
	private void impulseTremplin(Part other, float velocity) {
		// Plus l'objet arrive avec une vitesse importante plus le tremplin l'expulsera haut
		// La velocity negative en x sert juste a donner un mouvement qui va plus vers la droite
		// a l'objet qui rencontre le tremplin (les valeurs ont ete trouvees en tatonnant)
		other.getEntity().applyImpulse(new Vector(-velocity / 4, 10f * (Math.abs(velocity) / 10)), null);
	}

}
