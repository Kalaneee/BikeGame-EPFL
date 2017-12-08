/*
 *	Author:      Valentin Kaelin
 *	Date:        23 nov. 2017
 */
package ch.epfl.cs107.play.game.actor;

import java.awt.Color;
import java.util.ArrayList;

import com.sun.glass.events.KeyEvent;
import com.sun.glass.events.MouseEvent;

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
	private static Window window;
	private FileSystem fileSystem;
	private World world;
	private ArrayList<Actor> listActor = new ArrayList<Actor>();
	// Viewport properties
	private Vector viewCenter;
	private Vector viewTarget;
	private Vector shiftedViewCenter;
	private Positionable viewCandidate;
	private static final float VIEW_TARGET_VELOCITY_COMPENSATION = 0.2f;
	private static final float VIEW_INTERPOLATION_RATIO_PER_SECOND = 0.1f;
	private static final float VIEW_SCALE = 10.0f;
	// boolean pour mettre le jeu en Pause si = true
	private boolean enPause;
	private boolean isDrawing;
	private ArrayList<Vector> points = new ArrayList<Vector>();
	private Polyline formeTerrain;
	TextGraphics editMsg;

	public Keyboard getKeyboard() {
		return window.getKeyboard();
	}

	public Mouse getMouse() {
		return window.getMouse();
	}

	public Canvas getCanvas() {
		return window;
	}

	public FileSystem getFileSystem() {
		return fileSystem;
	}

	public void addActor(Actor a) {
		listActor.add(a);
	}

	public void removeActor(Actor a) {
		listActor.remove(a);
	}

	// methode permettant de destroy tous les acteurs, ce qui les remove aussi
	// de l'ArrayList.
	// Nous utilisons cette methode lors du reset du jeu
	public void destroyAllActor() {
		int nbr = listActor.size();
		for (int i = 0; i < nbr; i++) {
			listActor.get(0).destroy();
		}
	}

	public void setViewCandidate(Positionable c) {
		viewCandidate = c;
	}
	
	public void getPayload() {
		
	}

	// This event is raised when game has just started
	public boolean begin(Window window, FileSystem fileSystem) {
		this.window = window;
		this.fileSystem = fileSystem;
		world = new World();
		world.setGravity(new Vector(0.0f, -9.81f));

		viewCenter = Vector.ZERO;
		viewTarget = Vector.ZERO;
		// Successfully initiated
		return true;
	}

	// This event is called at each frame
	public void update(float deltaTime) {
		if ((window.getKeyboard().get(KeyEvent.VK_P).isReleased())) {
			enPause = !enPause;
		}
		if (!enPause) {
			world.update(deltaTime);
			// On ne peut pas faire de for each parce que les elements sont lies
			// entre eux, donc si nous en supprimons un,
			// on ne sait plus ou aller recuper le prochain
			for (int i = 0; i < listActor.size(); i++) {
				listActor.get(i).update(deltaTime);
			}
			// Interpolate with previous location
			float ratio = (float) Math.pow(VIEW_INTERPOLATION_RATIO_PER_SECOND, deltaTime);
			viewCenter = viewCenter.mixed(viewTarget, 1.0f - ratio);
			shiftedViewCenter = viewCenter;
			// Compute new viewport
			Transform viewTransform = Transform.I.scaled(VIEW_SCALE).translated(viewCenter);
			window.setRelativeTransform(viewTransform);
		} else {
			ImageGraphics background = new ImageGraphics("metal.cracked.5.png", 0.4f, 0.15f, new Vector(4.9f, -2.5f), 1, 90f);
			background.setParent(window);
			background.draw(window);
//			TextGraphics pauseMsg = new TextGraphics("", 0.05f, Color.BLUE, Color.BLACK, 0.02f, true, false, new Vector(4.0f, 12.0f),
//					1.0f, 100.0f);
			TextGraphics pauseMsg = new TextGraphics("", 0.05f, Color.BLUE, Color.BLACK, 0.02f, true, false, new Vector(2.6f, 8.6f),
					1.0f, 100.0f);
			pauseMsg.setParent(background);
			pauseMsg.setRelativeTransform(Transform.I.translated(0.0f, -1.0f));
			pauseMsg.setText("PAUSE");
			pauseMsg.draw(window);
			if (getKeyboard().get(KeyEvent.VK_T).isReleased() && !isDrawing) {
				for (Actor a : listActor) {
					if ((a instanceof Bike)) {
						a.setPosition(getMouse().getPosition());
					}
					
				}
			}
			
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
			if (getKeyboard().get(KeyEvent.VK_C).isReleased() && !isDrawing) {
				Crate crate = new Crate(this, false, getMouse().getPosition(), 1, 1, "stone.broken.1.png");
			}
			if (getKeyboard().get(KeyEvent.VK_G).isReleased()) {
				points.clear();
				isDrawing = true;
//				editMsg = new TextGraphics("", 0.05f, Color.BLUE, Color.BLACK, 0.02f, true, false, new Vector(2.85f, 11.0f),
//						1.0f, 100.0f);
				editMsg = new TextGraphics("", 0.05f, Color.BLUE, Color.BLACK, 0.02f, true, false, new Vector(2.0f, 7.6f),
						1.0f, 100.0f);
				editMsg.setParent(background);
				editMsg.setRelativeTransform(Transform.I.translated(0.0f, -1.0f));
				editMsg.setText("EDIT-MOD");
			}
			if (getKeyboard().get(KeyEvent.VK_H).isReleased()) {
				isDrawing = false;
				if (points.size() != 0) {
					points.add(points.get(0));
					formeTerrain = new Polyline(points); 
					Terrain terrain = new Terrain(this, true, Vector.ZERO, formeTerrain, Color.LIGHT_GRAY, Color.BLACK);
				}
			}
			if (isDrawing) {
				editMsg.draw(window);
				if (getMouse().getLeftButton().isReleased()) {
					points.add(getMouse().getPosition());
				}
			}
			// Compute shifted viewport
			Transform shiftViewTransform = Transform.I.scaled(VIEW_SCALE).translated(shiftedViewCenter);
			window.setRelativeTransform(shiftViewTransform);
		}

		// Update expected viewport center
		if (viewCandidate != null) {
			viewTarget = viewCandidate.getPosition()
					.add(viewCandidate.getVelocity().mul(VIEW_TARGET_VELOCITY_COMPENSATION));
		}

		for (Actor a : listActor) {
			a.draw(window);
		}
	}

	// This event is raised after game ends, to release additional resources
	public void end() {
		// Empty on purpose, no cleanup required yet
	}

	// Pour eviter de faire un getter du World
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