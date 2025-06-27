package jeu.endlessrunner.bll.managers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import jeu.endlessrunner.R;
import jeu.endlessrunner.be.Bird;
import jeu.endlessrunner.be.Constants;
import jeu.endlessrunner.be.Floor;

// Classe pour gérer la génération, l'affichage et la suppression des oiseaux ennemis
public class BirdManager {

    private List<Bird> mBirds; // Liste des oiseaux actifs à l'écran
    private Bitmap mBirdImage1; // Première image pour l'animation de l'oiseau
    private Bitmap mBirdImage2; // Deuxième image pour l'animation de l'oiseau
    private Random random; // Générateur aléatoire pour le spawn
    private Floor mFloor; // Référence au sol pour placer les oiseaux au bon endroit

    // Constructeur
    public BirdManager(Floor floor) {
        mBirds = new ArrayList<>();
        // Charge les deux images de l'oiseau depuis les ressources
        mBirdImage1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.bird);
        mBirdImage2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.bird2);
        random = new Random();
        mFloor = floor;
    }

    // Met à jour tous les oiseaux (déplacement, suppression si hors écran,
    // génération aléatoire)
    public void update() {
        Iterator<Bird> it = mBirds.iterator();
        while (it.hasNext()) {
            Bird bird = it.next();
            bird.update();
            if (bird.isOffScreen()) {
                it.remove(); // Supprime l'oiseau s'il sort de l'écran à gauche
            }
        }

        // Spawn aléatoire d'un nouvel oiseau (2% de chance à chaque frame)
        if (random.nextInt(100) < 2) {
            int floorTop = mFloor.getRect().top;
            int maxY = floorTop - 80; // Laisser un espace de vol au-dessus du sol
            int minY = 150; // Limite haute pour éviter les bords supérieurs
            int y = minY + random.nextInt(Math.max(10, maxY - minY));

            // Crée un nouveau rectangle pour l'oiseau à la position de spawn
            Rect birdRect = new Rect(Constants.SCREEN_WIDTH, y, Constants.SCREEN_WIDTH + 100, y + 60);
            mBirds.add(new Bird(birdRect, mBirdImage1, mBirdImage2, 35));
        }
    }

    // Dessine tous les oiseaux sur le canvas
    public void draw(Canvas canvas) {
        for (Bird bird : mBirds) {
            bird.draw(canvas);
        }
    }

    // Vérifie la collision entre le joueur et n'importe quel oiseau
    public boolean checkCollision(Rect playerRect) {
        for (Bird bird : mBirds) {
            if (bird.collidesWith(playerRect)) {
                return true;
            }
        }
        return false;
    }
}
