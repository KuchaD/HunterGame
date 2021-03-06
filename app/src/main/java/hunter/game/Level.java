package hunter.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.view.View;

/**
 * Created by Dave on 20.11.2017.
 */

public class Level {

    private Bitmap grass;
    private Bitmap tree ;
    private Bitmap tree2 ;
    private Bitmap house ;
    private Bitmap root ;
    private Bitmap bg;

    private Bitmap image;
    private GameSurface gameSurface;
    public Level(GameSurface gameSurface,Canvas canvas) {


        this.gameSurface= gameSurface;

          grass = BitmapFactory.decodeResource(gameSurface.getResources(),R.drawable.grass);
          tree = BitmapFactory.decodeResource(gameSurface.getResources(),R.drawable.tree);
          tree2 = BitmapFactory.decodeResource(gameSurface.getResources(),R.drawable.tree2);
          house = BitmapFactory.decodeResource(gameSurface.getResources(),R.drawable.house);
          root = BitmapFactory.decodeResource(gameSurface.getResources(),R.drawable.root);

        int width = canvas.getWidth();
        int height = canvas.getHeight();
        mydraw(width,height);

    }

    public void mydraw(int w,int h)  {

        bg = Bitmap.createBitmap( (int)w, (int)h, Bitmap.Config.RGB_565 );
        Canvas canvas = new Canvas(bg);


        int width = canvas.getWidth();
        int height = canvas.getHeight();

        int heightImage = grass.getHeight();
        int widthImage = grass.getWidth();



        for(int x =0; x < width; x = x+widthImage){
            for (int y = 0;y < height; y+= heightImage){
                canvas.drawBitmap(grass,x, y, null);
            }
        }

        heightImage = tree.getHeight();
        widthImage = tree.getWidth();

        int nItem = 0;
        for (int y = 0; y < height; y += heightImage - 20) {

            if(nItem %3 == 0)
                canvas.drawBitmap(tree2,widthImage-50, y-(heightImage/2), null);
            if(nItem %2 == 0)
                canvas.drawBitmap(tree,widthImage/2, y, null);

            canvas.drawBitmap(tree, 0, y, null);
            nItem++;
        }

        canvas.drawBitmap(house,house.getWidth(),0, null);





    }

    public void draw(Canvas canvas){

        canvas.drawBitmap(bg,0,0,null);
    }



}
