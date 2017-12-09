/*
 *	Author:      Valentin Kaelin
 *	Date:        25 nov. 2017
 */
package ch.epfl.cs107.play.game.actor.bike;

import java.awt.Color;

import com.sun.glass.events.KeyEvent;

import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.actor.crate.Crate;
import ch.epfl.cs107.play.game.actor.general.Bascule;
import ch.epfl.cs107.play.game.actor.general.Pendule;
import ch.epfl.cs107.play.game.actor.general.Start;
import ch.epfl.cs107.play.game.actor.general.Terrain;
import ch.epfl.cs107.play.game.actor.general.Tremplin;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Polyline;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Image;
import ch.epfl.cs107.play.window.Window;

public class BikeGame extends ActorGame{
	
	private boolean fixed = false;
	
	private Terrain terrain;
//	private Polyline formeTerrain = new Polyline (
//			-1000.0f, -1000.0f,
//			-1000.0f, 0.0f,
//			0.0f, 0.0f,
//			3.0f, 1.0f,
//			8.0f, 1.0f,
//			15.0f, 3.0f,
//			16.0f, 3.0f,
//			25.0f, 0.0f,
//			35.0f, -5.0f,
//			50.0f, -5.0f,
//			55.0f, -4.0f,
//			65.0f, 0.0f,
//			6500.0f, -1000.0f);
	private Polyline formeTerrain = new Polyline (
			-10.0f, -1000.0f,
			-10.0f, 20.0f,
			0.0f, 20.0f,
			0.0f, 0.0f,
			10.0f, 0.0f,
			15.0f, -7.0f,
			25.0f, -7.0f,
			35.0f, 0.0f,
			45.0f, 0.0f,
			50.0f, -20.0f,
			55.0f, 0.0f,
			65.0f, 0.0f,
			70.0f, -20.0f,
			75.0f, 0.0f,
			85.0f, 0.0f,
			100.0f, -20.0f,
			120.0f, -20.0f,
			125.0f, -18.0f,
			135.0f, -45.0f,
			145.0f, -20.0f,
			163.0f, -20.0f,
			163.0f, -21.0f,
			165.0f, -21.0f,
			170.0f, -35.0f,
			175.0f, -20.0f,
			190.0f, -20.0f,
			190.0f, 0.0f,
			200.0f, 0.0f,
			200.0f, -1000.0f);
	
	private Crate c1;
	private Crate c2;
	private Crate c3;
	private Crate c4;
	private Crate c5;
	private Crate c6;
	private Crate c7;
	private Crate c8;
	private Crate c9;
	private Vector posCrate1 = new Vector(9.0f, 5.0f);
	private Vector posCrate2 = new Vector(9.0f, 7.0f);
	private Vector posCrate3 = new Vector(9.0f, 6.0f);
	private Vector posCrate4 = new Vector(104.0f, -15.0f);
	private Vector posCrate5 = new Vector(106.0f, -15.0f);
	private Vector posCrate6 = new Vector(108.0f, -15.0f);
	private Vector posCrate7 = new Vector(110.0f, -15.0f);
	private Vector posCrate8 = new Vector(112.0f, -15.0f);
	private Vector posCrate9 = new Vector(114.0f, -15.0f);
	
	private Crate plateforme;
	private Crate plateforme2;
	private Vector posPlateforme= new Vector(0.0f, 3.0f);
	private Vector posPlateforme2= new Vector(133f, -18.0f);
	
	private Polygon polyBike = new Polygon (
			0.0f, 0.5f,
			0.5f, 1.0f,
			0.0f, 2.0f,
			-0.5f, 1.0f);
	private Bike bike;
	private final float RAYON_ROUES = 0.5f;
	private Vector posBike = new Vector(2.0f, 5.0f);
	//private Vector posBike = new Vector(155.0f, -20.0f);
	
	private Finish flag;
	private float rayonFlag = 0.5f;
	private Vector posFlag = new Vector(187.0f, -19.5f);
	
	private Pendule pendule1;
	private Vector posPendule1 = new Vector(20.0f, -2.5f);
	
	private Bascule bascule1;
	private Vector posBascule1 = new Vector(46.0f, -0.25f);
	private Bascule bascule2;
	private Vector posBascule2 = new Vector(50.5f, -0.25f);
	private Bascule bascule3;
	private Vector posBascule3 = new Vector(67.0f, -0.35f);
	
	private Tremplin tremplin1;
	private Vector posTremplin1 = new Vector(163.0f, -21f);
	
	private TextGraphics msgCrash;
	
	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);
		//start = new Start(this, true, Vector.ZERO);
		
		// Nous dessinons un texte vide pour eviter que nos macs crash environ
		// une seconde a l'affichage du 1er texte
		// de la partie. Sur nos PC, nous n'avons pas rencontre ce probleme
		msgCrash = new TextGraphics("", 0.1f, Color.BLACK);
		msgCrash.draw(window);

		terrain = new Terrain(this, true, Vector.ZERO, formeTerrain, new Color(139, 69, 19), new Color(0,128,0), 0.15f);
		
		c1 = new Crate(this, fixed, posCrate1, 1, 1, "stone.broken.4.png");
		c2 = new Crate(this, fixed, posCrate2, 1, 1, "stone.broken.1.png");
		c3 = new Crate(this, fixed, posCrate3, 1, 1, "stone.broken.2.png");
		c4 = new Crate(this, fixed, posCrate4, 1, 1, "blocker.dead.png");
		c5 = new Crate(this, fixed, posCrate5, 1, 1, "blocker.happy.png");
		c6 = new Crate(this, fixed, posCrate6, 1, 1, "blocker.dead.png");
		c7 = new Crate(this, fixed, posCrate7, 1, 1, "blocker.happy.png");
		c8 = new Crate(this, fixed, posCrate8, 1, 1, "blocker.dead.png");
		c9 = new Crate(this, fixed, posCrate9, 1, 1, "blocker.happy.png");
		
		pendule1 = new Pendule(this, true, posPendule1, 4.0f, 0.5f);
		
		bascule1 = new Bascule(this, false, posBascule1, 3.5f, 0.5f, "wood.4.png", terrain);
		bascule2 = new Bascule(this, false, posBascule2, 3.5f, 0.5f, "wood.4.png", terrain);
		bascule3 = new Bascule(this, false, posBascule3, 6.5f, 0.7f, "wood.4.png", terrain);
		
		plateforme = new Crate(this, true, posPlateforme, 5, 0.7f, "wood.3.png");
		plateforme2 = new Crate(this, true, posPlateforme2, 5, 1, "wood.3.png");
		
		bike = new Bike(this, fixed, posBike, polyBike, RAYON_ROUES);
		
		tremplin1 = new Tremplin(this, true, posTremplin1, 2.0f, 1.0f, "jumper.normal.png");
		
		setViewCandidate(bike);
		
		flag = new Finish(this, true, posFlag, rayonFlag, "flag.red.png");
		
		return true;
	}
	
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (super.getKeyboard().get(KeyEvent.VK_R).isReleased()) {
			super.destroyAllActor();
			begin((Window)getCanvas(), getFileSystem());
			}
		if (bike.isHit()) {
			bike.afficheText();
			}
		if (flag.getWin()) {
			flag.afficheText();
			bike.celebration();
			bike.deleteListener();
			}
		}
}
