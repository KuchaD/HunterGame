package hunter.game;

import android.graphics.Bitmap;

/**
 * Created by Dave on 20.11.2017.
 */

public class FireObject extends GameObject {

    public FireObject(GameSurface gameSurface, Bitmap image, int x, int y) {
        super(image, 4, 3, x, y);
    }
}
