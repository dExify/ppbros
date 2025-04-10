package inf112.ppbros.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;

public class TextureUtils {

    private TextureUtils() {}

    public static Texture resizeTexture(Texture originalTexture, int width, int height) {
        // Make sure the texture is prepared (fully loaded into memory)
        originalTexture.getTextureData().prepare();  // Prepare the texture data
        
        // Get the original texture's Pixmap
        Pixmap originalPixmap = originalTexture.getTextureData().consumePixmap();

        // Create a new Pixmap with the desired size
        Pixmap resizedPixmap = new Pixmap(width, height, Format.RGBA8888);

        // Resize the original Pixmap into the new Pixmap
        resizedPixmap.drawPixmap(originalPixmap, 0, 0, originalPixmap.getWidth(), originalPixmap.getHeight(),
                0, 0, width, height);

        // Create a new texture from the resized Pixmap
        Texture resizedTexture = new Texture(resizedPixmap);

        // Dispose the Pixmaps to free memory
        originalPixmap.dispose();
        resizedPixmap.dispose();

        return resizedTexture;
    }
}

