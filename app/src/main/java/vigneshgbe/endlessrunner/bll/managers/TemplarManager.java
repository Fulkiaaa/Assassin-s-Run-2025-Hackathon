package vigneshgbe.endlessrunner.bll.managers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import vigneshgbe.endlessrunner.R;
import vigneshgbe.endlessrunner.be.Constants;
import vigneshgbe.endlessrunner.be.Dagger;
import vigneshgbe.endlessrunner.be.Floor;
import vigneshgbe.endlessrunner.be.IGameObject;
import vigneshgbe.endlessrunner.be.Templar;

public class TemplarManager implements IGameObject {
    private List<Templar> templars;
    private List<Dagger> daggers;
    private Bitmap templarImage;
    private Bitmap daggerImage;
    private long lastShotTime = 0;

    private static final int SHOT_INTERVAL = 2000; // ms
    private static final int TEMPLAR_GAP = 800; // Distance entre templiers
    private static final float TEMPLAR_SPEED = 15f;
    private static final float DAGGER_SPEED = 25f;

    private Random random;
    private Floor mFloor;

    public TemplarManager(Floor floor) {
        templars = new ArrayList<>();
        daggers = new ArrayList<>();
        templarImage = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.templar);
        daggerImage = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.dagger);
        random = new Random();
        mFloor = floor;

        populateTemplars();
    }

    private void populateTemplars() {
        int templarWidth = 100;
        int templarHeight = 120;
        int groundY = mFloor.getRect().top - templarHeight + 50; // Ajustement pour la hauteur du sol

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

    @Override
    public void update() {
        // Déplacement des templiers
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

        // Ajout de nouveaux templiers
        if (!templars.isEmpty()) {
            Templar lastTemplar = templars.get(templars.size() - 1);
            if (lastTemplar.getRect().right < 4 * Constants.SCREEN_WIDTH) {
                int templarWidth = 100;
                int templarHeight = 120;
                int groundY = mFloor.getRect().top - templarHeight + 50;
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

        // Tir de dague multiple (tous les templiers visibles)
        long now = System.currentTimeMillis();
        if (now - lastShotTime >= SHOT_INTERVAL && !templars.isEmpty()) {
            for (Templar t : templars) {
                Rect rect = t.getRect();
                if (rect.right > 0 && rect.left < Constants.SCREEN_WIDTH) {
                    int daggerY = rect.top + rect.height() / 2 - 10;
                    int daggerX = rect.left;

                    daggers.add(new Dagger(
                            new Rect(daggerX, daggerY, daggerX + 40, daggerY + 20),
                            daggerImage,
                            25));
                }
            }
            lastShotTime = now;
        }

        for (Dagger d : daggers)
            d.update();

        Iterator<Dagger> daggerIt = daggers.iterator();
        while (daggerIt.hasNext()) {
            Dagger d = daggerIt.next();
            if (d.getRect().right < 0 || d.getRect().left > Constants.SCREEN_WIDTH) {
                daggerIt.remove();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        for (Templar t : templars)
            t.draw(canvas);
        for (Dagger d : daggers)
            d.draw(canvas);
    }

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

        for (Templar t : templars) {
            if (Rect.intersects(t.getRect(), player)) {
                return true;
            }
        }

        return false;
    }

    public int getTemplarCount() {
        return templars.size();
    }

    public int getDaggerCount() {
        return daggers.size();
    }
}
