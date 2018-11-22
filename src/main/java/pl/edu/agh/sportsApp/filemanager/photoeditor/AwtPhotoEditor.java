package pl.edu.agh.sportsApp.filemanager.photoeditor;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;

@Primary
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AwtPhotoEditor implements PhotoEditor {

    int MAX_IMG_WIDTH = 250;
    int MAX_IMG_HEIGHT = 250;

    public BufferedImage resize(BufferedImage originalImage) {
        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        if(width <= MAX_IMG_WIDTH && height <= MAX_IMG_HEIGHT)
            return originalImage;

        ImageSize newSize = calculateNewSize(width, height);
        BufferedImage resizedImage = new BufferedImage(newSize.getWidth(), newSize.getHeight(), type);
        Graphics2D g = resizedImage.createGraphics();

        g.drawImage(originalImage, 0, 0, newSize.getWidth(), newSize.getHeight(), null);
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.dispose();

        return resizedImage;
    }

    private ImageSize calculateNewSize(int width, int height) {
        float rw = width / (float) MAX_IMG_WIDTH;
        float rh = height / (float) MAX_IMG_HEIGHT;

        int newHeight, newWidth;

        if(rw > rh) {
            newWidth = MAX_IMG_WIDTH;
            newHeight = Math.round(height / rw);
        }
        else {
            newWidth = Math.round(width / rh);
            newHeight = MAX_IMG_HEIGHT;
        }

        return new ImageSize(newWidth, newHeight);
    }

}
