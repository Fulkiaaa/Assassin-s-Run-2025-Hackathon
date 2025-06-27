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

// Classe représentant la scène du menu principal du jeu
public class Menu implements IScene {

    private Floor mFloor; // Sol affiché dans le menu
    private Rect mTextRect; // Rectangle pour centrer le texte
    private Typeface mCinzelFont; // Police personnalisée pour le titre
    private Context mContext; // Contexte Android

    // Constructeur : initialise le sol, le rectangle de texte et la police
    public Menu(Context context) {
        mContext = context;
        mFloor = new Floor(new Rect());
        mTextRect = new Rect();

        // Charge la police personnalisée depuis les ressources
        mCinzelFont = ResourcesCompat.getFont(context, R.font.vcr_osd_mono);
    }

    // Pas de logique à mettre à jour dans le menu pour l'instant
    @Override
    public void update() {

    }

    // Dessine le fond du menu, le sol et le texte
    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#2A3F94")); // Fond bleu
        mFloor.draw(canvas); // Dessine le sol
        drawText(canvas, "Assassin's run", "Tapez pour jouer "); // Titre et sous-titre
    }

    // Libère les ressources si besoin (ici rien à faire)
    @Override
    public void terminate() {

    }

    // Gère le toucher sur l'écran : démarre la partie si on tape
    @Override
    public void recieveTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                SceneManager.ACTIVE_SCENE = Constants.GAME_SCENE;
                break;
            }
        }
    }

    // Méthode utilitaire pour dessiner le titre et le sous-titre centrés
    private void drawText(Canvas canvas, String headLine, String text) {
        Paint paint = new Paint();

        // Appliquer la police personnalisée si disponible
        if (mCinzelFont != null) {
            paint.setTypeface(mCinzelFont);
        }

        paint.setTextSize(200);
        paint.setColor(Color.parseColor("#C9161D")); // Rouge pour le titre
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