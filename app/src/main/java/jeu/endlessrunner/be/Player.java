package jeu.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import jeu.endlessrunner.R;
import jeu.endlessrunner.bll.animation.Animation;
import jeu.endlessrunner.bll.animation.AnimationManager;

// Classe représentant le joueur dans le jeu
public class Player implements IGameObject {

    // Rectangle de collision et de dessin du joueur
    private Rect mRect;
    // Couleur du joueur (non utilisée ici, mais peut servir pour debug)
    private int mColor;

    // Animations du joueur
    private Animation mRunAnimation;
    private Animation mJumpAnimation;
    private Animation mDoubleJumpAnimation;

    // Gestionnaire d'animations
    private AnimationManager mAnimationManager;

    // Constructeur
    public Player(Rect rect, int color) {
        mRect = rect;
        mColor = color;

        // Crée les animations à partir des images ressources
        createStickAnimations();

        // Initialise le gestionnaire d'animations avec les différentes animations
        mAnimationManager = new AnimationManager(new Animation[] {
                mRunAnimation,
                mJumpAnimation,
                mDoubleJumpAnimation
        });
    }

    // Dessine le joueur sur le canvas en utilisant l'animation courante
    @Override
    public void draw(Canvas canvas) {
        mAnimationManager.draw(canvas, mRect);
    }

    // Met à jour l'animation (appelé à chaque frame)
    @Override
    public void update() {
        mAnimationManager.update();
    }

    // Met à jour la position et l'état d'animation du joueur
    public void update(Point point, boolean isJumping, boolean isDoubleJumping) {
        // Centre le rectangle du joueur sur le point donné
        mRect.set(point.x - mRect.width() / 2,
                point.y - mRect.height() / 2,
                point.x + mRect.width() / 2,
                point.y + mRect.height() / 2);

        // Sélectionne l'animation selon l'état du joueur
        if (isDoubleJumping) {
            mAnimationManager.playAnimation(2); // Double saut
        } else if (isJumping) {
            mAnimationManager.playAnimation(1); // Saut simple
        } else {
            mAnimationManager.playAnimation(0); // Course
        }

        mAnimationManager.update();
    }

    // Retourne le rectangle du joueur (pour la collision)
    public Rect getRect() {
        return mRect;
    }

    // Crée les animations du stickman à partir des ressources
    private void createStickAnimations() {
        BitmapFactory bf = new BitmapFactory();
        Bitmap run1 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.assassin_run_1);
        Bitmap run2 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.assassin_running_back);
        Bitmap jump = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.assassin_jumping);
        Bitmap doubleJump = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),
                R.drawable.assassin_double_jumping);

        mRunAnimation = new Animation(new Bitmap[] { run1, run2 }, 0.3f); // Animation de course
        mJumpAnimation = new Animation(new Bitmap[] { jump }, 2f); // Animation de saut
        mDoubleJumpAnimation = new Animation(new Bitmap[] { doubleJump }, 2f); // Animation de double saut
    }
}
