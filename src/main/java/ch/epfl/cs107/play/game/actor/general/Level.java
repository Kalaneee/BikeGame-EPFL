/*
 *	Author:      Valentin Kaelin
 *	Date:        8 déc. 2017
 */
package ch.epfl.cs107.play.game.actor.general;

import java.util.List;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.math.Node;

public abstract class Level extends Node implements Actor {
	protected List<Level> createLevelList() {
		return Arrays.asList(
				new BasicBikeGameLevel(..)
				
				new CrazyEpicLevel(..)
				);
		}

	public abstract void createAllActors();

}
