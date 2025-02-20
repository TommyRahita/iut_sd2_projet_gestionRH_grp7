package JavaProject;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Classe permettant de dessiner une bordure arrondie pour les boutons.
 */
class BordureArrondie implements Border {
    private int radius;

    /**
     * Constructeur de la bordure arrondie.
     * @param radius Rayon d'arrondi des coins.
     */
    public BordureArrondie(int radius) {
        this.radius = radius;
    }

    @Override
/**
 * Méthode paintBorder.
 * Description de la méthode.
 * @param c Description du paramètre.
 * @param g Description du paramètre.
 * @param x Description du paramètre.
 * @param y Description du paramètre.
 * @param width Description du paramètre.
 * @param height Description du paramètre.
 */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(c.getBackground());
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2.dispose();
    }

    @Override
/**
 * Méthode getBorderInsets.
 * Description de la méthode.
 * @param c Description du paramètre.
 * @return Insets Description du retour.
 */
    public Insets getBorderInsets(Component c) {
        return new Insets(4, 4, 4, 4);
    }

    @Override
/**
 * Méthode isBorderOpaque.
 * Description de la méthode.
 * @return boolean Description du retour.
 */
    public boolean isBorderOpaque() {
        return true;
    }
}
