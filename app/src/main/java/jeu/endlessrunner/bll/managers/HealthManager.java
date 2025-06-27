package jeu.endlessrunner.bll.managers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import jeu.endlessrunner.be.Constants;

// Classe pour gérer la barre de vie du joueur sous forme de cœurs
public class HealthManager {

    // Nombre maximum de cœurs affichés
    private final int MAX_HEARTS = 5;
    // Taille d'un cœur en pixels
    private final int HEART_SIZE = 60;
    // Espace entre les cœurs
    private final int HEART_SPACING = 20;
    // Dégâts nécessaires pour perdre un cœur
    private final int DAMAGE_PER_HEART = 10;
    // Position X de départ (centré horizontalement)
    private final int START_X = (Constants.SCREEN_WIDTH - (MAX_HEARTS * HEART_SIZE + (MAX_HEARTS - 1) * HEART_SPACING))
            / 2;
    // Position Y de départ (un peu en dessous du haut de l'écran)
    private final int START_Y = (int) (Constants.START_FROM_TOP * 1.5f);

    private Paint mHeartPaint; // Peinture pour les cœurs pleins
    private Paint mEmptyHeartPaint; // Peinture pour les cœurs vides
    private Paint mShadowPaint; // Peinture pour l'ombre des cœurs
    private Path mHeartPath; // Chemin pour dessiner la forme du cœur
    private int mCurrentHearts; // Nombre de cœurs restants

    // Pour l'effet de flash rouge lors d'un dégât
    private boolean isDamaged = false;
    private long damageFlashStartTime = 0;
    private static final long DAMAGE_FLASH_DURATION = 150; // Durée du flash en ms

    // Constructeur
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
        mEmptyHeartPaint.setAlpha(100); // Plus transparent pour les cœurs vides

        mShadowPaint = new Paint();
        mShadowPaint.setColor(Color.BLACK);
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setAlpha(100);

        mHeartPath = new Path();
    }

    // Dessine la barre de vie (cœurs) sur le canvas
    public void draw(Canvas canvas) {
        // Effet rouge si dégât récent
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

        // Dessine chaque cœur (plein ou vide)
        for (int i = 0; i < MAX_HEARTS; i++) {
            int x = START_X + i * (HEART_SIZE + HEART_SPACING);
            int y = START_Y;

            // Ombre du cœur
            drawHeart(canvas, x + 3, y + 3, mShadowPaint);

            if (i >= MAX_HEARTS - mCurrentHearts) {
                // Cœur plein
                drawHeart(canvas, x, y, mHeartPaint);
                drawHeartHighlight(canvas, x, y);
            } else {
                // Cœur vide
                drawHeart(canvas, x, y, mEmptyHeartPaint);
            }
        }
    }

    // Dessine un cœur à la position donnée avec la peinture donnée
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

    // Dessine un petit reflet sur le cœur plein
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

    // Met à jour la vie selon les dégâts subis, retourne true si le joueur n'a plus
    // de vie
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

    // Remet la vie au maximum
    public void resetHealth() {
        mCurrentHearts = MAX_HEARTS;
    }

    // Ajoute un cœur (si pas déjà au max)
    public void addHeart() {
        if (mCurrentHearts < MAX_HEARTS) {
            mCurrentHearts++;
        }
    }

    // Retire un cœur (si possible) et déclenche l'effet de dégât
    public void removeHeart() {
        if (mCurrentHearts > 0) {
            mCurrentHearts--;
            isDamaged = true;
            damageFlashStartTime = System.currentTimeMillis();
        }
    }

    // Retourne le nombre de cœurs actuels
    public int getCurrentHearts() {
        return mCurrentHearts;
    }

    // Retourne le nombre maximum de cœurs
    public int getMaxHearts() {
        return MAX_HEARTS;
    }
}
