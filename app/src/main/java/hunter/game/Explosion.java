package hunter.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Dave on 06.12.2017.
 */

public class Explosion extends GameObject {

    private int rowIndex = 0 ;
    private int colIndex = -1 ;

    private boolean finish= false;
    private GameSurface gameSurface;

    public Explosion(GameSurface GameSurface, Bitmap image, int x, int y) {
        super(image, 1, 6, x, y);

        this.gameSurface= GameSurface;
        this.gameSurface.playSoundKill();

    }

    public void update()  {
        this.colIndex++;
            if(this.colIndex >= 5)  {
                this.finish= true;

            }

    }

    public void draw(Canvas canvas)  {
        if(!finish)  {
            Bitmap bitmap= this.createSubImageAt(rowIndex,colIndex);
            canvas.drawBitmap(bitmap, this.x, this.y,null);
        }
    }

    public boolean isFinish() {
        return finish;
    }

}