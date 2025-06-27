package jeu.endlessrunner.be;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import jeu.endlessrunner.R;

// Classe représentant le sol du jeu
public class Floor implements IGameObject {

    // Largeur du sol (égale à la largeur de l'écran)
    private final int WIDTH = Constants.SCREEN_WIDTH;

    // Hauteur réelle de l'image du sol (affichage visuel)
    private final int IMAGE_HEIGHT = 300;

    // Hauteur utilisée pour la collision (hitbox, invisible)
    private final int HITBOX_HEIGHT = 250;

    // Coordonnée Y du sol (placé en bas de l'écran)
    private final int Y_COORDINATE = Constants.SCREEN_HEIGHT - IMAGE_HEIGHT;

    // Rectangle de collision (hitbox)
    private Rect mRect;
    // Liste des rectangles pour l'affichage du sol (pour le défilement infini)
    private List<Rect> mRectList;
    // Image du sol
    private Bitmap mFloorImage;

    // Constructeur
    public Floor(Rect rect) {
        mRect = rect;
        // Définition de la hitbox sur 250px de haut (pour la collision)
        mRect.set(0, Constants.SCREEN_HEIGHT - HITBOX_HEIGHT, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        // Chargement de l'image du sol
        BitmapFactory bf = new BitmapFactory();
        mFloorImage = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.route);

        mRectList = new ArrayList<>();
        // Ajout de deux rectangles pour permettre le défilement infini du sol
        mRectList.add(new Rect(0, Y_COORDINATE, WIDTH, Y_COORDINATE + IMAGE_HEIGHT));
        mRectList.add(new Rect(WIDTH, Y_COORDINATE, WIDTH * 2, Y_COORDINATE + IMAGE_HEIGHT));
    }

    // Dessine le sol sur le canvas
    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setFilterBitmap(true);

        // Dessine chaque portion du sol (pour l'effet de défilement)
        for (Rect rect : mRectList) {
            canvas.drawBitmap(mFloorImage, null, rect, paint);
        }
    }

    // Met à jour la position du sol pour le faire défiler
    @Override
    public void update() {
        // Vitesse de défilement du sol (effet de parallaxe)
        float parallaxSpeed = Constants.SPEED * 1.2f;

        // Déplace chaque rectangle vers la gauche
        for (Rect rect : mRectList) {
            rect.set(rect.left - (int) parallaxSpeed, rect.top,
                    rect.right - (int) parallaxSpeed, rect.bottom);
        }

        // Si une portion du sol sort complètement de l'écran à gauche,
        // on la replace à la suite de la dernière portion pour un défilement infini
        if (mRectList.get(0).right <= 0) {
            Rect rect = mRectList.remove(0);
            int x = mRectList.get(mRectList.size() - 1).right;
            rect.set(x, Y_COORDINATE, x + WIDTH, Y_COORDINATE + IMAGE_HEIGHT);
            mRectList.add(rect);
        }
    }

    // Retourne la hitbox réelle du sol (pour la collision)
    public Rect getRect() {
        return mRect;
    }
}
