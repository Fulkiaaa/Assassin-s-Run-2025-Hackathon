package jeu.endlessrunner.bll;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import jeu.endlessrunner.gui.GamePanel;

// Thread principal qui gère la boucle de jeu (update/draw à chaque frame)
public class GameLoopThread extends Thread {

    public static final int MAX_FPS = 30; // Nombre d'images par seconde maximum
    public static Canvas mCanvas; // Canvas utilisé pour dessiner

    private SurfaceHolder mSurfaceHolder; // Permet d'accéder à la surface de dessin
    private GamePanel mGamePanel; // Référence au panneau de jeu

    private boolean mRunning; // Indique si la boucle tourne
    private double mAverageFPS; // FPS moyen (optionnel)

    public GameLoopThread(SurfaceHolder holder, GamePanel gamePanel) {
        super();
        mSurfaceHolder = holder;
        mGamePanel = gamePanel;
    }

    // Permet d'arrêter ou démarrer la boucle de jeu
    public void setRunning(boolean running) {
        mRunning = running;
    }

    // Boucle principale du jeu : update + draw à chaque frame
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long targetTime = 1000 / MAX_FPS; // Durée cible d'une frame en ms

        while (mRunning) {
            startTime = System.nanoTime();
            mCanvas = null;

            // Gère la synchronisation et le dessin sur le canvas
            try {
                // Verrouille le canvas pour dessiner dessus
                mCanvas = mSurfaceHolder.lockCanvas();
                synchronized (mSurfaceHolder) {
                    // Met à jour la logique du jeu et dessine la frame
                    mGamePanel.update();
                    mGamePanel.draw(mCanvas);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (mCanvas != null) {
                    try {
                        // Déverrouille le canvas et affiche la frame
                        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            // Limite le nombre de FPS en dormant si besoin
            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;
            try {
                if (waitTime > 0) {
                    this.sleep(waitTime);
                }
            } catch (InterruptedException iex) {
                iex.printStackTrace();
            }

            /*
             * // Calcul du FPS moyen (optionnel, pour debug)
             * int frameCount = 0;
             * long totalTime = 0;
             * totalTime += System.nanoTime() - startTime;
             * frameCount++;
             * if(frameCount == MAX_FPS){
             * mAverageFPS = 1000/((totalTime/frameCount)/1000000);
             * frameCount = 0;
             * totalTime = 0;
             * Log.d("AverageFPS", mAverageFPS + "");
             * }
             */
        }
    }
}
