package jeu.endlessrunner.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import jeu.endlessrunner.be.Constants;
import jeu.endlessrunner.bll.GameLoopThread;
import jeu.endlessrunner.bll.managers.SceneManager;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private GameLoopThread mGameLoopThread;
    private SceneManager mSceneManager;

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        Constants.CURRENT_CONTEXT = context;

        mGameLoopThread = new GameLoopThread(getHolder(), this);

        // Passer le context au SceneManager
        mSceneManager = new SceneManager(context);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Stocke la taille de l'écran dans les constantes
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
        boolean retry = true;
        while (retry) {
            try {
                mGameLoopThread.setRunning(false);
                mGameLoopThread.join();
                // Nettoyer les ressources
                if (mSceneManager != null) {
                    mSceneManager.terminate();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update() {
        mSceneManager.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mSceneManager.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mSceneManager.receiveTouch(event);
        return true;
    }
}