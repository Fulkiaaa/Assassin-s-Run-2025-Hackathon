package jeu.endlessrunner.bll.managers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import jeu.endlessrunner.be.Constants;

public class HealthManager {

    private final int MAX_HEARTS = 5;
    private final int HEART_SIZE = 60;
    private final int HEART_SPACING = 20;
    private final int DAMAGE_PER_HEART = 10;
    private final int START_X = (Constants.SCREEN_WIDTH - (MAX_HEARTS * HEART_SIZE + (MAX_HEARTS - 1) * HEART_SPACING)) / 2;
    private final int START_Y = (int) (Constants.START_FROM_TOP * 1.5f);

    private Paint mHeartPaint;
    private Paint mEmptyHeartPaint;
    private Paint mShadowPaint;
    private Path mHeartPath;
    private int mCurrentHearts;

    // === Nouveau pour effet de flash rouge ===
    private boolean isDamaged = false;
    private long damageFlashStartTime = 0;
    private static final long DAMAGE_FLASH_DURATION = 150; // ms

    public HealthManager() {
        mCurrentHearts = MAX_HEARTS;

        mHeartPaint = new Paint();
        mHeartPaint.setColor(Color.parseColor("#EA0707"));
        mHeartPaint.setStyle(Paint.Style.FILL);
        mHeartPaint.setAntiAlias(true);

        mEmptyHeartPaint = new Paint();
        mEmptyHeartPaint.setColor(Color.parseColor("#EA0707"));
        mEmptyHeartPaint.setStyle(Paint.Style.FILL);
        mEmptyHeartPaint.setAntiAlias(true);
        mEmptyHeartPaint.setAlpha(100);

        mShadowPaint = new Paint();
        mShadowPaint.setColor(Color.BLACK);
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setAlpha(100);

        mHeartPath = new Path();
    }

    public void draw(Canvas canvas) {
        // === Effet rouge si dégât récent ===
        if (isDamaged) {
            long elapsed = System.currentTimeMillis() - damageFlashStartTime;
            if (elapsed < DAMAGE_FLASH_DURATION) {
                Paint redFlash = new Paint();
                redFlash.setColor(Color.RED);
                redFlash.setAlpha(100); // Opacité
                canvas.drawRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, redFlash);
            } else {
                isDamaged = false;
            }
        }

        for (int i = 0; i < MAX_HEARTS; i++) {
            int x = START_X + i * (HEART_SIZE + HEART_SPACING);
            int y = START_Y;

            drawHeart(canvas, x + 3, y + 3, mShadowPaint);

            if (i >= MAX_HEARTS - mCurrentHearts) {
                drawHeart(canvas, x, y, mHeartPaint);
                drawHeartHighlight(canvas, x, y);
            } else {
                drawHeart(canvas, x, y, mEmptyHeartPaint);
            }
        }
    }

    private void drawHeart(Canvas canvas, int x, int y, Paint paint) {
        mHeartPath.reset();

        float centerX = x + HEART_SIZE / 2f;
        float centerY = y + HEART_SIZE / 2f;
        float size = HEART_SIZE * 0.4f;

        mHeartPath.moveTo(centerX, centerY + size * 0.3f);

        mHeartPath.cubicTo(
                centerX - size * 0.5f, centerY - size * 0.5f,
                centerX - size, centerY - size * 0.2f,
                centerX - size * 0.5f, centerY - size * 0.8f);

        mHeartPath.cubicTo(
                centerX - size * 0.8f, centerY - size,
                centerX - size * 0.2f, centerY - size,
                centerX, centerY - size * 0.5f);

        mHeartPath.cubicTo(
                centerX + size * 0.2f, centerY - size,
                centerX + size * 0.8f, centerY - size,
                centerX + size * 0.5f, centerY - size * 0.8f);

        mHeartPath.cubicTo(
                centerX + size, centerY - size * 0.2f,
                centerX + size * 0.5f, centerY - size * 0.5f,
                centerX, centerY + size * 0.3f);

        mHeartPath.close();
        canvas.drawPath(mHeartPath, paint);
    }

    private void drawHeartHighlight(Canvas canvas, int x, int y) {
        Paint highlightPaint = new Paint();
        highlightPaint.setColor(Color.parseColor("#FF6B6B"));
        highlightPaint.setStyle(Paint.Style.FILL);
        highlightPaint.setAntiAlias(true);

        float centerX = x + HEART_SIZE / 2f;
        float centerY = y + HEART_SIZE / 2f;
        float size = HEART_SIZE * 0.15f;

        RectF highlight = new RectF(
                centerX - size * 0.8f,
                centerY - size * 1.5f,
                centerX - size * 0.2f,
                centerY - size * 0.8f);

        canvas.drawOval(highlight, highlightPaint);
    }

    public boolean update(int damageTaken) {
        int heartsLost = damageTaken / DAMAGE_PER_HEART;
        int newHearts = Math.max(0, MAX_HEARTS - heartsLost);

        if (newHearts < mCurrentHearts) {
            isDamaged = true;
            damageFlashStartTime = System.currentTimeMillis();
        }

        mCurrentHearts = newHearts;
        return mCurrentHearts <= 0;
    }

    public void resetHealth() {
        mCurrentHearts = MAX_HEARTS;
    }

    public void addHeart() {
        if (mCurrentHearts < MAX_HEARTS) {
            mCurrentHearts++;
        }
    }

    public void removeHeart() {
        if (mCurrentHearts > 0) {
            mCurrentHearts--;
            isDamaged = true;
            damageFlashStartTime = System.currentTimeMillis();
        }
    }

    public int getCurrentHearts() {
        return mCurrentHearts;
    }

    public int getMaxHearts() {
        return MAX_HEARTS;
    }
}
