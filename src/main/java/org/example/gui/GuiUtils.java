package org.example.gui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class GuiUtils {
    private static BufferedImage tintImage(BufferedImage original, Color tint) {
        BufferedImage tintedImage = new BufferedImage(
                original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = tintedImage.createGraphics();

        g2d.drawImage(original, 0, 0, null);
        g2d.setComposite(AlphaComposite.SrcAtop);
        g2d.setColor(tint);
        g2d.fillRect(0, 0, original.getWidth(), original.getHeight());

        g2d.dispose();
        return tintedImage;
    }

    public static BufferedImage getIconImage(URL iconUrl) {
        try {
            BufferedImage originalIcon = ImageIO.read(iconUrl);
            return tintImage(originalIcon, new Color(0, 80, 255, 120));
        } catch (IOException e) {
            System.err.println("Failed to load icon image: " + e.getMessage());
        }
        return null;
    }
}
