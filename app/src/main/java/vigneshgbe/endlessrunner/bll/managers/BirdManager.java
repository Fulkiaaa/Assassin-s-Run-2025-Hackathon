package vigneshgbe.endlessrunner.bll.managers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import vigneshgbe.endlessrunner.R;
import vigneshgbe.endlessrunner.be.Bird;
import vigneshgbe.endlessrunner.be.Constants;
import vigneshgbe.endlessrunner.be.Floor;

public class BirdManager {

    private List<Bird> mBirds;
    private Bitmap mBirdImage1;
    private Bitmap mBirdImage2;
    private Random random;
    private Floor mFloor;

    public BirdManager(Floor floor) {
        mBirds = new ArrayList<>();
        mBirdImage1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.bird);
        mBirdImage2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.bird2);
        random = new Random();
        mFloor = floor;
    }

    public void update() {
        Iterator<Bird> it = mBirds.iterator();
        while (it.hasNext()) {
            Bird bird = it.next();
            bird.update();
            if (bird.isOffScreen()) {
                it.remove();
            }
        }

        // Spawn aléatoire
        if (random.nextInt(100) < 2) { // 2% de chance
            int floorTop = mFloor.getRect().top;
            int maxY = floorTop - 80; // Laisser un espace de vol
            int minY = 150; // Limite haute pour éviter les bords
            int y = minY + random.nextInt(Math.max(10, maxY - minY));

            Rect birdRect = new Rect(Constants.SCREEN_WIDTH, y, Constants.SCREEN_WIDTH + 100, y + 60);
            mBirds.add(new Bird(birdRect, mBirdImage1, mBirdImage2, 35));
        }
    }

    public void draw(Canvas canvas) {
        for (Bird bird : mBirds) {
            bird.draw(canvas);
        }
    }

    public boolean checkCollision(Rect playerRect) {
        for (Bird bird : mBirds) {
            if (bird.collidesWith(playerRect)) {
                return true;
            }
        }
        return false;
    }
}
