package jeu.endlessrunner.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import jeu.endlessrunner.be.Constants;
import jeu.endlessrunner.bll.GameLoopThread;
import jeu.endlessrunner.bll.managers.SceneManager;

// Classe principale de la vue de jeu, gère l'affichage et les interactions
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private GameLoopThread mGameLoopThread; // Thread principal de la boucle de jeu
    private SceneManager mSceneManager; // Gère les différentes scènes du jeu

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this); // Ajoute le callback pour gérer la surface

        Constants.CURRENT_CONTEXT = context; // Stocke le contexte global

        mGameLoopThread = new GameLoopThread(getHolder(), this);

        // Initialise le gestionnaire de scènes avec le contexte
        mSceneManager = new SceneManager(context);

        setFocusable(true); // Permet de recevoir les événements tactiles
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Stocke la taille de l'écran dans les constantes globales
        Constants.SCREEN_WIDTH = getWidth();
        Constants.SCREEN_HEIGHT = getHeight();

        mGameLoopThread = new GameLoopThread(holder, this);
        mGameLoopThread.setRunning(true);
        mGameLoopThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // rien à modifier ici
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Arrête proprement la boucle de jeu et libère les ressources
        boolean retry = true;
        while (retry) {
            try {
                mGameLoopThread.setRunning(false);
                mGameLoopThread.join();
                // Nettoyer les ressources des scènes
                if (mSceneManager != null) {
                    mSceneManager.terminate();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    // Met à jour la logique du jeu (appelé à chaque frame)
    public void update() {
        mSceneManager.update();
    }

    // Dessine la scène active sur le canvas (appelé à chaque frame)
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mSceneManager.draw(canvas);
    }

    // Gère les événements tactiles et les transmet à la scène active
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mSceneManager.receiveTouch(event);
        return true;
    }
}