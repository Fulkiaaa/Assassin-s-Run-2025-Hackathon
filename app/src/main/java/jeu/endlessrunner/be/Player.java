package jeu.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import jeu.endlessrunner.R;
import jeu.endlessrunner.bll.animation.Animation;
import jeu.endlessrunner.bll.animation.AnimationManager;

public class Player implements IGameObject {

    private Rect mRect;
    private int mColor;

    private Animation mRunAnimation;
    private Animation mJumpAnimation;
    private Animation mDoubleJumpAnimation;

    private AnimationManager mAnimationManager;

    public Player(Rect rect, int color) {
        mRect = rect;
        mColor = color;

        createStickAnimations();

        mAnimationManager = new AnimationManager(new Animation[] {
                mRunAnimation,
                mJumpAnimation,
                mDoubleJumpAnimation
        });
    }

    @Override
    public void draw(Canvas canvas) {
        mAnimationManager.draw(canvas, mRect);
    }

    @Override
    public void update() {
        mAnimationManager.update();
    }

    public void update(Point point, boolean isJumping, boolean isDoubleJumping) {
        mRect.set(point.x - mRect.width() / 2,
                point.y - mRect.height() / 2,
                point.x + mRect.width() / 2,
                point.y + mRect.height() / 2);

        if (isDoubleJumping) {
            mAnimationManager.playAnimation(2);
        } else if (isJumping) {
            mAnimationManager.playAnimation(1);
        } else {
            mAnimationManager.playAnimation(0);
        }

        mAnimationManager.update();
    }

    public Rect getRect() {
        return mRect;
    }

    private void createStickAnimations() {
        BitmapFactory bf = new BitmapFactory();
        Bitmap run1 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.assassin_run_1);
        Bitmap run2 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.assassin_running_back);
        Bitmap jump = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.assassin_jumping);
        Bitmap doubleJump = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.assassin_double_jumping);

        mRunAnimation = new Animation(new Bitmap[] { run1, run2 }, 0.3f);
        mJumpAnimation = new Animation(new Bitmap[] { jump }, 2f);
        mDoubleJumpAnimation = new Animation(new Bitmap[] { doubleJump }, 2f);
    }
}
