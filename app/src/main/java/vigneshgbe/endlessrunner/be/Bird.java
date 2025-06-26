package vigneshgbe.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Bird {
    private Rect mRect;
    private Bitmap mImage1;
    private Bitmap mImage2;
    private int mSpeed;

    private int mInitialY;
    private float mWaveAngle = 0;
    private float mWaveSpeed = 0.1f;
    private float mWaveAmplitude = 60f;

    private long mLastFrameTime = 0;
    private boolean mUseImage1 = true;
    private static final long FRAME_INTERVAL = 150; // ms

    public Bird(Rect rect, Bitmap image1, Bitmap image2, int speed) {
        mRect = rect;
        mImage1 = image1;
        mImage2 = image2;
        mSpeed = speed;
        mInitialY = rect.top;
    }

    public void update() {
        mRect.left -= mSpeed;
        mRect.right -= mSpeed;

        // Mouvement sinusoïdal
        mWaveAngle += mWaveSpeed;
        int offsetY = (int) (Math.sin(mWaveAngle) * mWaveAmplitude);
        int height = mRect.height();

        mRect.top = mInitialY + offsetY;
        mRect.bottom = mRect.top + height;

        // Changement d'image (animation)
        long now = System.currentTimeMillis();
        if (now - mLastFrameTime >= FRAME_INTERVAL) {
            mUseImage1 = !mUseImage1;
            mLastFrameTime = now;
        }
    }

    public void draw(Canvas canvas) {
        Bitmap current = mUseImage1 ? mImage1 : mImage2;
        canvas.drawBitmap(current, null, mRect, new Paint());
    }

    public boolean collidesWith(Rect playerRect) {
        return Rect.intersects(mRect, playerRect);
    }

    public Rect getRect() {
        return mRect;
    }

    public boolean isOffScreen() {
        return mRect.right < 0;
    }
}
