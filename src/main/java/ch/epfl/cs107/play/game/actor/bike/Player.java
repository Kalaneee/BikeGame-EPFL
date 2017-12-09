/*
 *	Author:      Giulia Murgia
 *	Date:        01.11.2017
 */

package ch.epfl.cs107.play.game.actor.bike;

import java.awt.Color;

import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.math.Circle;
import ch.epfl.cs107.play.math.Entity;
import ch.epfl.cs107.play.math.Polyline;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Player {
	private boolean sensMarche;

	private ShapeGraphics imgHead;
	private ShapeGraphics imgBody;
	private ShapeGraphics imgArm;
	private ShapeGraphics imgThight1;
	private ShapeGraphics imgThight2;
	private ShapeGraphics imgLeg1;
	private ShapeGraphics imgLeg2;

	private Circle pHead;
	private Polyline pBody;
	private Polyline pArm;
	private Polyline pThight1;
	private Polyline pThight2;
	private Polyline pLeg1;
	private Polyline pLeg2;

	public Player(Color playerColor, Entity bike, boolean sensMarche) {
		
		try {
			if (playerColor == null) {
				throw new NullPointerException("Valeur indispensable !");
			}
			if (bike == null) {
				throw new NullPointerException("Valeur indispensable !");
			}
		}
		finally {
		}
		
		// Draw player head;
		pHead = new Circle(0.2f, getHeadLocation());
		imgHead = new ShapeGraphics(pHead, null, Color.RED, .1f, 1.f, 0);
		imgHead.setParent(bike);

		// Draw player arm;
		pArm = new Polyline(getShoulderLocation(), getHandLocation());
		imgArm = new ShapeGraphics(pArm, Color.WHITE, Color.RED, .1f, 1.f, 0);
		imgArm.setParent(bike);

		// Draw player body;
		pBody = new Polyline(getShoulderLocation(), getHipLocation());
		imgBody = new ShapeGraphics(pBody, Color.WHITE, Color.RED, .1f, 1.f, 0);
		imgBody.setParent(bike);

		// Draw player thight1;
		pThight1 = new Polyline(getHipLocation(), getKnee1Location());
		imgThight1 = new ShapeGraphics(pThight1, Color.WHITE, Color.RED, .1f, 1.f, 0);
		imgThight1.setParent(bike);

		// Draw player thight2;
		pThight2 = new Polyline(getHipLocation(), getKnee2Location());
		imgThight2 = new ShapeGraphics(pThight2, Color.WHITE, Color.RED, .1f, 1.f, 0);
		imgThight2.setParent(bike);

		// Draw player leg1;
		pLeg1 = new Polyline(getKnee1Location(), getFoot1Location());
		imgLeg1 = new ShapeGraphics(pLeg1, Color.WHITE, Color.RED, .1f, 1.f, 0);
		imgLeg1.setParent(bike);

		// Draw player leg2;
		pLeg2 = new Polyline(getKnee2Location(), getFoot2Location());
		imgLeg2 = new ShapeGraphics(pLeg2, Color.WHITE, Color.RED, .1f, 1.f, 0);
		imgLeg2.setParent(bike);

	}

	// Nous avons decider de draw le player ici et non pas dans ActorGame car selon nous, le Player n'est pas un 
	// Actor a part entiere, seulement une image au-dessus de l'entity bike
	protected void playerDraw(Canvas canvas) {
		imgArm.draw(canvas);
		imgHead.draw(canvas);
		imgBody.draw(canvas);
		imgThight1.draw(canvas);
		imgThight2.draw(canvas);
		imgLeg1.draw(canvas);
		imgLeg2.draw(canvas);
	}

	/**
	 * Methode qui inverse la boolean indiquant la direction du velo et appelle la methode 
	 *  pour le redessiner
	 */
	protected void TurnBike() {
		if (!sensMarche) { // regarde a droite
			sensMarche = true; // regarde a gauche
			reDraw();
		} else if (sensMarche) {
			sensMarche = false;
			reDraw();
		}

	}

	/**
	 * Methode permettant de redessiner le Player lorsque l'on change de direction
	 */
	private void reDraw() {
		imgArm.setShape(new Polyline(getShoulderLocation(), getHandLocation()));
		imgBody.setShape(new Polyline(getShoulderLocation(), getHipLocation()));
		imgThight1.setShape(new Polyline(getHipLocation(), getKnee1Location()));
		imgThight2.setShape(new Polyline(getHipLocation(), getKnee2Location()));
		imgLeg1.setShape(new Polyline(getKnee1Location(), getFoot1Location()));
		imgLeg2.setShape(new Polyline(getKnee2Location(), getFoot2Location()));
	}

	/**
	 * Methode affichant l'animation du bras qui monte puis descend lorsque l'on a reussi un niveau 
	 * @param time : un timer qui s'incremente puis decremente
	 */
	protected void celebrate(float time) {
		// Nous faisons ici une rotation du bras autour de l'epaule
		imgArm.setRelativeTransform(Transform.I.rotated(time / 150.0f, getShoulderLocation()));
	}

	/**
	 * Methode qui redefinit les cuisses/jambes du cycliste lors du pedalement
	 * @param position : la position angulaire de la roue
	 */
	protected void pedale(float position) {
		Vector posKnee1 = getKnee1Location(position);
		Vector posKnee2 = getKnee2Location(position);
		Vector posFoot1 = getFoot1Location(position);
		Vector posFoot2 = getFoot2Location(position);

		imgThight1.setShape(new Polyline(getHipLocation(), posKnee1));
		imgThight2.setShape(new Polyline(getHipLocation(), posKnee2));
		imgLeg1.setShape(new Polyline(posKnee1, posFoot1));
		imgLeg2.setShape(new Polyline(posKnee2, posFoot2));
	}

	/**
	 * Recalcule les positions des membres selon la direction du cycliste. S'il
	 * regarde a droite, les coordonées en abcisse sont conservees, s'il regarde
	 * a gauche elles sont inversees.
	 * 
	 * @param x: un float, la position en x.
	 * @return : un float, la nouvelle position selon la direction du cycliste.
	 */
	public float Direction(float x) {
		if (sensMarche) {
			return -x;
		} else {
			return x;
		}
	}

	/**
	 * Les methodes qui suivent sont des getters des positions necessaires pour dessiner le player
	 * @return un Vector, la position de la tete ou du genou par ex, cette valeur sera multipliée par
	 *         (-1) si le cycliste regarde vers la gauche.
	 */
	public Vector getHeadLocation() {
		return new Vector(Direction(0.0f), 1.75f);
	}

	public Vector getShoulderLocation() {
		return new Vector(Direction(-0.15f), 1.5f);
	}

	public Vector getHandLocation() {
		return new Vector(Direction(0.65f), 0.9f);
	}

	public Vector getHipLocation() {
		return new Vector(Direction(-0.5f), 0.9f);
	}

	public Vector getKnee1Location() {
		return new Vector(Direction(0.2f), 0.5f);
	}

	/**
	 * Nous avons surcharge certains getters pour l'animation du pedalement
	 * @param pos : la position angulaire de la roue
	 * @return un Vector, la nouvelle position du genou, du pieds etc
	 */
	public Vector getKnee1Location(float pos) {
		// Les valeurs des getters pour le pedalement ont ete trouvees en 
		// tatonnant et en utilisant la trigonometrie
		float x = (float) (0.2 + Math.cos(pos) / 6);
		float y = (float) (0.5 + Math.sin(pos) / 6);
		return new Vector(Direction(x), y);
	}

	public Vector getKnee2Location() {
		return new Vector(Direction(0.1f), 0.4f);
	}

	public Vector getKnee2Location(float pos) {
		float x = (float) (0.1 - (Math.cos(pos) / 6));
		float y = (float) (0.4 - (Math.sin(pos) / 6));
		return new Vector(Direction(x), y);
	}

	public Vector getFoot1Location() {
		return new Vector(Direction(0.1f), 0.0f);
	}

	public Vector getFoot1Location(float pos) {
		float x = (float) (0.1 + (Math.cos(pos) / 6));
		float y = (float) (0.0 + (Math.sin(pos) / 6));
		return new Vector(Direction(x), y);
	}

	public Vector getFoot2Location() {
		return new Vector(Direction(-0.1f), 0.0f);
	}

	public Vector getFoot2Location(float pos) {
		float x = (float) (-0.1 - (Math.cos(pos) / 6));
		float y = (float) (0.0 - (Math.sin(pos) / 6));
		return new Vector(Direction(x), y);
	}
}