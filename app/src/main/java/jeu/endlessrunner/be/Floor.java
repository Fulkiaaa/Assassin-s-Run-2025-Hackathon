package jeu.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import jeu.endlessrunner.R;

public class Floor implements IGameObject {

    private final int WIDTH = Constants.SCREEN_WIDTH;

    // Hauteur réelle de l'image au sol
    private final int IMAGE_HEIGHT = 300;

    // Hauteur utilisée pour la collision (hitbox)
    private final int HITBOX_HEIGHT = 250;

    // Y de départ (sol visuel en bas de l'écran)
    private final int Y_COORDINATE = Constants.SCREEN_HEIGHT - IMAGE_HEIGHT;

    private Rect mRect;
    private List<Rect> mRectList;
    private Bitmap mFloorImage;

    public Floor(Rect rect) {
        mRect = rect;
        // Hitbox sur 250px (invisible mais pour collision)
        mRect.set(0, Constants.SCREEN_HEIGHT - HITBOX_HEIGHT, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        BitmapFactory bf = new BitmapFactory();
        mFloorImage = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.route);

        mRectList = new ArrayList<>();
        // Image dessinée sur 300px (visuel complet)
        mRectList.add(new Rect(0, Y_COORDINATE, WIDTH, Y_COORDINATE + IMAGE_HEIGHT));
        mRectList.add(new Rect(WIDTH, Y_COORDINATE, WIDTH * 2, Y_COORDINATE + IMAGE_HEIGHT));
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setFilterBitmap(true);

        for (Rect rect : mRectList) {
            canvas.drawBitmap(mFloorImage, null, rect, paint);
        }
    }

    @Override
    public void update() {
        float parallaxSpeed = Constants.SPEED * 1.2f;

        for (Rect rect : mRectList) {
            rect.set(rect.left - (int) parallaxSpeed, rect.top,
                    rect.right - (int) parallaxSpeed, rect.bottom);
        }

        if (mRectList.get(0).right <= 0) {
            Rect rect = mRectList.remove(0);
            int x = mRectList.get(mRectList.size() - 1).right;
            rect.set(x, Y_COORDINATE, x + WIDTH, Y_COORDINATE + IMAGE_HEIGHT);
            mRectList.add(rect);
        }
    }

    public Rect getRect() {
        return mRect; // hitbox réelle
    }
}
