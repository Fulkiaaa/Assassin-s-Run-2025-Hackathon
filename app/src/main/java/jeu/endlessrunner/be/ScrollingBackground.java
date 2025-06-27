package jeu.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

// Classe pour gérer un arrière-plan qui défile horizontalement
public class ScrollingBackground implements IGameObject {

    private Bitmap mImage; // Image du fond
    private Rect mRect1, mRect2; // Deux rectangles pour l'effet de défilement infini
    private int mSpeed; // Vitesse de défilement
    private int mScreenWidth; // Largeur de l'écran
    private int mScreenHeight; // Hauteur de l'écran

    public ScrollingBackground(Bitmap image, int screenWidth, int screenHeight, int speed) {
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        mSpeed = speed;

        // Adapter la hauteur du background pour qu’il s’arrête au-dessus du sol
        int backgroundHeight = screenHeight - Constants.FLOOR_HEIGHT;

        // Redimensionner le background à la taille de l'écran (largeur x hauteur utile)
        mImage = Bitmap.createScaledBitmap(image, screenWidth, backgroundHeight, true);

        // Initialiser deux rectangles côte à côte pour le défilement infini
        mRect1 = new Rect(0, 0, screenWidth, backgroundHeight);
        mRect2 = new Rect(screenWidth, 0, screenWidth * 2, backgroundHeight);
    }

    // Dessine les deux portions du background pour l'effet de défilement
    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(mImage, null, mRect1, new Paint());
        canvas.drawBitmap(mImage, null, mRect2, new Paint());
    }

    // Met à jour la position des rectangles pour faire défiler le fond
    @Override
    public void update() {
        // Déplace les deux rectangles vers la gauche
        mRect1.left -= mSpeed;
        mRect1.right -= mSpeed;

        mRect2.left -= mSpeed;
        mRect2.right -= mSpeed;

        // Si un rectangle sort complètement de l'écran à gauche, on le replace à droite
        // de l'autre
        if (mRect1.right <= 0) {
            mRect1.left = mRect2.right;
            mRect1.right = mRect1.left + mScreenWidth;
        }

        if (mRect2.right <= 0) {
            mRect2.left = mRect1.right;
            mRect2.right = mRect2.left + mScreenWidth;
        }
    }
}
