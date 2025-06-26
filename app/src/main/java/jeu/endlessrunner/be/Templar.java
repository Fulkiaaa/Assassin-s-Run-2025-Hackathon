package jeu.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Templar {
    private Rect mRect;
    private Bitmap mImage;

    public Templar(Rect rect, Bitmap image) {
        mRect = rect;
        mImage = image;
    }

    public void draw(Canvas canvas) {
        if (mImage != null && !mImage.isRecycled()) {
            canvas.drawBitmap(mImage, null, mRect, new Paint());
        } else {
            // DEBUG : Rectangle rouge visible si l'image est vide
            Paint debugPaint = new Paint();
            debugPaint.setColor(0xFFFF0000); // rouge
            debugPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(mRect, debugPaint);
        }
    }

    public Rect getRect() {
        return mRect;
    }

    public int getX() {
        return mRect.centerX();
    }

    public int getY() {
        return mRect.centerY();
    }
}
