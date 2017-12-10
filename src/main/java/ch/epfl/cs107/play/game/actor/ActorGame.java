/*
 *	Author:      Valentin Kaelin
 *	Date:        23 nov. 2017
 */
package ch.epfl.cs107.play.game.actor;

import java.awt.Color;
import java.util.ArrayList;

import com.sun.glass.events.KeyEvent;

import ch.epfl.cs107.play.game.Game;
import ch.epfl.cs107.play.game.actor.bike.Bike;
import ch.epfl.cs107.play.game.actor.crate.Crate;
import ch.epfl.cs107.play.game.actor.general.Terrain;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.EntityBuilder;
import ch.epfl.cs107.play.math.Polyline;
import ch.epfl.cs107.play.math.Positionable;
import ch.epfl.cs107.play.math.RevoluteConstraintBuilder;
import ch.epfl.cs107.play.math.RopeConstraintBuilder;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.WheelConstraintBuilder;
import ch.epfl.cs107.play.math.World;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Mouse;
import ch.epfl.cs107.play.window.Window;

public abstract class ActorGame implements Game {
	private Window window;
	private FileSystem fileSystem;
	private World world;
	private ArrayList<Actor> listActor = new ArrayList<Actor>();
	private Vector viewCenter;
	private Vector viewTarget;
	private Vector shiftedViewCenter;
	private Positionable viewCandidate;
	private static final float VIEW_TARGET_VELOCITY_COMPENSATION = 0.2f;
	private static final float VIEW_INTERPOLATION_RATIO_PER_SECOND = 0.1f;
	private static final float VIEW_SCALE = 10.0f;
	// boolean pour mettre le jeu en Pause si = true
	private boolean enPause;
	// boolean pour savoir si nous sommes en train de dessiner un polyline dans le mode pause
	private boolean isDrawing;
	// Une ArrayList de Vector qui represente les points du polyline que nous dessinons,
	// chaque position de points est saisie sur la position de la souris lors
	// du click en "edit-mod" (isDrawing = true).
	private ArrayList<Vector> points = new ArrayList<Vector>();
	// Ce polyline est le polyline constitué des points de l'array ci-dessus
	private Polyline formeTerrain;
	TextGraphics editMsg;

	/**
	 * Methode pour pouvoir interagir avec le clavier dans differentes classes : Bike, BikeGame, ActorGame,...
	 * @return Retourne le clavier
	 */
	public Keyboard getKeyboard() {
		return window.getKeyboard();
	}

	/**
	 * Meme methode que getkeyboard mais cette fois-ci pour la souris, nous l'utilisons que dans cette classe
	 * @return Retourne la souris
	 */
	private Mouse getMouse() {
		return window.getMouse();
	}

	/**
	 * Methode qui nous retourne la window, nous l'utilsons a plusieurs endroits
	 * @return la window
	 */
	public Canvas getCanvas() {
		return window;
	}

	/**
	 * Methode permettant d'ajouter un acteur a l'ArrayList
	 * @param a : un acteur
	 */
	public void addActor(Actor a) {
		listActor.add(a);
	}

	/**
	 * Methode permettant de supprimer un acteur de la liste, nous utilisons cette methode 
	 * dans les methodes destroy
	 * @param a : un acteur
	 */
	public void removeActor(Actor a) {
		listActor.remove(a);
	}

	/**
	 * Methode permettant de destroy tous les acteurs (ce qui les supprime aussi de 
	 * l'ArrayList listActor). Nous utilisons cette methode lors du reset du jeu.
	 */
	public void destroyAllActor() {
		// Nous stockons ici la size de l'ArrayList car celle-ci va varier 
		// lorsque nous supprimerons des elements de celle-ci
		int nbr = listActor.size();
		for (int i = 0; i < nbr; i++) {
			listActor.get(0).destroy();
		}
	}
	
	/**
	 * Methode permettant de fixer la camera sur un positionable
	 * @param c : dans notre cas, le bike
	 */
	public void setViewCandidate(Positionable c) {
		viewCandidate = c;
	}

	public void getPayload() {

	}

	/**
	 * Methode qui se lance au debut du jeu
	 */
	public boolean begin(Window window, FileSystem fileSystem) {
		this.window = window;
		this.fileSystem = fileSystem;
		world = new World();
		world.setGravity(new Vector(0.0f, -9.81f));
		viewCenter = Vector.ZERO;
		viewTarget = Vector.ZERO;
		return true;
	}

