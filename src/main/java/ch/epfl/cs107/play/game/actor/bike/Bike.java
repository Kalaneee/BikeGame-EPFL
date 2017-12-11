/*
 *	Authors:      Valentin Kaelin - Giulia Murgia
 *	Date:        29 nov. 2017
 */
package ch.epfl.cs107.play.game.actor.bike;

import java.awt.Color;

import com.sun.glass.events.KeyEvent;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.math.Contact;
import ch.epfl.cs107.play.math.ContactListener;
import ch.epfl.cs107.play.math.Part;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Bike extends GameEntity implements Actor {
	private boolean bikeLook; // false = droite, true = gauche
	private Wheel roueGauche; // initiallement a gauche
	private Wheel roueDroite; // initialement a droite
	private Player player;
	private boolean aGauche = true;
	private boolean hit;
	private TextGraphics mainMsg;
	private TextGraphics secondMsg;
	private float jumpTimer = 0;
	private ImageGraphics smokeGauche;
	private ImageGraphics smokeDroite;
	private boolean frein;
	// 2 floats pour le timer de l'animation en cas de victoire
	private float animBras = 0;
	private float animBras2 = 150;
	private ContactListener listener;
	// Nous devons la mettre en static pour que la couleur reste la 
	// meme lors du reset et donc de la creation d'un nouveau bike, donc 
	// d'un nouveau player
	private static Color colorPlayer;

	public Bike(ActorGame game, boolean fixed, Vector position, Polygon polygon, float rayon) {
		super(game, fixed, position);

		// Un game et une position sont des arguments obligatoires pour une
		// GameEntity
		// On ne met pas de bloc catch pour que si la valeur est nulle on
		// ait un arret du programme.
		if (game == null) {
			throw new NullPointerException("Un game est obligatoire !");
		}
		if (position == null) {
			throw new NullPointerException("Une position est obligatoire !");
		}
		if ((rayon == 0f) || (rayon < 0)) {
			throw new IllegalArgumentException("Un rayon valide pour la roue doit etre donnee");
		}

		PartBuilder partBuilder = getEntity().createPartBuilder();
		partBuilder.setShape(polygon);
		partBuilder.setGhost(true);
		partBuilder.build();
		// Nous creeons ici un player qui est un ensemble de polyline
		// La couleur par default est le rouge
		player = new Player(colorPlayer == null ? Color.RED : colorPlayer, getEntity(), bikeLook);
		getOwner().addActor(this);
		// Nous creeons les 2 roues du bike, une decalee de 1 a gauche et
		// l'autre de 1 a droite
		roueGauche = new Wheel(game, fixed, new Vector(position.getX() - 1, position.getY()), rayon,
				new Color(0, 0, 0, 0), Color.BLUE, true);
		roueDroite = new Wheel(game, fixed, new Vector(position.getX() + 1, position.getY()), rayon,
				new Color(0, 0, 0, 0), Color.BLUE, true);
		// Nous attons les 2 roues au bike
		roueGauche.attach(getEntity(), new Vector(-1.0f, 0.0f), new Vector(-0.5f, -1.0f));
		roueDroite.attach(getEntity(), new Vector(1.0f, 0.0f), new Vector(0.5f, -1.0f));

		// Nous creeons 2 imageGraphics de Smoke qui apparaissent en cas de
		// freinage
		smokeGauche = new ImageGraphics("smoke.gray.3.png", 1, 1, new Vector(2.6f, 0.1f));
		smokeDroite = new ImageGraphics("smoke.gray.3.png", 1, 1, new Vector(-2f, 0.1f));
		smokeGauche.setParent(getEntity());
		smokeDroite.setParent(getEntity());

		// Nous creeons les 2 messages qui serviront en cas de chute
		mainMsg = new TextGraphics("", 0.1f, Color.RED, Color.BLACK, 0.02f, true, false, new Vector(0.5f, -2.0f), 1.0f,
				100.0f);
		mainMsg.setParent(getOwner().getCanvas());
		mainMsg.setRelativeTransform(Transform.I.translated(0.0f, -1.0f));

		secondMsg = new TextGraphics("", 0.05f, Color.RED, Color.BLACK, 0.02f, true, false, new Vector(0.5f, -6.0f),
				1.0f, 100.0f);
		secondMsg.setParent(getOwner().getCanvas());
		secondMsg.setRelativeTransform(Transform.I.translated(0.0f, -1.0f));

		listener = new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Part other = contact.getOther();

				// Si le bike entre en contact avec un element ghost nous ne le
				// comptons pas, par ex le flag de fin de partie
				if (contact.getOther().isGhost()) {
					return;
				}
				// Nous testons d'abord si les roues sont attachees au velo, elles peuvent 
				// par exemple avoir ete detach ()
				if (roueDroite.wheelAttach() && roueGauche.wheelAttach()) {
					// si contact avec les roues, nous ne les comptons pas :
					if (other == roueGauche.getParts().get(0) || other == roueDroite.getParts().get(0)) {
						return;
					}
				}
				// autrement il y a collision
				hit = true;
			}

			@Override
			public void endContact(Contact contact) {
			}
		};
		getEntity().addContactListener(listener);
		getOwner().setPayLoad(this);
	}

	/**
	 * Methode pour rendre invulnerable le cycliste en cas de victoire
	 */
	protected void deleteListener() {
		getEntity().removeContactListener(listener);
	}

	/**
	 * Methode pour definir la position du bike et de ses 2 roues, nous
	 * utilisons cette methode lors de la teleportation
	 * 
	 * @param pos : un Vector, la nouvelle position du bike
	 */
	public void setPosition(Vector pos) {
		getEntity().setPosition(pos);
		roueDroite.getWheel().setPosition(new Vector(pos.getX() + 1, pos.getY()));
		roueGauche.getWheel().setPosition(new Vector(pos.getX() - 1, pos.getY()));
	}
	
	public void createPlayer(Color c) {
		colorPlayer = c;
		player = new Player(c, getEntity(), bikeLook);
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
		player.playerDraw(canvas);
		// Si nous freinons nous dessinons la smoke
		if (frein) {
			// Nous dessinons la smoke uniquement si la vitesse de la roue
			// non-montrice est superieure a 5 dans un esprit de realisme
			if (aGauche && (Math.abs(roueDroite.getSpeed()) > 5.0f)) {
				smokeGauche.draw(canvas);
			} else if (!aGauche && (Math.abs(roueGauche.getSpeed()) > 5.0f)) {
				smokeDroite.draw(canvas);
			}
		}
	}

	/**
	 * Methode utilisee en cas de chute dans le BikeGame pour annoncer la
	 * defaite
	 */
	protected void afficheText() {
		mainMsg.draw(getOwner().getCanvas());
		secondMsg.draw(getOwner().getCanvas());
	}

	/**
	 * Methode pour savoir si nous avons perdu ou pas
	 * 
	 * @return un boolean, true = il y a une collision
	 */
	protected boolean isHit() {
		return hit;
	}

	/**
	 * Methode pour faire monter le bras puis apres 150 update il fait descendre
	 * le bras a la position initiale
	 */
	protected void celebration() {
		if (animBras <= 150) {
			animBras++;
			player.celebrate(animBras);
		}
		if (animBras >= 150 && animBras2 > 0) {
			animBras2--;
			player.celebrate(animBras2);
		}
	}

	/**
	 * Methode pour tourner le velo, nous inversons donc la boolean
	 */
	private void turnBike() {
		player.TurnBike();
		aGauche = !aGauche;
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void update(float deltaTime) {
		// Nous remettons le frein a false pour ne pas avoir la smoke
		// inutilement
		frein = false;
		// Nous decrementons le timer a chaque update
		// La condition est ici pour eviter que le timer se decremente trop pour
		// rien
		if (jumpTimer > 0)
			jumpTimer--;
		// S'il y a une collision, nous detruisons le velo
		if (hit) {
			this.destroy();
			roueDroite.destroy();
			roueGauche.destroy();
			mainMsg.setText("GAME-OVER");
			secondMsg.setText("PRESS-R-TO-REPLAY");
		}
		// Animation de pedalement
		if (aGauche) {
			player.pedale(roueGauche.getAngularPosition());
		} else {
			player.pedale(-roueDroite.getAngularPosition());
		}
		// Espace = tourner le velo
		if (getOwner().getKeyboard().get(KeyEvent.VK_SPACE).isReleased()) {
			turnBike();
		}
		// Touche du haut pour avancer
		if (getOwner().getKeyboard().get(KeyEvent.VK_UP).isDown()) {
			if (aGauche) {
				roueGauche.power(-20);
			} else {
				roueDroite.power(20);
			}
		}
		// Touche du bas pour freiner
		else if (getOwner().getKeyboard().get(KeyEvent.VK_DOWN).isDown()) {
			frein = true;
			if (aGauche) {
				roueGauche.power(0);
			} else {
				roueDroite.power(0);
			}
		}
		// Si nous n'accelerons pas ou ne freinons pas, les roues sont relachees
		else {
			roueGauche.relax();
			roueDroite.relax();
		}
		// 2 touches suivantes sont la pour pouvoir donner une rotation au bike
		if (getOwner().getKeyboard().get(KeyEvent.VK_LEFT).isDown()) {
			getEntity().applyAngularForce(20.0f);
		}
		if (getOwner().getKeyboard().get(KeyEvent.VK_RIGHT).isDown()) {
			getEntity().applyAngularForce(-20.0f);
		}
		// Touche J = saut
		if (getOwner().getKeyboard().get(KeyEvent.VK_J).isReleased()) {
			// Nous avons mis un timer pour eviter que le joueur puisse
			// passer au-dessus de tous les obstacles en sautant
			if (jumpTimer == 0) {
				// Nous lui donnons une force verticale, ce qui fait sauter le
				// bike
				getEntity().applyImpulse(new Vector(0, 10f), null);
				jumpTimer = 150;
			}
		}
	}
}
