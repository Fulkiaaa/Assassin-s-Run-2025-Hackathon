package vigneshgbe.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Dagger {
    private Rect mRect;
    private Bitmap mImage;
    private int mSpeed;

    public Dagger(Rect rect, Bitmap image, int speed) {
        mRect = rect;
        mImage = image;
        mSpeed = speed;
    }

    public void update() {
        mRect.left -= mSpeed;
        mRect.right -= mSpeed;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(mImage, null, mRect, new Paint());
    }

    public boolean collidesWith(Rect target) {
        return Rect.intersects(mRect, target);
    }

    public boolean isOffScreen() {
        return mRect.right < 0;
    }

    public Rect getRect() {
        return mRect;
    }
}
