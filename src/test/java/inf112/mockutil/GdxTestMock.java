package inf112.mockutil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;

// Added separate class to mock gdx to fix error when running mocks concurrently
public class GdxTestMock {

    private static MockedStatic<Gdx> gdxMock;

    public static void init() {
        if (gdxMock == null) {  // only mock once
            gdxMock = mockStatic(Gdx.class);
            Graphics graphicsMock = mock(Graphics.class);
            Gdx.graphics = graphicsMock;

            when(Gdx.graphics.getWidth()).thenReturn(1920);
            when(Gdx.graphics.getHeight()).thenReturn(1080);
        }
    }
}
