package com.jeu.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;

import com.jeu.endlessrunner.R;

public class Bird implements IGameObject {

    private Rect mRect;
    private int mColor;
    private Bitmap mImage1;
    private Bitmap mImage2;
    private Bitmap mCurrentImage;
    private float mVerticalSpeed;
    private long mLastSwitchTime;
    private static final long SWITCH_INTERVAL = 150;

    public Bird(int birdHeight, int birdWidth, int startX, int startY, int color) {
        mRect = new Rect(startX, startY, startX + birdWidth, startY + birdHeight);
        mColor = color;
        mVerticalSpeed = (float) (Math.random() * 2 - 1);

        BitmapFactory bf = new BitmapFactory();
        mImage1 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.bird);
        mImage2 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.bird_flying);
        mCurrentImage = mImage1;
        mLastSwitchTime = SystemClock.uptimeMillis();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(mCurrentImage, null, mRect, new Paint());
    }

    @Override
    public void update() {
        long now = SystemClock.uptimeMillis();
        if (now - mLastSwitchTime > SWITCH_INTERVAL) {
            mCurrentImage = (mCurrentImage == mImage1) ? mImage2 : mImage1;
            mLastSwitchTime = now;
        }

        mRect.top += mVerticalSpeed;
        mRect.bottom += mVerticalSpeed;

        // MODIFICATION : Nouvelles limites de vol plus basses
        int minFlightHeight = Constants.SCREEN_HEIGHT - 600; // Plus haut que le sol
        int maxFlightHeight = Constants.SCREEN_HEIGHT / 2; // Mi-hauteur de l'écran

        if (mRect.top <= minFlightHeight) {
            mVerticalSpeed = Math.abs(mVerticalSpeed); // Force vers le bas
        } else if (mRect.bottom >= maxFlightHeight) {
            mVerticalSpeed = -Math.abs(mVerticalSpeed); // Force vers le haut
        }
    }

    public void move(float speed) {
        mRect.left -= speed;
        mRect.right -= speed;
    }

    public Rect getRect() {
        return mRect;
    }

    public boolean collisionWithPlayer(Rect playerRect) {
        return mRect.intersects(playerRect.left, playerRect.top, playerRect.right, playerRect.bottom);
    }
}