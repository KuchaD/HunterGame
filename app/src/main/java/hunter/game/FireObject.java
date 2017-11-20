package hunter.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;

import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by Dave on 20.11.2017.
 */

public class FireObject extends GameObject {

    private Bitmap Fire;
    public float VELOCITY = 0.5f;

    private int movingVectorX ;
    private int movingVectorY ;

    private long lastDrawNanoTime =-1;
    private GameSurface gameSurface;
    private float angle;
    public FireObject(GameSurface gameSurface,int type,int x,int y,float moveX,float moveY) {
        super(BitmapFactory.decodeResource(gameSurface.getResources(),R.drawable.arrow), 1, 1, x, y);
        this.gameSurface = gameSurface;

        movingVectorX = (int) moveX;
        movingVectorY = (int )moveY;

        float moX = moveX-x;
        float moY = moveY-y;

        angle = (float) calculateAngle(x,y,movingVectorX,movingVectorY);


        switch (type){
            case 1:
                Fire = BitmapFactory.decodeResource(gameSurface.getResources(),R.drawable.arrow);
                VELOCITY = 0.1f;
            default:
                Fire = image;
        }

    }

    public void update()  {


        long now = System.nanoTime();


        if(lastDrawNanoTime==-1) {
            lastDrawNanoTime= now;
        }

        int deltaTime = (int) ((now - lastDrawNanoTime)/ 1000000 );
        float distance = VELOCITY * deltaTime;
        double movingVectorLength = Math.sqrt(movingVectorX* movingVectorX + movingVectorY*movingVectorY);

        this.x = x +  (int)(distance* movingVectorX / movingVectorLength);
        this.y = y +  (int)(distance* movingVectorY / movingVectorLength);

    }
    public void draw(Canvas canvas)  {



        Bitmap bitmap = RotateBitmap(Fire,angle);
        canvas.drawBitmap(bitmap,x, y, null);

    }


    public Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0,0 , source.getWidth(), source.getHeight(), matrix, true);
    }

    public static double calculateAngle(double x1, double y1, double x2, double y2)
    {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        // Keep angle between 0 and 360
        angle = angle + Math.ceil( -angle / 360 ) * 360;

        return angle;
    }

}
