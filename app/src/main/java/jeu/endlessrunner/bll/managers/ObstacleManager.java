package jeu.endlessrunner.bll.managers;

import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import jeu.endlessrunner.be.Constants;
import jeu.endlessrunner.be.Floor;
import jeu.endlessrunner.be.IGameObject;
import jeu.endlessrunner.be.Obstacle;

// Classe qui gère la génération, le déplacement et la collision des obstacles
public class ObstacleManager implements IGameObject {

    private List<Obstacle> mObstacles; // Liste des obstacles actifs
    private int mObstacleGap; // Espace horizontal entre deux obstacles
    private int mObstacleHeight; // Hauteur d'un obstacle
    private int mObstacleWidth; // Largeur d'un obstacle
    private int mColor; // Couleur (ou identifiant) de l'obstacle
    private Floor mFloor; // Référence au sol pour placer les obstacles

    // Constructeur
    public ObstacleManager(int obstacleGap, int obstacleHeight, int obstacleWidth, int color, Floor floor) {
        mObstacleGap = obstacleGap;
        mObstacleHeight = obstacleHeight;
        mObstacleWidth = obstacleWidth;
        mColor = color;
        mFloor = floor;

        mObstacles = new ArrayList<>();
        populateObstacles(); // Génère les obstacles de départ
    }

    // Génère les obstacles initiaux hors de l'écran à droite
    private void populateObstacles() {
        int currX = 5 * Constants.SCREEN_WIDTH / 3;
        while (currX > Constants.SCREEN_WIDTH) {
            mObstacles.add(new Obstacle(
                    mObstacleHeight,
                    mObstacleWidth,
                    (int) (Math.random() * currX + Constants.SCREEN_WIDTH),
                    mFloor.getRect().top - mObstacleHeight + 30,
                    mColor));
            currX -= mObstacleGap;
        }
    }

    // Retourne la liste des rectangles des obstacles (utile pour la collision)
    public List<Rect> getObstacleRects() {
        List<Rect> list = new ArrayList<>();
        for (Obstacle o : mObstacles) {
            list.add(o.getRect());
        }
        return list;
    }

    // Dessine tous les obstacles sur le canvas
    @Override
    public void draw(Canvas canvas) {
        for (Obstacle obj : mObstacles) {
            obj.draw(canvas);
        }
    }

    // Met à jour la position des obstacles et recycle ceux qui sortent de l'écran
    @Override
    public void update() {
        for (Obstacle obj : mObstacles) {
            obj.move(Constants.SPEED); // Déplace chaque obstacle vers la gauche
        }

        // Si le dernier obstacle est complètement sorti de l'écran à gauche
        if (mObstacles.get(mObstacles.size() - 1).getRect().right <= 0) {
            mObstacles.remove(mObstacles.size() - 1); // Supprime-le
            int xStart = (int) (Math.random() * (mObstacleGap + mObstacles.get(0).getRect().right));
            // Ajoute un nouvel obstacle à droite
            mObstacles.add(0, new Obstacle(
                    mObstacleHeight,
                    mObstacleWidth,
                    Constants.SCREEN_WIDTH + xStart,
                    mFloor.getRect().top - mObstacleHeight + 30,
                    mColor));
        }
    }

    // Vérifie la collision entre le joueur et tous les obstacles
    public boolean collisionWithPlayer(Rect playerRect) {
        for (Obstacle obj : mObstacles) {
            if (obj.collisionWithPlayer(playerRect)) {
                return true;
            }
        }
        return false;
    }
}