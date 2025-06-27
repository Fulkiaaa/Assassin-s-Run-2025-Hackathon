package jeu.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

// Classe représentant un oiseau ennemi dans le jeu
public class Bird {
    // Rectangle de collision et de dessin de l'oiseau
    private Rect mRect;
    // Deux images pour l'animation de battement d'ailes
    private Bitmap mImage1;
    private Bitmap mImage2;
    // Vitesse de déplacement horizontal
    private int mSpeed;

    // Position verticale initiale (pour le mouvement sinusoïdal)
    private int mInitialY;
    // Angle utilisé pour calculer la position sinusoïdale
    private float mWaveAngle = 0;
    // Vitesse de l'onde (fréquence du mouvement)
    private float mWaveSpeed = 0.1f;
    // Amplitude du mouvement sinusoïdal
    private float mWaveAmplitude = 60f;

    // Pour gérer l'animation (changement d'image)
    private long mLastFrameTime = 0;
    private boolean mUseImage1 = true;
    private static final long FRAME_INTERVAL = 150; // Intervalle entre les frames en ms

    // Constructeur
    public Bird(Rect rect, Bitmap image1, Bitmap image2, int speed) {
        mRect = rect;
        mImage1 = image1;
        mImage2 = image2;
        mSpeed = speed;
        mInitialY = rect.top;
    }

    // Met à jour la position et l'animation de l'oiseau
    public void update() {
        // Déplacement horizontal vers la gauche
        mRect.left -= mSpeed;
        mRect.right -= mSpeed;

        // Mouvement vertical sinusoïdal
        mWaveAngle += mWaveSpeed;
        int offsetY = (int) (Math.sin(mWaveAngle) * mWaveAmplitude);
        int height = mRect.height();

        mRect.top = mInitialY + offsetY;
        mRect.bottom = mRect.top + height;

        // Animation : alterne entre les deux images à intervalle régulier
        long now = System.currentTimeMillis();
        if (now - mLastFrameTime >= FRAME_INTERVAL) {
            mUseImage1 = !mUseImage1;
            mLastFrameTime = now;
        }
    }

    // Dessine l'oiseau sur le canvas
    public void draw(Canvas canvas) {
        Bitmap current = mUseImage1 ? mImage1 : mImage2;
        canvas.drawBitmap(current, null, mRect, new Paint());
    }

    // Détecte la collision avec le joueur
    public boolean collidesWith(Rect playerRect) {
        return Rect.intersects(mRect, playerRect);
    }

    // Retourne le rectangle de l'oiseau
    public Rect getRect() {
        return mRect;
    }

    // Indique si l'oiseau est sorti de l'écran (à gauche)
    public boolean isOffScreen() {
        return mRect.right < 0;
    }
}
