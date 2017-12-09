/*
 *	Author:      Valentin Kaelin
 *	Date:        24 nov. 2017
 */
package ch.epfl.cs107.play.game.actor;

import java.util.List;

import ch.epfl.cs107.play.math.Entity;
import ch.epfl.cs107.play.math.EntityBuilder;
import ch.epfl.cs107.play.math.Part;
import ch.epfl.cs107.play.math.Vector;

public abstract class GameEntity implements Actor{
	private Entity body;
	private ActorGame game;
	
	public GameEntity(ActorGame game, boolean fixed, Vector position) {
		// Un game et une position sont des arguments obligatoires pour une GameEntity
		// On ne met pas de bloc catch pour que si la valeur est nulle on ait un arret du programme.
	
		if (game == null) {
			throw new NullPointerException("Valeur indispensable ! ");
		}
		
		//if (fixed = null) {
		//	throw new NullPointerException("Une position est obligatoire !");
		//}
		
		if (position == null) {
			throw new NullPointerException("Valeur indispensable !");
		}
		this.game = game;
		EntityBuilder entityBuilder = game.getEntityBuilder();
		entityBuilder.setFixed(fixed);
		entityBuilder.setPosition(position);
		body = entityBuilder.build();
	}
	
	public GameEntity(ActorGame game, boolean fixed) {
		this(game, fixed, Vector.ZERO);
	}
	
	public void destroy() {
		body.destroy();
		getOwner().removeActor(this);
	}
	
	protected Entity getEntity() {
		return this.body;
	}
	
	protected ActorGame getOwner() {
		return this.game;
	}
	
	public List<Part> getParts() {
		return body.getParts();
	}
}