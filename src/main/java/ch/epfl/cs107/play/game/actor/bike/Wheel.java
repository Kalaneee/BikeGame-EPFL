/*
 *	Author:      Valentin Kaelin
 *	Date:        30 nov. 2017
 */
package ch.epfl.cs107.play.game.actor.bike;

import java.awt.Color;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.GameEntity;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.math.Circle;
import ch.epfl.cs107.play.math.Entity;
import ch.epfl.cs107.play.math.PartBuilder;
import ch.epfl.cs107.play.math.Polyline;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.WheelConstraint;
import ch.epfl.cs107.play.math.WheelConstraintBuilder;
import ch.epfl.cs107.play.window.Canvas;

public class Wheel extends GameEntity implements Actor {
	//private boolean arriere; // true = roue arrière -> à gauche, false = roue avant -> à droite
	private WheelConstraint constraint;
	private ShapeGraphics graphic;
	private ShapeGraphics imgLine;
	private final float MAX_WHEEL_SPEED = 20.f;
	private boolean ligne;

	public Wheel(ActorGame game, boolean fixed, Vector position, float rayon, Color colorInt, Color colorExt, boolean ligne) {
		super(game, fixed, position);
		this.ligne = ligne;
		PartBuilder partBuilder = getEntity().createPartBuilder();
		Circle circle = new Circle(rayon);
		partBuilder.setShape(circle);
		partBuilder.setFriction(10f);
		partBuilder.build();
		//graphic = new ImageGraphics(img, rayon * 2.0f, rayon * 2.0f, new Vector(rayon, rayon));
		graphic = new ShapeGraphics(circle, colorInt, colorExt, .1f, 1.f, 0);
		//Polyline line = new Polyline(position , new Vector(position.getX() , position.getY()));
		if (ligne) {
			Polyline line = new Polyline(Vector.ZERO, new Vector(0.0f, rayon));
			imgLine = new ShapeGraphics(line, Color.BLACK, Color.BLACK, .1f, 1.f, 0);
			imgLine.setParent(getEntity());
		}
		graphic.setParent(getEntity());
		getOwner().addActor(this);
	}

	public Transform getTransform() {
		return getEntity().getTransform();
	}
	
	public Entity getEntity2() {
		return this.getEntity();
	}
	
	public Vector getVelocity() {
		return getEntity().getVelocity();
	}

	public void draw(Canvas canvas) {
		graphic.draw(canvas);
		if (ligne) {
			imgLine.draw(canvas);
		}
	}
	
	public void attach(Entity vehicle, Vector anchor, Vector axis) {
		WheelConstraintBuilder constraintBuilder = getOwner().getWheelConstraintBuilder();
		constraintBuilder.setFirstEntity(vehicle);
		// point d'ancrage du véhicule : 
		constraintBuilder.setFirstAnchor(anchor);
		// Entity associée à la roue :
		constraintBuilder.setSecondEntity(getEntity());
		// point d'ancrage de la roue (son centre):
		constraintBuilder.setSecondAnchor(Vector.ZERO);
		// axe le long duquel la roue peut se déplacer :
		constraintBuilder.setAxis(axis);
		// fréquence du ressort associé
		constraintBuilder.setFrequency(3.0f); 
		constraintBuilder.setDamping(0.5f);
		// force angulaire maximale pouvant être appliquée //à la roue pour la faire tourner : 
		constraintBuilder.setMotorMaxTorque(10.0f); 
		constraint = constraintBuilder.build();
	}
	
	public void power(float speed) {
		if (this.getSpeed() < MAX_WHEEL_SPEED) {
			constraint.setMotorEnabled(true);
			constraint.setMotorSpeed(speed);
		}
	}
	
	public void relax() {
		constraint.setMotorEnabled(false);
	}
	
	public void detach() {
		constraint.destroy();
	}
	
	public float getAngularPosition() {
		return getEntity().getAngularPosition();
	}
	
	/**
	 * @return relative rotation speed, in radians per second
	 */
	public float getSpeed() {
		return (getEntity().getAngularVelocity());
	}
}