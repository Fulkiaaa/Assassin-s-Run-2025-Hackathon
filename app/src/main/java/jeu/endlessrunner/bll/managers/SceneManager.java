package jeu.endlessrunner.bll.managers;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import jeu.endlessrunner.be.Constants;
import jeu.endlessrunner.bll.scenes.GamePlayScene;
import jeu.endlessrunner.bll.IScene;
import jeu.endlessrunner.bll.scenes.Menu;

// Classe qui gère les différentes scènes du jeu (menu, gameplay, etc.)
public class SceneManager {

    public static int ACTIVE_SCENE; // Index de la scène actuellement active

    private List<IScene> mScenes = new ArrayList<>(); // Liste des scènes du jeu

    // Constructeur : initialise les scènes et démarre sur le menu
    public SceneManager(Context context) {
        ACTIVE_SCENE = Constants.MENU_SCENE;
        mScenes.add(new Menu(context)); // Ajoute la scène du menu
        mScenes.add(new GamePlayScene(context)); // Ajoute la scène de jeu principal
    }

    // Met à jour la scène active
    public void update() {
        mScenes.get(ACTIVE_SCENE).update();
    }

    // Dessine la scène active
    public void draw(Canvas canvas) {
        mScenes.get(ACTIVE_SCENE).draw(canvas);
    }

    // Transmet les événements tactiles à la scène active
    public void receiveTouch(MotionEvent event) {
        mScenes.get(ACTIVE_SCENE).recieveTouch(event);
    }

    // Termine toutes les scènes (libération des ressources, etc.)
    public void terminate() {
        for (IScene scene : mScenes) {
            scene.terminate();
        }
    }
}