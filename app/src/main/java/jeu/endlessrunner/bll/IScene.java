package jeu.endlessrunner.bll;

import android.graphics.Canvas;
import android.view.MotionEvent;

// Interface pour représenter une scène du jeu (menu, gameplay, etc.)
public interface IScene {
    // Met à jour la logique de la scène (appelé à chaque frame)
    public void update();

    // Dessine la scène sur le canvas
    public void draw(Canvas canvas);

    // Libère les ressources de la scène (appelé à la fin)
    public void terminate();

    // Gère les événements tactiles reçus par la scène
    public void recieveTouch(MotionEvent event);
}