	/**
	 * Methode appellee a chaque frame du jeu
	 */
	public void update(float deltaTime) {
		// Pour mettre le jeu en pause nous utilisons la touche P
		if ((window.getKeyboard().get(KeyEvent.VK_P).isReleased())) {
			enPause = !enPause;
		}
		if (!enPause) {
			world.update(deltaTime);
			// On ne peut pas faire de for each parce que les elements sont lies
			// entre eux, donc si nous en supprimons un,
			// on ne sait plus ou aller recuper le prochain, ce qui cause une erreur
			// de compilation
			for (int i = 0; i < listActor.size(); i++) {
				listActor.get(i).update(deltaTime);
			}
			float ratio = (float) Math.pow(VIEW_INTERPOLATION_RATIO_PER_SECOND, deltaTime);
			viewCenter = viewCenter.mixed(viewTarget, 1.0f - ratio);
			shiftedViewCenter = viewCenter;
			Transform viewTransform = Transform.I.scaled(VIEW_SCALE).translated(viewCenter);
			window.setRelativeTransform(viewTransform);
		} else {
			// Nous sommes en Pause
			ImageGraphics background = new ImageGraphics("metal.cracked.5.png", 0.4f, 0.15f, new Vector(4.9f, -2.5f), 1,
					90f);
			background.setParent(window);
			background.draw(window);
			TextGraphics pauseMsg = new TextGraphics("", 0.05f, Color.BLUE, Color.BLACK, 0.02f, true, false,
					new Vector(2.6f, 8.6f), 1.0f, 100.0f);
			pauseMsg.setParent(background);
			pauseMsg.setRelativeTransform(Transform.I.translated(0.0f, -1.0f));
			pauseMsg.setText("PAUSE");
			pauseMsg.draw(window);
			// Nous utilisons la touche T pour nous teleporter pour debug notre niveau
			if (getKeyboard().get(KeyEvent.VK_T).isReleased() && !isDrawing) {
				// Nous parcourons la liste d'acteurs pour trouver le bike
				for (Actor a : listActor) {
					if ((a instanceof Bike)) {
						// Nous changeons la position du bike et des roues
						((Bike) a).setPosition(getMouse().getPosition());
					}
				}
			}
			// 4 conditions pour deplacer la camera si nous sommes en Pause
			if (getKeyboard().get(KeyEvent.VK_RIGHT).isDown() && !isDrawing) {
				shiftedViewCenter = shiftedViewCenter.add((new Vector(0.4f, 0.0f)));
			}
			if (getKeyboard().get(KeyEvent.VK_LEFT).isDown() && !isDrawing) {
				shiftedViewCenter = shiftedViewCenter.add((new Vector(-0.4f, 0.0f)));
			}
			if (getKeyboard().get(KeyEvent.VK_UP).isDown() && !isDrawing) {
				shiftedViewCenter = shiftedViewCenter.add((new Vector(0.0f, 0.4f)));
			}
			if (getKeyboard().get(KeyEvent.VK_DOWN).isDown() && !isDrawing) {
				shiftedViewCenter = shiftedViewCenter.add((new Vector(0.4f, -0.4f)));
			}
			// Si nous appuyons sur C, cela cree une Crate a l'endroit du curseur de la souris
			if (getKeyboard().get(KeyEvent.VK_C).isReleased() && !isDrawing) {
				Crate crate = new Crate(this, false, getMouse().getPosition(), 1, 1, "stone.broken.1.png", 100.0f);
			}
			// Si nous appuyons sur G nous entrons en "edit-mod", pour dessiner un polyline
			if (getKeyboard().get(KeyEvent.VK_G).isReleased()) {
				points.clear();
				isDrawing = true;
				editMsg = new TextGraphics("", 0.05f, Color.BLUE, Color.BLACK, 0.02f, true, false,
						new Vector(2.0f, 7.6f), 1.0f, 100.0f);
				editMsg.setParent(background);
				editMsg.setRelativeTransform(Transform.I.translated(0.0f, -1.0f));
				editMsg.setText("EDIT-MOD");
			}
			// Lorsque nous appuyons sur H, nous quittons l'edit mod et nous dessinons le polyline
			if (getKeyboard().get(KeyEvent.VK_H).isReleased()) {
				isDrawing = false;
				// Nous testons si nous avons pas 0 point ce qui causerait une erreur de compilation
				if (points.size() != 0) {
					// Nous ajoutons le 1er point en dernier pour finir le contour de la forme
					points.add(points.get(0));
					formeTerrain = new Polyline(points);
					Terrain terrain = new Terrain(this, true, Vector.ZERO, formeTerrain, Color.LIGHT_GRAY, Color.BLACK,
							0.1f, null, 100.0f);
				}
			}
			if (isDrawing) {
				editMsg.draw(window);
				// A chaque clique en edit-mod nous ajoutons un point au polyline
				if (getMouse().getLeftButton().isReleased()) {
					points.add(getMouse().getPosition());
				}
			}
			Transform shiftViewTransform = Transform.I.scaled(VIEW_SCALE).translated(shiftedViewCenter);
			window.setRelativeTransform(shiftViewTransform);
		}
		
		if (viewCandidate != null) {
			viewTarget = viewCandidate.getPosition()
					.add(viewCandidate.getVelocity().mul(VIEW_TARGET_VELOCITY_COMPENSATION));
		}
		// Nous dessinons ici tous les acteurs
		for (Actor a : listActor) {
			a.draw(window);
		}
	}

	public void end() {
	}

	/**
	 * les 4mMethodes suivantes sont la pour eviter de faire un 
	 * getter du World entier
	 * @return L'entityBuilder du world
	 */
	public EntityBuilder getEntityBuilder() {
		return world.createEntityBuilder();
	}

	public WheelConstraintBuilder getWheelConstraintBuilder() {
		return world.createWheelConstraintBuilder();
	}

	public RopeConstraintBuilder getRopeConstraintBuilder() {
		return world.createRopeConstraintBuilder();
	}

	public RevoluteConstraintBuilder getRevoluteConstraintBuilder() {
		return world.createRevoluteConstraintBuilder();
	}
}