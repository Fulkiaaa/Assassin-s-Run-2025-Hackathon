package jeu.endlessrunner.be;

import android.graphics.Canvas;

// Interface pour les objets du jeu pouvant être dessinés et mis à jour
public interface IGameObject {

    // Méthode pour dessiner l'objet sur le canvas
    public void draw(Canvas canvas);

    // Méthode pour mettre à jour l'état de l'objet (position, animation, etc.)
    public void update();
}
