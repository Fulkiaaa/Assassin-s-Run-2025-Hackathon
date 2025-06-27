package jeu.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

// Classe représentant un templier (ennemi ou PNJ) dans le jeu
public class Templar {
    // Rectangle de collision et de dessin du templier
    private Rect mRect;
    // Image du templier
    private Bitmap mImage;

    // Constructeur
    public Templar(Rect rect, Bitmap image) {
        mRect = rect;
        mImage = image;
    }

    // Dessine le templier sur le canvas
    public void draw(Canvas canvas) {
        if (mImage != null && !mImage.isRecycled()) {
            // Si l'image est valide, on la dessine dans le rectangle
            canvas.drawBitmap(mImage, null, mRect, new Paint());
        } else {
            // DEBUG : Rectangle rouge visible si l'image est vide ou recyclée
            Paint debugPaint = new Paint();
            debugPaint.setColor(0xFFFF0000); // rouge
            debugPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(mRect, debugPaint);
        }
    }

    // Retourne le rectangle du templier (pour la collision ou la position)
    public Rect getRect() {
        return mRect;
    }

    // Retourne la position X centrale du templier
    public int getX() {
        return mRect.centerX();
    }

    // Retourne la position Y centrale du templier
    public int getY() {
        return mRect.centerY();
    }
}
