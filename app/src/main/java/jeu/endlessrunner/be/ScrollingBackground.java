package jeu.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class ScrollingBackground implements IGameObject {

    private Bitmap mImage;
    private Rect mRect1, mRect2;
    private int mSpeed;
    private int mScreenWidth;
    private int mScreenHeight;

    public ScrollingBackground(Bitmap image, int screenWidth, int screenHeight, int speed) {
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        mSpeed = speed;

        // Adapter la hauteur du background pour qu’il s’arrête au-dessus du sol
        int backgroundHeight = screenHeight - Constants.FLOOR_HEIGHT;

        // Redimensionner le background
        mImage = Bitmap.createScaledBitmap(image, screenWidth, backgroundHeight, true);

        mRect1 = new Rect(0, 0, screenWidth, backgroundHeight);
        mRect2 = new Rect(screenWidth, 0, screenWidth * 2, backgroundHeight);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(mImage, null, mRect1, new Paint());
        canvas.drawBitmap(mImage, null, mRect2, new Paint());
    }

    @Override
    public void update() {
        mRect1.left -= mSpeed;
        mRect1.right -= mSpeed;

        mRect2.left -= mSpeed;
        mRect2.right -= mSpeed;

        if (mRect1.right <= 0) {
            mRect1.left = mRect2.right;
            mRect1.right = mRect1.left + mScreenWidth;
        }

        if (mRect2.right <= 0) {
            mRect2.left = mRect1.right;
            mRect2.right = mRect2.left + mScreenWidth;
        }
    }
}
