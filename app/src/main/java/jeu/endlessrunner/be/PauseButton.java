package jeu.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import jeu.endlessrunner.R;

// Classe représentant le bouton pause/play du jeu
public class PauseButton {

    // Dimensions du bouton
    private final int WIDTH = 175;
    private final int HEIGHT = 175;
    // Position du bouton (en haut à droite de l'écran)
    private final int X_COORDINATE = Constants.SCREEN_WIDTH - (int) (WIDTH * 1.5f);
    private final int Y_COORDINATE = Constants.START_FROM_TOP;

    // Rectangle de la zone cliquable du bouton
    private Rect mRect;

    // Images pour l'état pause et play
    private Bitmap mPauseImage;
    private Bitmap mPlayImage;

    // Constructeur
    public PauseButton() {
        // Initialise la zone du bouton
        mRect = new Rect(X_COORDINATE, Y_COORDINATE, X_COORDINATE + WIDTH, Y_COORDINATE + HEIGHT);
        // Charge les images depuis les ressources
        BitmapFactory bf = new BitmapFactory();
        mPauseImage = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.pause);
        mPlayImage = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.play);
    }

    // Retourne la zone du bouton (utile pour la détection de clic)
    public Rect getRect() {
        return mRect;
    }

    // Dessine le bouton sur le canvas selon l'état (pause ou play)
    public void draw(Canvas canvas, boolean isPaused) {
        if (isPaused) {
            canvas.drawBitmap(mPlayImage, null, mRect, new Paint());
        } else {
            canvas.drawBitmap(mPauseImage, null, mRect, new Paint());
        }
    }
}
