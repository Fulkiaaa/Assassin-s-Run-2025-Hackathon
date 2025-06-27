package jeu.endlessrunner;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import jeu.endlessrunner.be.Constants;
import jeu.endlessrunner.gui.GamePanel;

// Activité principale du jeu, point d'entrée de l'application
public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Met l'application en plein écran (cache la barre de statut)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Supprime la barre de titre
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Récupère la taille de l'écran en pixels
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;

        // Définit la vue principale du jeu
        setContentView(new GamePanel(this));
    }
}
