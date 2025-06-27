package jeu.endlessrunner.be;

import android.content.Context;

// Classe utilitaire pour stocker les constantes globales du jeu
public class Constants {

    // Largeur de l'écran (à initialiser au lancement)
    public static int SCREEN_WIDTH;
    // Hauteur de l'écran (à initialiser au lancement)
    public static int SCREEN_HEIGHT;

    // Décalage de départ depuis le haut de l'écran pour certains éléments
    public static int START_FROM_TOP = 50;
    // Hauteur du sol dans le jeu
    public static final int FLOOR_HEIGHT = 300; // ou la hauteur exacte de ton sol

    // Identifiants pour les différentes scènes du jeu
    public static int MENU_SCENE = 0;
    public static int GAME_SCENE = 1;

    // Vitesse de déplacement globale dans le jeu
    public static float SPEED = 15f;

    // Contexte courant de l'application (à initialiser dans l'activité principale)
    public static Context CURRENT_CONTEXT;

}
