package vigneshgbe.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import vigneshgbe.endlessrunner.R;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;



public class Floor implements IGameObject {

    // Largeur de votre image complète de route
    private final int WIDTH = Constants.SCREEN_WIDTH;
    private final int Y_COORDINATE = Constants.SCREEN_HEIGHT - 300;

    private Rect mRect;

    private List<Rect> mRectList;
    private Bitmap mFloorImage;

    public Floor(Rect rect) {
        mRect = rect;
        mRect.set(0, Y_COORDINATE, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        BitmapFactory bf = new BitmapFactory();
        mFloorImage = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.route);

        mRectList = new ArrayList<>();

        // Créer seulement 2 instances de votre route complète pour l'effet de
        // défilement
        // Une à l'écran, une autre qui arrive
        mRectList.add(new Rect(0, Y_COORDINATE, WIDTH, Constants.SCREEN_HEIGHT));
        mRectList.add(new Rect(WIDTH, Y_COORDINATE, WIDTH * 2, Constants.SCREEN_HEIGHT));
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setFilterBitmap(true); // Lissage de l'image

        for (Rect rect : mRectList) {
            canvas.drawBitmap(mFloorImage, null, rect, paint);
        }
    }

    @Override
    public void update() {
        // Parallax scrolling - ajustez le multiplicateur selon vos préférences
        float parallaxSpeed = Constants.SPEED * 1.2f;

        for (Rect rect : mRectList) {
            rect.set(rect.left - (int) parallaxSpeed, Y_COORDINATE,
                    rect.right - (int) parallaxSpeed, Constants.SCREEN_HEIGHT);
        }

        // Quand la première route sort complètement de l'écran,
        // on la remet à la fin pour créer la boucle infinie
        if (mRectList.get(0).right <= 0) {
            Rect rect = mRectList.remove(0);
            int x = mRectList.get(mRectList.size() - 1).right;
            rect.set(x, Y_COORDINATE, x + WIDTH, Constants.SCREEN_HEIGHT);
            mRectList.add(rect);
        }
    }

    public Rect getRect() {
        return mRect;
    }
}