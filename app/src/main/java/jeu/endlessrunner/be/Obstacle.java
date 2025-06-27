package jeu.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import jeu.endlessrunner.R;

// Classe représentant un obstacle dans le jeu
public class Obstacle implements IGameObject {

    // Rectangle de collision et de dessin de l'obstacle
    private Rect mRect;
    // Couleur de l'obstacle (non utilisée si image)
    private int mColor;
    // Image de l'obstacle
    private Bitmap mImage;

    // Constructeur
    public Obstacle(int rectHeight, int obstacleWidth, int startX, int startY, int color) {
        // Initialise le rectangle de l'obstacle à la position et taille données
        mRect = new Rect(startX, startY, startX + obstacleWidth, startY + rectHeight);
        mColor = color;

        // Charge l'image de l'obstacle depuis les ressources
        BitmapFactory bf = new BitmapFactory();
        mImage = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.singlegate);
    }

    // Dessine l'obstacle sur le canvas
    @Override
    public void draw(Canvas canvas) {
        // Dessine l'image de l'obstacle redimensionnée dans le rectangle
        canvas.drawBitmap(mImage, null, mRect, new Paint());
    }

    // Méthode de mise à jour (vide ici, à compléter si besoin)
    @Override
    public void update() {

    }

    /**
     * Déplace l'obstacle de la droite vers la gauche avec la vitesse donnée.
     * 
     * @param speed vitesse de déplacement
     */
    public void move(float speed) {
        mRect.left -= speed;
        mRect.right -= speed;
    }

    // Retourne le rectangle de l'obstacle
    public Rect getRect() {
        return mRect;
    }

    /**
     * Vérifie si l'obstacle entre en collision avec le joueur.
     * 
     * @param playerRect rectangle du joueur
     * @return true si collision, false sinon
     */
    public boolean collisionWithPlayer(Rect playerRect) {
        return mRect.intersects(playerRect.left, playerRect.top, playerRect.right, playerRect.bottom);
    }
}
