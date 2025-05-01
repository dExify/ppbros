package inf112.ppbros.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;

/**
 * Utility class for performing operations on {@link Texture} objects.
 * 
 * <p>This class provides methods such as resizing a texture using Pixmaps.
 */
public class TextureUtils {
  
  private TextureUtils() {}
  
  /**
   * Resizes a given {@link Texture} to the specified width and height.
   * 
   * @param originalTexture The original texture to resize.
   * @param width           The desired width of the new texture.
   * @param height          The desired height of the new texture.
   * @return A new {@link Texture} object with the specified dimensions.
   */
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

