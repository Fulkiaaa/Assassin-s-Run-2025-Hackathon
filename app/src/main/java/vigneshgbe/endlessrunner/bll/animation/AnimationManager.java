package vigneshgbe.endlessrunner.bll.animation;

import android.graphics.Canvas;
import android.graphics.Rect;

public class AnimationManager {

    private Animation[] mAnimations;
    private int mAnimationIndex;

    public AnimationManager(Animation[] animations) {
        mAnimations = animations;
        mAnimationIndex = 0;
    }

    public void playAnimation(int index) {
        if (mAnimationIndex != index) {
            mAnimations[mAnimationIndex].stop();
            mAnimationIndex = index;
            mAnimations[mAnimationIndex].play();
        }
    }

    public void draw(Canvas canvas, Rect rect) {
        if (mAnimations[mAnimationIndex].isPlaying()) {
            mAnimations[mAnimationIndex].draw(canvas, rect);
        }
    }

    public void update() {
        if (mAnimations[mAnimationIndex].isPlaying()) {
            mAnimations[mAnimationIndex].update();
        }
    }
}