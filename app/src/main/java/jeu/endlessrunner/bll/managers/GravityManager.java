package jeu.endlessrunner.bll.managers;

import jeu.endlessrunner.be.Floor;
import jeu.endlessrunner.be.Player;

// Classe pour gérer la gravité et la détection du contact joueur/sol
public class GravityManager {

    /**
     * Vérifie si le bas du joueur ne touche pas le haut du sol.
     * 
     * @param player Le joueur
     * @param floor  Le sol
     * @return true si le joueur n'est pas en contact avec le sol, false sinon
     */
    public boolean isPlayerNotTouchingFloor(Player player, Floor floor) {
        int playerBottom = player.getRect().bottom; // Position Y du bas du joueur
        int floorTop = floor.getRect().top; // Position Y du haut du sol

        // Retourne vrai si le bas du joueur est au-dessus du haut du sol
        return playerBottom < floorTop; // marge de tolérance
    }

}
