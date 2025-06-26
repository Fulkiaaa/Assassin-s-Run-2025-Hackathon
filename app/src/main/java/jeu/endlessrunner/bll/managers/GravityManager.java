package jeu.endlessrunner.bll.managers;

import jeu.endlessrunner.be.Floor;
import jeu.endlessrunner.be.Player;



public class GravityManager {

    /**
     * Checks if the bottom of the player is touching the top of the floor.
     * @param player
     * @param floor
     * @return
     */
    public boolean isPlayerNotTouchingFloor(Player player, Floor floor){
        int playerBottom = player.getRect().bottom;
        int floorTop = floor.getRect().top;

        return playerBottom < floorTop ; // marge de tolérance
    }

}
