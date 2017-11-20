package hunter.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;

/**
 * Created by Dave on 20.11.2017.
 */

public class Level {

    private Bitmap grass;
    private Bitmap tree ;
    private Bitmap tree2 ;
    private Bitmap house ;
    private Bitmap root ;

    private Bitmap image;
    private GameSurface gameSurface;
    public Level(GameSurface gameSurface) {


        this.gameSurface= gameSurface;

          grass = BitmapFactory.decodeResource(gameSurface.getResources(),R.drawable.grass);
          tree = BitmapFactory.decodeResource(gameSurface.getResources(),R.drawable.tree);
          tree2 = BitmapFactory.decodeResource(gameSurface.getResources(),R.drawable.tree2);
          house = BitmapFactory.decodeResource(gameSurface.getResources(),R.drawable.house);
          root = BitmapFactory.decodeResource(gameSurface.getResources(),R.drawable.root);

    }

    public void draw(Canvas canvas)  {
        grass = BitmapFactory.decodeResource(gameSurface.getResources(),R.drawable.grass);
        //Bitmap grass = BitmapFactory.decodeResource(gameSurface.getContext().getResources(),R.drawable.grass);
        canvas.drawBitmap(grass,200,200,null);
        /*
        Bitmap newGrass = Bitmap.createScaledBitmap(image, 120, 120, false);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        int heightImage = grass.getHeight();
        int widthImage = grass.getWidth();



        for(int x =0; x < width-widthImage; x = x+widthImage){
            for (int y = 0;y < height-heightImage; y+= heightImage){
                canvas.drawBitmap(grass,x+(widthImage/2), y + (heightImage/2), null);
            }
        }
*/

    }
}
