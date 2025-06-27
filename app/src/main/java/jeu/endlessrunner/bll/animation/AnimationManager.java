package jeu.endlessrunner.bll.animation;

import android.graphics.Canvas;
import android.graphics.Rect;

// Classe pour gérer plusieurs animations et basculer entre elles
public class AnimationManager {

    private Animation[] mAnimations; // Tableau des différentes animations possibles
    private int mAnimationIndex; // Index de l'animation actuellement jouée

    // Constructeur
    public AnimationManager(Animation[] animations) {
        mAnimations = animations;
        mAnimationIndex = 0;
    }

    // Joue une animation différente si l'index change
    public void playAnimation(int index) {
        if (mAnimationIndex != index) {
            mAnimations[mAnimationIndex].stop(); // Arrête l'animation courante
            mAnimationIndex = index; // Change d'animation
            mAnimations[mAnimationIndex].play(); // Démarre la nouvelle animation
        }
    }

    // Dessine l'animation courante sur le canvas
    public void draw(Canvas canvas, Rect rect) {
        if (mAnimations[mAnimationIndex].isPlaying()) {
            mAnimations[mAnimationIndex].draw(canvas, rect);
        }
    }

    // Met à jour l'animation courante (changement de frame, etc.)
    public void update() {
        if (mAnimations[mAnimationIndex].isPlaying()) {
            mAnimations[mAnimationIndex].update();
        }
    }
}