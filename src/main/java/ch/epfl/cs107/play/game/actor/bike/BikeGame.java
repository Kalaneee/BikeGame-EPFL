/*
 *	Authors:      Valentin Kaelin - Giulia Murgia
 *	Date:        25 nov. 2017
 */
package ch.epfl.cs107.play.game.actor.bike;

import java.awt.Color;

import com.sun.glass.events.KeyEvent;

import ch.epfl.cs107.play.game.actor.ActorGame;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.actor.general.Crate;
import ch.epfl.cs107.play.game.actor.general.Accelerateur;
import ch.epfl.cs107.play.game.actor.general.Ascenseur;
import ch.epfl.cs107.play.game.actor.general.Bascule;
import ch.epfl.cs107.play.game.actor.general.Pendule;
import ch.epfl.cs107.play.game.actor.general.Terrain;
import ch.epfl.cs107.play.game.actor.general.Tremplin;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Polyline;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Window;

public class BikeGame extends ActorGame {
	// Nous gardons en attribut le fileSystem pour pouvoir l'utiliser lors du reset
	private FileSystem fileSystem;
	
	private boolean fixed = false;
	
	private Terrain terrain;
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
			245.0f, -20.0f,
			245.0f, -22.0f,
			255.0f, -22.0f,
			258.0f, -20.0f,
			261.0f, -20.0f,
			261.0f, -25.0f,
			268.0f, -25.0f,
			268.0f, -20.0f,
			300.0f, -20.0f,
			305.0f, -17.0f,
			305.0f, -30.0f,
			340.0f, -30.0f,
			340.0f, -17.0f,
			345.0f, -20.0f,
			390.0f, -20.0f,
			390.0f, 0.0f,
			400.0f, 0.0f,
			420.0f, 0.0f,
			420.0f, 20.0f,
			440.0f, 20.0f,
			440.0f, -1000.0f);
	// Nous mettons une friction nulle au terrain car le PartBuilder
	// ne l'utilisera pas. Il applique une friction au terrain
	// seulement si le terrain est glissant
	private float frictionTerrain = 0.0f;
	private Terrain terrainGlissant;
	private Polyline formeTerrainGlissant = new Polyline (
			-0.2f, -1.0f,
			-0.2f, 2.05f,
			13.0f, 2.05f,
			13.0f, -1.0f);
	private float frictionGlissant = 0.001f;
	
	// Nous creeons un tableau de vector contenant les positions
	// des differents buissons sur le terrain
	private Vector[] bushPos = {
			new Vector(-5.0f, 0.0f),
			new Vector(-15.0f, 7.0f),
			new Vector(-22.0f, 7.0f),
			new Vector(-38.0f, 0.0f),
			new Vector(-43.0f, 0.0f),
			new Vector(-63.0f, 0.0f),
			new Vector(-77.0f, 0.0f),
			new Vector(-118.0f, 20.0f),
			new Vector(-150.0f, 20.0f),
			new Vector(-152.0f, 20.0f),
			new Vector(-159.0f, 20.0f),
			new Vector(-177.0f, 20.0f),
			new Vector(-185.0f, 20.0f),
			new Vector(-197.0f, 20.0f),
			new Vector(-204.0f, 20.0f),
			new Vector(-212.0f, 20.0f),
			new Vector(-221.0f, 20.0f),
			new Vector(-242.0f, 20.0f),
			new Vector(-263.0f, 25.0f),
			new Vector(-271.0f, 20.0f),
			new Vector(-290.0f, 20.0f),
			new Vector(-306.0f, 30.0f),
			new Vector(-312.0f, 30.0f),
			new Vector(-336.0f, 30.0f),
			new Vector(-349.0f, 20.0f),
			new Vector(-355.0f, 20.0f),
			new Vector(-372.0f, 20.0f),
			new Vector(-377.0f, 20.0f),
			new Vector(-396.0f, 0.0f),
			new Vector(-410.0f, 0.0f),
	};
	
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
	
	private Crate plateforme1;
	private Crate plateforme2;
	private Vector posPlateforme1= new Vector(0.0f, 3.0f);
	private Vector posPlateforme2= new Vector(133f, -18.0f);
	
	private Crate mur1;
	private Vector posMur1 = new Vector(258.0f, -20.0f);
	
	private Polygon polyBike = new Polygon (
			0.0f, 0.5f,
			0.5f, 1.0f,
			0.0f, 2.0f,
			-0.5f, 1.0f);
	private Bike bike;
	// Nous mettons le rayon des roues en final car celui-ci ne change pas
	private final float RAYON_ROUES = 0.5f;
	private Vector posBike = new Vector(2.0f, 5.0f);
	
	private Finish flag;
	private float rayonFlag = 0.5f;
	private Vector posFlag = new Vector(417.0f, 0.5f);
	
	private Pendule pendule1;
	private Pendule pendule2;
	private Pendule pendule3;
	private Vector posPendule1 = new Vector(20.0f, -2.5f);
	private Vector posPendule2 = new Vector(205.0f, -15.5f);
	private Vector posPendule3 = new Vector(230.0f, -15.5f);
	
	private Bascule bascule1;
	private Vector posBascule1 = new Vector(46.0f, -0.25f);
	private Bascule bascule2;
	private Vector posBascule2 = new Vector(50.5f, -0.25f);
	private Bascule bascule3;
	private Vector posBascule3 = new Vector(67.0f, -0.35f);
	
	private Tremplin tremplin1;
	private Vector posTremplin1 = new Vector(163.0f, -21f);
	
	private Accelerateur accelerateur1;
	private Vector posAccelerateur1 = new Vector(295.0f, -20.0f);
	
	private Ascenseur ascenseur1;
	private Vector posAscenseur1 = new Vector(385.0f, -20.0f);
	
	private TextGraphics msgDeath;
	private int nbDeaths = 0;
	
	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);
		this.fileSystem = fileSystem;
		
		// Nous dessinons un texte vide pour eviter que nos Macs crash environ
		// une seconde a l'affichage du 1er texte
		// de la partie. Sur nos PC, nous n'avons pas rencontre ce probleme

		terrain = new Terrain(this, true, Vector.ZERO, formeTerrain, new Color(139, 69, 19), new Color(0, 128, 0),
				0.15f, bushPos, false, frictionTerrain);
		terrainGlissant = new Terrain(this, true, new Vector(245.0f, -22.0f), formeTerrainGlissant, new Color(127, 140, 141),
				new Color(66, 66, 66), 0.0f, null, true, frictionGlissant);

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
		pendule2 = new Pendule(this, true, posPendule2, 4.0f, 0.5f);
		pendule3 = new Pendule(this, true, posPendule3, -5.0f, 0.5f);
		
		bascule1 = new Bascule(this, false, posBascule1, 3.5f, 0.5f, "wood.cracked.3.png", terrain);
		bascule2 = new Bascule(this, false, posBascule2, 3.5f, 0.5f, "wood.cracked.3.png", terrain);
		bascule3 = new Bascule(this, false, posBascule3, 6.5f, 0.7f, "wood.cracked.3.png", terrain);
		
		plateforme1 = new Crate(this, true, posPlateforme1, 5, 0.7f, "metal.3.png");
		plateforme2 = new Crate(this, true, posPlateforme2, 5, 1, "metal.3.png");
		
		mur1 = new Crate(this, fixed, posMur1, 1.3f, 9.0f, "wood.cracked.8.png");
		
		bike = new Bike(this, fixed, posBike, polyBike, RAYON_ROUES);
		
		tremplin1 = new Tremplin(this, true, posTremplin1, 2.0f, 1.0f, "jumper.normal.png");
		
		accelerateur1 = new Accelerateur(this, true, posAccelerateur1, 3, 1, "Accelerateur.png");
		
		ascenseur1 = new Ascenseur(this, true, posAscenseur1, 5.0f, 0.5f, "glass.cracked.3.png", 23.0f, 500.0f, 0.02f);
		
		// Nous fixons la camera sur le bike
		setViewCandidate(bike);
		
		flag = new Finish(this, true, posFlag, rayonFlag, "flag.red.png");
		
		msgDeath = new TextGraphics("", 0.05f, Color.BLACK, Color.BLACK, 0.08f, true, false, new Vector(-1.2f, -8.0f), 1.0f,
				100.0f);
		msgDeath.setText("DEATHS-" + nbDeaths);
		msgDeath.setParent(window);
		msgDeath.draw(window);
		
		return true;
	}
	
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		msgDeath.draw(getCanvas());
		// Si nous appuyons sur R, nous remettons le jeu a 0 donc nous supprimons
		// tous les acteurs et nous relancons la methode begin
		if (super.getKeyboard().get(KeyEvent.VK_R).isReleased()) {
			super.destroyAllActor();
			nbDeaths++;
			begin((Window)getCanvas(), fileSystem);
			}
		// Nous affichons les messages en cas de chute
		if (bike.isHit()) {
			bike.afficheText();
			}
		// Nous lancons l'animation et rendons invincible le bike en cas de victoire
		if (flag.getWin()) {
			// Nous le mettons ici a -1 pour qu'il soit a 0 lors du reset
			nbDeaths = -1;
			bike.celebration();
			bike.deleteListener();
			}
		}
}
