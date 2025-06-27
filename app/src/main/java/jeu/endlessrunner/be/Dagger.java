package jeu.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

// Classe représentant une dague ennemie dans le jeu
public class Dagger {
    // Rectangle de collision et de dessin de la dague
    private Rect mRect;
    // Image de la dague
    private Bitmap mImage;
    // Vitesse de déplacement horizontal
    private int mSpeed;
    // Angle de rotation actuel de la dague
    private float mRotationAngle = 0f;

    // Constructeur
    public Dagger(Rect rect, Bitmap image, int speed) {
        mRect = rect;
        mImage = image;
        mSpeed = speed;
    }

    // Met à jour la position et la rotation de la dague
    public void update() {
        // Déplacement horizontal vers la gauche
        mRect.left -= mSpeed;
        mRect.right -= mSpeed;

        // Incrémente l'angle de rotation pour l'effet de rotation
        mRotationAngle += 15f;
        if (mRotationAngle >= 360f)
            mRotationAngle -= 360f;
    }

    // Dessine la dague sur le canvas avec rotation et redimensionnement
    public void draw(Canvas canvas) {
        int centerX = mRect.centerX();
        int centerY = mRect.centerY();

        // Calcul des facteurs d'échelle pour adapter l'image à la taille du rect
        float scaleX = (float) mRect.width() / mImage.getWidth();
        float scaleY = (float) mRect.height() / mImage.getHeight();

        Matrix matrix = new Matrix();
        // Place l'origine de l'image au centre
        matrix.postTranslate(-mImage.getWidth() / 2f, -mImage.getHeight() / 2f);
        // Applique la rotation
        matrix.postRotate(mRotationAngle);
        // Applique le redimensionnement
        matrix.postScale(scaleX, scaleY);
        // Replace l'image au bon endroit sur le canvas
        matrix.postTranslate(centerX, centerY);

        canvas.drawBitmap(mImage, matrix, new Paint());
    }

    // Détecte la collision avec une autre entité (par son rectangle)
    public boolean collidesWith(Rect target) {
        return Rect.intersects(mRect, target);
    }

    // Indique si la dague est sortie de l'écran (à gauche)
    public boolean isOffScreen() {
        return mRect.right < 0;
    }

    // Retourne le rectangle de la dague
    public Rect getRect() {
        return mRect;
    }
}
