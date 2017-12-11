/*
 *	Authors:      Valentin Kaelin - Giulia Murgia
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
	private WheelConstraint constraint;
	private ShapeGraphics graphic;
	private ShapeGraphics imgLine;
	private final float MAX_WHEEL_SPEED = 20.f;
	private boolean ligne;

	public Wheel(ActorGame game, boolean fixed, Vector position, float rayon, Color colorInt, Color colorExt, boolean ligne) {
		super(game, fixed, position);
		
		// Un game et une position sont des arguments obligatoires pour une GameEntity
		// On ne met pas de bloc catch pour que si la valeur est nulle on ait un arret du programme.
		
		if (rayon <= 0f) {
			throw new IllegalArgumentException();
		}
		
		if (colorInt == null) {
			throw new NullPointerException();
		}
		
		if (colorExt == null) {
			throw new NullPointerException();
		}
		
		this.ligne = ligne;
		PartBuilder partBuilder = getEntity().createPartBuilder();
		Circle circle = new Circle(rayon);
		partBuilder.setShape(circle);
		partBuilder.setFriction(100f);
		partBuilder.build();
		graphic = new ShapeGraphics(circle, colorInt, colorExt, .1f, 1.f, 0);
		// la ligne represente le rayon dans la roue
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
	
	public Vector getVelocity() {
		return getEntity().getVelocity();
	}

	public void draw(Canvas canvas) {
		graphic.draw(canvas);
		if (ligne) {
			imgLine.draw(canvas);
		}
	}
	
	// Nous utilisons cette methode dans ActorGame, pour teleporter le velo
	public Entity getEntity2() {
		return this.getEntity();
	}
	
	/**
	 *  Cette methode lie la roue a l'Entity entre en parametre
	 * @param vehicle : l'Entity qui sera liee a la roue, dans notre cas : le bike
	 * @param anchor :  point d'ancrage du vehicule
	 * @param axis : axe le long duquel la roue peut se deplacer
	 */
	protected void attach(Entity vehicle, Vector anchor, Vector axis) {
		WheelConstraintBuilder constraintBuilder = getOwner().getWheelConstraintBuilder();
		constraintBuilder.setFirstEntity(vehicle);
		constraintBuilder.setFirstAnchor(anchor);
		// Entity associee a la roue
		constraintBuilder.setSecondEntity(getEntity());
		// point d'ancrage de la roue (son centre)
		constraintBuilder.setSecondAnchor(Vector.ZERO);
		constraintBuilder.setAxis(axis);
		// frequence du ressort associe
		constraintBuilder.setFrequency(3.0f);
		constraintBuilder.setDamping(0.5f);
		// force angulaire maximale pouvant etre appliquee a la roue pour la faire tourner 
		constraintBuilder.setMotorMaxTorque(10.0f); 
		constraint = constraintBuilder.build();
	}
	
	/**
	 * Methode pour donner une vitesse a la roue
	 * @param speed : on donne la vitesse de la roue en parametre pour eviter qu'elle ne depasse
	 * 				  la vitesse maximale
	 */
	protected void power(float speed) {
		if (this.getSpeed() < MAX_WHEEL_SPEED) {
			constraint.setMotorEnabled(true);
			constraint.setMotorSpeed(speed);
		}
	}
	
	// Methode pour eteindre le moteur
	protected void relax() {
		constraint.setMotorEnabled(false);
	}
	
	// Methode pour supprimer l'attache entre le bike et la roue
	protected void detach() {
		constraint.destroy();
	}
	
	// Nous avons besoin de la position angulaiire pour l'animation de pedalement
	protected float getAngularPosition() {
		return getEntity().getAngularPosition();
	}
	
	/**
	 * @return relative rotation speed, in radians per second
	 */
	public float getSpeed() {
		return (getEntity().getAngularVelocity());
	}
}