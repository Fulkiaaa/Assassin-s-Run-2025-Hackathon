package jeu.endlessrunner.bll.scenes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import androidx.core.content.res.ResourcesCompat;

import jeu.endlessrunner.be.Constants;
import jeu.endlessrunner.be.Floor;
import jeu.endlessrunner.bll.IScene;
import jeu.endlessrunner.bll.managers.SceneManager;
import jeu.endlessrunner.R;

public class Menu implements IScene {

    private Floor mFloor;
    private Rect mTextRect;
    private Typeface mCinzelFont;
    private Context mContext;

    public Menu(Context context) {
        mContext = context;
        mFloor = new Floor(new Rect());
        mTextRect = new Rect();

        // Charger la police personnalisée depuis res/font/
        mCinzelFont = ResourcesCompat.getFont(context, R.font.cinzel);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#2A3F94"));
        mFloor.draw(canvas);
        drawText(canvas, "Assassin's run", "Tapez pour jouer ");
    }

    @Override
    public void terminate() {

    }

    @Override
    public void recieveTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                SceneManager.ACTIVE_SCENE = Constants.GAME_SCENE;
                break;
            }
        }
    }

    private void drawText(Canvas canvas, String headLine, String text) {
        Paint paint = new Paint();

        // Appliquer la police personnalisée
        if (mCinzelFont != null) {
            paint.setTypeface(mCinzelFont);
        }

        paint.setTextSize(200);
        paint.setColor(Color.parseColor("#C9161D"));
        paint.setShadowLayer(5, 0, 0, Color.BLACK);
        paint.setAntiAlias(true); // Pour un rendu plus lisse

        paint.setTextAlign(Paint.Align.CENTER);
        canvas.getClipBounds(mTextRect);
        int cHeight = mTextRect.height();
        int cWidth = mTextRect.width();
        paint.getTextBounds(headLine, 0, headLine.length(), mTextRect);
        float x = cWidth / 2f;
        float y = cHeight / 4f;
        canvas.drawText(headLine, x, y, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(75);
        y = cHeight / 2f;
        canvas.drawText(text, x, y, paint);
    }
}