package com.jeu.endlessrunner.bll.managers;

import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import com.jeu.endlessrunner.be.Constants;
import com.jeu.endlessrunner.be.IGameObject;
import com.jeu.endlessrunner.be.Bird;

public class BirdManager implements IGameObject {

    private List<Bird> mBirds;
    private int mBirdGap;
    private int mBirdHeight;
    private int mBirdWidth;
    private int mColor;
    private boolean mIsActive;

    public BirdManager(int birdGap, int birdHeight, int birdWidth, int color) {
        mBirdGap = birdGap;
        mBirdHeight = birdHeight;
        mBirdWidth = birdWidth;
        mColor = color;
        mIsActive = false;

        mBirds = new ArrayList<>();
    }

    public void setActive(int score) {
        if (score >= 15 && !mIsActive) {
            mIsActive = true;
            populateBirds();
        }
    }

    private void populateBirds() {
        int currX = 3 * Constants.SCREEN_WIDTH;
        int maxBirds = 2;
        int count = 0;

        while (count < maxBirds) {
            // MODIFICATION : Positionnement plus bas
            // Hauteur du sol = Constants.SCREEN_HEIGHT - 300
            // Zone de vol : entre 200 pixels au-dessus du sol et mi-hauteur de l'écran
            int minY = Constants.SCREEN_HEIGHT - 500; // 200 pixels au-dessus du sol
            int maxY = Constants.SCREEN_HEIGHT / 2; // Mi-hauteur de l'écran
            int randomY = (int) (Math.random() * (maxY - minY) + minY);

            mBirds.add(new Bird(mBirdHeight, mBirdWidth,
                    currX + (int) (Math.random() * mBirdGap),
                    randomY, mColor));
            currX += mBirdGap;
            count++;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (mIsActive) {
            for (Bird bird : mBirds) {
                bird.draw(canvas);
            }
        }
    }

    @Override
    public void update() {
        if (!mIsActive)
            return;

        for (Bird bird : mBirds) {
            bird.update();
            bird.move(Constants.SPEED * 0.8f);
        }

        if (!mBirds.isEmpty() && mBirds.get(mBirds.size() - 1).getRect().right <= 0) {
            mBirds.remove(mBirds.size() - 1);

            int xStart = Constants.SCREEN_WIDTH + (int) (Math.random() * mBirdGap);

            // MODIFICATION : Même logique de positionnement pour les nouveaux oiseaux
            int minY = Constants.SCREEN_HEIGHT - 500;
            int maxY = Constants.SCREEN_HEIGHT / 2;
            int randomY = (int) (Math.random() * (maxY - minY) + minY);

            mBirds.add(0, new Bird(mBirdHeight, mBirdWidth,
                    xStart, randomY, mColor));
        }
    }

    public boolean collisionWithPlayer(Rect playerRect) {
        if (!mIsActive)
            return false;

        boolean collision = false;
        for (Bird bird : mBirds) {
            if (bird.collisionWithPlayer(playerRect)) {
                collision = true;
            }
        }
        return collision;
    }

    public boolean isActive() {
        return mIsActive;
    }
}