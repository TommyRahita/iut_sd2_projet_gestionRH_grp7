package JavaProject;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Classe BordureArrondie.
 * <p>
 * Cette classe permet de dessiner une bordure arrondie pour les composants, notamment les boutons.
 * Elle implémente l'interface Border afin de fournir un rendu personnalisé avec des coins arrondis.
 * </p>
 * 
 * @author Groupe 7
 * @version 1.0
 */
class BordureArrondie implements Border {
    private int radius;

    /**
     * Constructeur de la bordure arrondie.
     *
     * @param radius Le rayon d'arrondi des coins.
     */
    public BordureArrondie(int radius) {
        this.radius = radius;
    }

    /**
     * Dessine la bordure arrondie autour du composant.
     * <p>
     * La méthode crée une instance de Graphics2D pour activer l'anti-aliasing et dessine un rectangle arrondi
     * avec le rayon spécifié.
     * </p>
     *
     * @param c      Le composant pour lequel la bordure est dessinée.
     * @param g      Le contexte graphique utilisé pour le dessin.
     * @param x      La coordonnée x de départ.
     * @param y      La coordonnée y de départ.
     * @param width  La largeur du composant.
     * @param height La hauteur du composant.
     */
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(c.getBackground());
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2.dispose();
    }

    /**
     * Retourne les marges de la bordure.
     *
     * @param c Le composant auquel la bordure est appliquée.
     * @return Un objet Insets représentant les marges de la bordure.
     */
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(4, 4, 4, 4);
    }

    /**
     * Indique si la bordure est opaque.
     *
     * @return true, car cette bordure est opaque.
     */
    @Override
    public boolean isBorderOpaque() {
        return true;
    }
}
