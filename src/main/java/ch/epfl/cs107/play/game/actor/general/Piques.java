/*
 *	Author:      Valentin Kaelin
 *	Date:        11 déc. 2017
 */
package ch.epfl.cs107.play.game.actor.general;

import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.bike.Bike;
import ch.epfl.cs107.play.game.actor.crate.Crate;
import ch.epfl.cs107.play.math.Contact;
import ch.epfl.cs107.play.math.ContactListener;
import ch.epfl.cs107.play.math.Part;
import ch.epfl.cs107.play.math.Vector;

public class Piques extends Crate{
	private ContactListener listener;

	public Piques(ActorGame game, boolean fixed, Vector position, float width, float height, String image, Bike bike) {
		super(game, fixed, position, width, height, image);
		
		listener = new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Part other = contact.getOther();
				if (other != null) {
					System.out.println("je rentre");
					contact.getOther().destroy();
				}
				
			}

			@Override
			public void endContact(Contact contact) {
			}
		};
		getEntity().addContactListener(listener);
	}
}
