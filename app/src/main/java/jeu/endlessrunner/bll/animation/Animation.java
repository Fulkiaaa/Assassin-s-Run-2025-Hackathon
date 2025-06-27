package jeu.endlessrunner.bll.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

// Classe pour gérer une animation à partir d'une série d'images (frames)
public class Animation {

    private Bitmap[] mFrames; // Tableau des images de l'animation
    private int mFrameIndex; // Index de la frame courante
    private float mFrameTime; // Durée d'affichage d'une frame (en secondes)
    private long mLastFrame; // Temps du dernier changement de frame

    private boolean mIsPlaying; // Indique si l'animation est en cours

    // Constructeur
    public Animation(Bitmap[] frames, float animationTime) {
        mIsPlaying = false;
        mFrames = frames;
        mFrameIndex = 0;
        mFrameTime = animationTime / mFrames.length; // Durée d'une frame
        mLastFrame = System.currentTimeMillis();
    }

    // Indique si l'animation est en cours de lecture
    public boolean isPlaying() {
        return mIsPlaying;
    }

    // Démarre l'animation depuis le début
    public void play() {
        mIsPlaying = true;
        mFrameIndex = 0;
        mLastFrame = System.currentTimeMillis();
    }

    // Arrête l'animation
    public void stop() {
        mIsPlaying = false;
    }

    // Met à jour l'animation (change de frame si nécessaire)
    public void update() {
        if (!isPlaying()) {
            return;
        }
        // Si le temps écoulé dépasse la durée d'une frame, passe à la suivante
        if (System.currentTimeMillis() - mLastFrame > mFrameTime * 1000) {
            mFrameIndex++;
            mFrameIndex = mFrameIndex >= mFrames.length ? 0 : mFrameIndex;
            mLastFrame = System.currentTimeMillis();
        }
    }

    // Dessine la frame courante sur le canvas à la position donnée
    public void draw(Canvas canvas, Rect destination) {
        if (!isPlaying()) {
            return;
        }
        scaleRect(destination); // Ajuste le rectangle pour garder le ratio de l'image
        canvas.drawBitmap(mFrames[mFrameIndex], null, destination, new Paint());
    }

    // Ajuste le rectangle pour respecter le ratio largeur/hauteur de l'image
    private void scaleRect(Rect rect) {
        float whRatio = (float) (mFrames[mFrameIndex].getWidth()) / (float) (mFrames[mFrameIndex].getHeight());
        if (rect.width() > rect.height()) {
            rect.left = rect.height() - (int) ((rect.height() * whRatio));
        } else {
            rect.top = rect.bottom - (int) ((rect.width() * (1 / whRatio)));
        }
    }
}
