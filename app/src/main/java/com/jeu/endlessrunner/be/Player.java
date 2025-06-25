package com.jeu.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import com.jeu.endlessrunner.R;
import com.jeu.endlessrunner.bll.animation.Animation;
import com.jeu.endlessrunner.bll.animation.AnimationManager;

public class Player implements IGameObject {

    private Rect mRect;
    private int mColor;

    private Animation mRunAnimation;
    private Animation mJumpAnimation;

    private AnimationManager mAnimationManager;

    public Player(Rect rect, int color) {
        mRect = rect;
        mColor = color;

        createStickAnimations();

        mAnimationManager = new AnimationManager(new Animation[] { mRunAnimation, mJumpAnimation });
    }

    @Override
    public void draw(Canvas canvas) {
        // Paint paint = new Paint();
        // paint.setColor(mColor);
        // canvas.drawRect(mRect, paint);
        mAnimationManager.draw(canvas, mRect);
    }

    @Override
    public void update() {
        mAnimationManager.update();
    }

    public void update(Point point, boolean isJumping) {
        // Left, Top, Right, Back
        mRect.set(point.x - mRect.width() / 2,
                point.y - mRect.height() / 2,
                point.x + mRect.width() / 2,
                point.y + mRect.height() / 2);

        if (isJumping) {
            mAnimationManager.playAnimation(1);
            mAnimationManager.update();
        } else {
            mAnimationManager.playAnimation(0);
            mAnimationManager.update();
        }
    }

    public Rect getRect() {
        return mRect;
    }

    private void createStickAnimations() {
        BitmapFactory bf = new BitmapFactory();
        Bitmap img1 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.assassin_statique);
        // Bitmap img2 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),
        // R.drawable.assassin_run_1_1);
        Bitmap img2 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.assassin_run_1);
        // Bitmap img3 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),
        // R.drawable.assassin_run_1_2);
        Bitmap img3 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.assassin_run_2);
        Bitmap img4 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.aaaa);


        // Bitmap img4 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),
        // R.drawable.assassin_jumping);
        Bitmap img5 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.assassin_jumping);

        mRunAnimation = new Animation(new Bitmap[] { img2, img4 }, 0.3f);
        mJumpAnimation = new Animation(new Bitmap[] { img5 }, 2);
    }
}
