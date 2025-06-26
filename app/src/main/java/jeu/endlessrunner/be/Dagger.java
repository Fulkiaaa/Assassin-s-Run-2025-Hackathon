package jeu.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

public class Dagger {
    private Rect mRect;
    private Bitmap mImage;
    private int mSpeed;
    private float mRotationAngle = 0f;

    public Dagger(Rect rect, Bitmap image, int speed) {
        mRect = rect;
        mImage = image;
        mSpeed = speed;
    }

    public void update() {
        mRect.left -= mSpeed;
        mRect.right -= mSpeed;

        mRotationAngle += 15f;
        if (mRotationAngle >= 360f) mRotationAngle -= 360f;
    }

    public void draw(Canvas canvas) {
        int centerX = mRect.centerX();
        int centerY = mRect.centerY();

        float scaleX = (float) mRect.width() / mImage.getWidth();
        float scaleY = (float) mRect.height() / mImage.getHeight();

        Matrix matrix = new Matrix();
        matrix.postTranslate(-mImage.getWidth() / 2f, -mImage.getHeight() / 2f); // origine au centre
        matrix.postRotate(mRotationAngle); // rotation
        matrix.postScale(scaleX, scaleY); // redimensionne l’image à la taille du rect
        matrix.postTranslate(centerX, centerY); // repositionne sur le rect

        canvas.drawBitmap(mImage, matrix, new Paint());
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
