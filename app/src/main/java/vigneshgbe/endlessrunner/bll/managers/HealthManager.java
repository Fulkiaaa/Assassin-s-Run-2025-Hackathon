package vigneshgbe.endlessrunner.bll.managers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import vigneshgbe.endlessrunner.be.Constants;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class HealthManager {

    private final int MAX_HEARTS = 5; // Nombre maximum de cœurs
    private final int HEART_SIZE = 60;
    private final int HEART_SPACING = 20;
    private final int DAMAGE_PER_HEART = 10; // Dégâts nécessaires pour perdre un cœur
    private final int START_X = (Constants.SCREEN_WIDTH - (MAX_HEARTS * HEART_SIZE + (MAX_HEARTS - 1) * HEART_SPACING))
            / 2;
    private final int START_Y = (int) (Constants.START_FROM_TOP * 1.5f);

    private Paint mHeartPaint;
    private Paint mEmptyHeartPaint;
    private Paint mShadowPaint;
    private Path mHeartPath;
    private int mCurrentHearts;

    public HealthManager() {
        mCurrentHearts = MAX_HEARTS;

        // Paint pour les cœurs pleins
        mHeartPaint = new Paint();
        mHeartPaint.setColor(Color.parseColor("#EA0707"));
        mHeartPaint.setStyle(Paint.Style.FILL);
        mHeartPaint.setAntiAlias(true);

        // Paint pour les cœurs vides
        mEmptyHeartPaint = new Paint();
        mEmptyHeartPaint.setColor(Color.parseColor("#EA0707"));
        mEmptyHeartPaint.setStyle(Paint.Style.FILL);
        mEmptyHeartPaint.setAntiAlias(true);

        // Paint pour l'ombre
        mShadowPaint = new Paint();
        mShadowPaint.setColor(Color.BLACK);
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setAlpha(100); // Semi-transparent
        mEmptyHeartPaint.setAlpha(100);

        mHeartPath = new Path();
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < MAX_HEARTS; i++) {
            int x = START_X + i * (HEART_SIZE + HEART_SPACING);
            int y = START_Y;

            // Dessiner l'ombre d'abord (légèrement décalée)
            drawHeart(canvas, x + 3, y + 3, mShadowPaint);

            // Inverser la logique : les cœurs pleins sont à droite
            if (i >= MAX_HEARTS - mCurrentHearts) {
                // Cœur plein avec effet de brillance
                drawHeart(canvas, x, y, mHeartPaint);
                drawHeartHighlight(canvas, x, y);
            } else {
                // Cœur vide
                drawHeart(canvas, x, y, mEmptyHeartPaint);
            }
        }
    }

    /**
     * Dessine un cœur à la position donnée
     */
    private void drawHeart(Canvas canvas, int x, int y, Paint paint) {
        mHeartPath.reset();

        float centerX = x + HEART_SIZE / 2f;
        float centerY = y + HEART_SIZE / 2f;
        float size = HEART_SIZE * 0.4f;

        // Créer la forme du cœur
        mHeartPath.moveTo(centerX, centerY + size * 0.3f);

        // Côté gauche du cœur
        mHeartPath.cubicTo(
                centerX - size * 0.5f, centerY - size * 0.5f,
                centerX - size, centerY - size * 0.2f,
                centerX - size * 0.5f, centerY - size * 0.8f);

        // Arc gauche
        mHeartPath.cubicTo(
                centerX - size * 0.8f, centerY - size,
                centerX - size * 0.2f, centerY - size,
                centerX, centerY - size * 0.5f);

        // Arc droit
        mHeartPath.cubicTo(
                centerX + size * 0.2f, centerY - size,
                centerX + size * 0.8f, centerY - size,
                centerX + size * 0.5f, centerY - size * 0.8f);

        // Côté droit du cœur
        mHeartPath.cubicTo(
                centerX + size, centerY - size * 0.2f,
                centerX + size * 0.5f, centerY - size * 0.5f,
                centerX, centerY + size * 0.3f);

        mHeartPath.close();
        canvas.drawPath(mHeartPath, paint);
    }

    /**
     * Ajoute un effet de brillance sur les cœurs pleins
     */
    private void drawHeartHighlight(Canvas canvas, int x, int y) {
        Paint highlightPaint = new Paint();
        highlightPaint.setColor(Color.parseColor("#FF6B6B")); // Rouge plus clair
        highlightPaint.setStyle(Paint.Style.FILL);
        highlightPaint.setAntiAlias(true);

        float centerX = x + HEART_SIZE / 2f;
        float centerY = y + HEART_SIZE / 2f;
        float size = HEART_SIZE * 0.15f;

        // Petit ovale pour la brillance
        RectF highlight = new RectF(
                centerX - size * 0.8f,
                centerY - size * 1.5f,
                centerX - size * 0.2f,
                centerY - size * 0.8f);

        canvas.drawOval(highlight, highlightPaint);
    }

    /**
     * Met à jour le système de vie basé sur les dégâts reçus
     *
     * @param damageTaken Total des dégâts reçus
     * @return true si le joueur est mort (plus de cœurs)
     */
    public boolean update(int damageTaken) {
        // Calculer le nombre de cœurs restants
        int heartsLost = damageTaken / DAMAGE_PER_HEART;
        mCurrentHearts = Math.max(0, MAX_HEARTS - heartsLost);

        // Retourner true si le joueur n'a plus de cœurs
        return mCurrentHearts <= 0;
    }

    /**
     * Remet la vie au maximum (pour un nouveau jeu)
     */
    public void resetHealth() {
        mCurrentHearts = MAX_HEARTS;
    }

    /**
     * Ajoute un cœur (pour des power-ups par exemple)
     */
    public void addHeart() {
        if (mCurrentHearts < MAX_HEARTS) {
            mCurrentHearts++;
        }
    }

    /**
     * Enlève un cœur directement
     */
    public void removeHeart() {
        if (mCurrentHearts > 0) {
            mCurrentHearts--;
        }
    }

    /**
     * Retourne le nombre de cœurs actuels
     */
    public int getCurrentHearts() {
        return mCurrentHearts;
    }

    /**
     * Retourne le nombre maximum de cœurs
     */
    public int getMaxHearts() {
        return MAX_HEARTS;
    }
}