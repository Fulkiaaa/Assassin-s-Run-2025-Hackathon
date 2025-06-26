package jeu.endlessrunner.bll.managers;

import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import jeu.endlessrunner.be.Constants;
import jeu.endlessrunner.be.Floor;
import jeu.endlessrunner.be.IGameObject;
import jeu.endlessrunner.be.Obstacle;

public class ObstacleManager implements IGameObject {

    private List<Obstacle> mObstacles;
    private int mObstacleGap;
    private int mObstacleHeight;
    private int mObstacleWidth;
    private int mColor;
    private Floor mFloor;

    public ObstacleManager(int obstacleGap, int obstacleHeight, int obstacleWidth, int color, Floor floor) {
        mObstacleGap = obstacleGap;
        mObstacleHeight = obstacleHeight;
        mObstacleWidth = obstacleWidth;
        mColor = color;
        mFloor = floor;

        mObstacles = new ArrayList<>();
        populateObstacles();
    }

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

    public List<Rect> getObstacleRects() {
        List<Rect> list = new ArrayList<>();
        for (Obstacle o : mObstacles) {
            list.add(o.getRect());
        }
        return list;
    }

    @Override
    public void draw(Canvas canvas) {
        for (Obstacle obj : mObstacles) {
            obj.draw(canvas);
        }
    }

    @Override
    public void update() {
        for (Obstacle obj : mObstacles) {
            obj.move(Constants.SPEED);
        }

        if (mObstacles.get(mObstacles.size() - 1).getRect().right <= 0) {
            mObstacles.remove(mObstacles.size() - 1);
            int xStart = (int) (Math.random() * (mObstacleGap + mObstacles.get(0).getRect().right));
            mObstacles.add(0, new Obstacle(
                    mObstacleHeight,
                    mObstacleWidth,
                    Constants.SCREEN_WIDTH + xStart,
                    mFloor.getRect().top - mObstacleHeight + 30,
                    mColor));
        }
    }

    public boolean collisionWithPlayer(Rect playerRect) {
        for (Obstacle obj : mObstacles) {
            if (obj.collisionWithPlayer(playerRect)) {
                return true;
            }
        }
        return false;
    }
}