package jeu.endlessrunner.bll.managers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import jeu.endlessrunner.R;
import jeu.endlessrunner.be.Constants;
import jeu.endlessrunner.be.Dagger;
import jeu.endlessrunner.be.Floor;
import jeu.endlessrunner.be.IGameObject;
import jeu.endlessrunner.be.Templar;

// Classe qui gère les templiers ennemis et leurs projectiles (dagues)
public class TemplarManager implements IGameObject {
    private List<Templar> templars; // Liste des templiers actifs
    private List<Dagger> daggers; // Liste des dagues actives
    private Bitmap templarImage; // Image du templier
    private Bitmap daggerImage; // Image de la dague
    private long lastShotTime = 0; // Dernier tir de dague (pour le cooldown)

    private static final int SHOT_INTERVAL = 2000; // Intervalle entre tirs (ms)
    private static final int TEMPLAR_GAP = 800; // Distance entre deux templiers
    private static final float TEMPLAR_SPEED = 15f;
    private static final float DAGGER_SPEED = 25f;

    private Random random;
    private Floor mFloor;

    // Constructeur
    public TemplarManager(Floor floor) {
        templars = new ArrayList<>();
        daggers = new ArrayList<>();
        templarImage = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.templar);
        daggerImage = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.dagger);
        random = new Random();
        mFloor = floor;

        populateTemplars();
    }

    // Génère les templiers de départ hors de l'écran à droite
    private void populateTemplars() {
        int templarWidth = 100;
        int templarHeight = 120;
        int groundY = mFloor.getRect().top - templarHeight + 20; // Ajustement pour la hauteur du sol

        int currX = 3 * Constants.SCREEN_WIDTH / 2;

        while (currX < 5 * Constants.SCREEN_WIDTH) {
            int randomOffset = random.nextInt(TEMPLAR_GAP / 2);

            Templar newTemplar = new Templar(
                    new Rect(currX + randomOffset, groundY,
                            currX + randomOffset + templarWidth, groundY + templarHeight),
                    templarImage);
            templars.add(newTemplar);
            currX += TEMPLAR_GAP;
        }
    }

    // Met à jour la position des templiers et des dagues, gère les tirs et le
    // recyclage
    @Override
    public void update() {
        // Déplacement des templiers vers la gauche
        for (Templar t : templars) {
            t.getRect().offset(-(int) TEMPLAR_SPEED, 0);
        }

        // Suppression des templiers sortis de l'écran
        Iterator<Templar> templarIt = templars.iterator();
        while (templarIt.hasNext()) {
            if (templarIt.next().getRect().right < 0) {
                templarIt.remove();
            }
        }

        // Ajout de nouveaux templiers si besoin
        if (!templars.isEmpty()) {
            Templar lastTemplar = templars.get(templars.size() - 1);
            if (lastTemplar.getRect().right < 4 * Constants.SCREEN_WIDTH) {
                int templarWidth = 100;
                int templarHeight = 120;
                int groundY = mFloor.getRect().top - templarHeight + 20;
                int randomOffset = random.nextInt(TEMPLAR_GAP / 2);

                Templar newTemplar = new Templar(
                        new Rect(lastTemplar.getRect().right + TEMPLAR_GAP + randomOffset, groundY,
                                lastTemplar.getRect().right + TEMPLAR_GAP + randomOffset + templarWidth,
                                groundY + templarHeight),
                        templarImage);
                templars.add(newTemplar);
            }
        } else {
            populateTemplars();
        }

        // Tir de dagues par tous les templiers visibles à l'écran à intervalle régulier
        long now = System.currentTimeMillis();
        if (now - lastShotTime >= SHOT_INTERVAL && !templars.isEmpty()) {
            for (Templar t : templars) {
                Rect rect = t.getRect();
                if (rect.right > 0 && rect.left < Constants.SCREEN_WIDTH) {
                    int daggerY = rect.top + rect.height() / 2 - 10;
                    int daggerX = rect.left;

                    daggers.add(new Dagger(
                            new Rect(daggerX, daggerY, daggerX + 40, daggerY + 50),
                            daggerImage,
                            25));
                }
            }
            lastShotTime = now;
        }

        // Mise à jour des dagues (déplacement, rotation)
        for (Dagger d : daggers)
            d.update();

        // Suppression des dagues sorties de l'écran
        Iterator<Dagger> daggerIt = daggers.iterator();
        while (daggerIt.hasNext()) {
            Dagger d = daggerIt.next();
            if (d.getRect().right < 0 || d.getRect().left > Constants.SCREEN_WIDTH) {
                daggerIt.remove();
            }
        }
    }

    // Dessine tous les templiers et dagues sur le canvas
    @Override
    public void draw(Canvas canvas) {
        for (Templar t : templars)
            t.draw(canvas);
        for (Dagger d : daggers)
            d.draw(canvas);
    }

    // Vérifie la collision entre le joueur et les dagues ou templiers
    // Les dagues sont supprimées si elles touchent un obstacle ou le joueur
    public boolean checkCollision(Rect player, List<Rect> obstacles) {
        Iterator<Dagger> daggerIt = daggers.iterator();
        while (daggerIt.hasNext()) {
            Dagger d = daggerIt.next();

            boolean hitObstacle = false;
            for (Rect obs : obstacles) {
                if (Rect.intersects(d.getRect(), obs)) {
                    hitObstacle = true;
                    break;
                }
            }

            if (hitObstacle) {
                daggerIt.remove();
                continue;
            }

            if (Rect.intersects(d.getRect(), player)) {
                daggerIt.remove();
                return true;
            }
        }

        // Collision directe entre le joueur et un templier
        for (Templar t : templars) {
            if (Rect.intersects(t.getRect(), player)) {
                return true;
            }
        }

        return false;
    }

    // Retourne le nombre de templiers actifs
    public int getTemplarCount() {
        return templars.size();
    }

    // Retourne le nombre de dagues actives
    public int getDaggerCount() {
        return daggers.size();
    }
}
