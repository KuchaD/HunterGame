package hunter.game;

/**
 * Created by Dave on 16.11.2017.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ChibiCharacter extends GameObject {

    private static final int ROW_TOP_TO_BOTTOM = 0;
    private static final int ROW_RIGHT_TO_LEFT = 2;
    private static final int ROW_LEFT_TO_RIGHT = 3;
    private static final int ROW_BOTTOM_TO_TOP = 1;
    private static final int maxLife = 5;
    // Row index of Image are being used.
    private int rowUsing = ROW_LEFT_TO_RIGHT;

    private int colUsing;
    private double life = 5.0;
    private double score = 0.0;

    private Bitmap[] leftToRights;
    private Bitmap[] rightToLefts;
    private Bitmap[] topToBottoms;
    private Bitmap[] bottomToTops;
    private Bitmap[] heart = new Bitmap[2];
    private Bitmap weapon;
    private Bitmap coin;
    private String ammo = "∞";
    private int typeFire = 1;
    private int ammoCount = -66;
    // Velocity of game character (pixel/millisecond)
    public static final float VELOCITY = 0.5f;

    private int movingVectorX = 10;
    private int movingVectorY = 5;

    private long lastDrawNanoTime =-1;

    private GameSurface gameSurface;

    public ChibiCharacter(GameSurface gameSurface, Bitmap image, int x, int y) {
        super(image, 7,4, x, y);

        this.gameSurface= gameSurface;



        this.topToBottoms = new Bitmap[rowCount-3]; // 3
        this.rightToLefts = new Bitmap[rowCount-3]; // 3
        this.leftToRights = new Bitmap[rowCount-3]; // 3
        this.bottomToTops = new Bitmap[rowCount-3]; // 3

        for(int col = 0; col< this.rowCount-3; col++ ) {
            this.topToBottoms[col] = this.createSubImageAt(col,ROW_TOP_TO_BOTTOM);
            this.rightToLefts[col]  = this.createSubImageAt(col,ROW_RIGHT_TO_LEFT);
            this.leftToRights[col] = this.createSubImageAt(col,ROW_LEFT_TO_RIGHT);
            this.bottomToTops[col]  = this.createSubImageAt(col,ROW_BOTTOM_TO_TOP);
        }

        Bitmap heartImage = BitmapFactory.decodeResource(gameSurface.getResources(),R.drawable.heart);
        heart[0] = createSubImageAt(heartImage,0,0,1,5);
        heart[1]= createSubImageAt(heartImage,0,4,1,5);
        weapon = BitmapFactory.decodeResource(gameSurface.getResources(),R.drawable.arrow);
        coin = BitmapFactory.decodeResource(gameSurface.getResources(),R.drawable.gold);
    }

    public Bitmap[] getMoveBitmaps()  {
        switch (rowUsing)  {
            case ROW_BOTTOM_TO_TOP:
                return  this.bottomToTops;
            case ROW_LEFT_TO_RIGHT:
                return this.leftToRights;
            case ROW_RIGHT_TO_LEFT:
                return this.rightToLefts;
            case ROW_TOP_TO_BOTTOM:
                return this.topToBottoms;
            default:
                return null;
        }
    }

    public Bitmap getCurrentMoveBitmap()  {
        Bitmap[] bitmaps = this.getMoveBitmaps();
        return bitmaps[this.colUsing];
    }


    public void update()  {
        this.colUsing++;
        if(colUsing >= this.rowCount-3)  {
            this.colUsing =0;
        }
        // Current time in nanoseconds
        long now = System.nanoTime();

        // Never once did draw.
        if(lastDrawNanoTime==-1) {
            lastDrawNanoTime= now;
        }
        // Change nanoseconds to milliseconds (1 nanosecond = 1000000 milliseconds).
        int deltaTime = (int) ((now - lastDrawNanoTime)/ 1000000 );

        // Distance moves
        float distance = VELOCITY * deltaTime;

        double movingVectorLength = Math.sqrt(movingVectorX* movingVectorX + movingVectorY*movingVectorY);

        // Calculate the new position of the game character.
        this.x = x +  (int)(distance* movingVectorX / movingVectorLength);
        this.y = y +  (int)(distance* movingVectorY / movingVectorLength);

        // When the game's character touches the edge of the screen, then change direction

        if(this.x < 0 )  {
            this.x = 0;
            this.movingVectorX = - this.movingVectorX;
        } else if(this.x > this.gameSurface.getWidth() -width)  {
            this.x= this.gameSurface.getWidth()-width;
            this.movingVectorX = - this.movingVectorX;
        }

        if(this.y < 0 )  {
            this.y = 0;
            this.movingVectorY = - this.movingVectorY;
        } else if(this.y > this.gameSurface.getHeight()- height)  {
            this.y= this.gameSurface.getHeight()- height;
            this.movingVectorY = - this.movingVectorY ;
        }

        // rowUsing
        if( movingVectorX > 0 )  {
            if(movingVectorY > 0 && Math.abs(movingVectorX) < Math.abs(movingVectorY)) {
                this.rowUsing = ROW_TOP_TO_BOTTOM;
            }else if(movingVectorY < 0 && Math.abs(movingVectorX) < Math.abs(movingVectorY)) {
                this.rowUsing = ROW_BOTTOM_TO_TOP;
            }else  {
                this.rowUsing = ROW_LEFT_TO_RIGHT;
            }
        } else {
            if(movingVectorY > 0 && Math.abs(movingVectorX) < Math.abs(movingVectorY)) {
                this.rowUsing = ROW_TOP_TO_BOTTOM;
            }else if(movingVectorY < 0 && Math.abs(movingVectorX) < Math.abs(movingVectorY)) {
                this.rowUsing = ROW_BOTTOM_TO_TOP;
            }else  {
                this.rowUsing = ROW_RIGHT_TO_LEFT;
            }
        }
    }
    public void draw(Canvas canvas)  {
        Bitmap bitmap = this.getCurrentMoveBitmap();
        canvas.drawBitmap(bitmap,x, y, null);
        canvas.drawBitmap(weapon,canvas.getWidth()-290,20, null);
        canvas.drawBitmap(coin,canvas.getWidth()-500, 15, null);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);

        canvas.drawText(ammo,canvas.getWidth()-250,40,paint);
        paint.setTextSize(40);
        canvas.drawText(String.valueOf(score),canvas.getWidth()-450,40,paint);

        for (int i = 1;i <= maxLife;i++){
            if(life < i)
                canvas.drawBitmap(heart[1],canvas.getWidth()-210+(i*32),0,null);
            else
                canvas.drawBitmap(heart[0],canvas.getWidth()-210+(i*32),0,null);
        }


        // Last draw time.
        this.lastDrawNanoTime= System.nanoTime();
    }

    public void setMovingVector(int movingVectorX, int movingVectorY)  {
        this.movingVectorX= movingVectorX;
        this.movingVectorY = movingVectorY;
    }

    public boolean hit(){
        life --;

        if(life <= 0)
            return  false;

        return true;

    }

    public void LifePlus(){
        if(life < 5)
            life++;
    }

    public boolean shoot(){
        if(ammoCount == -66)
            return true;
        if(ammoCount == 0)
            return false;
        if(ammoCount > 0){
            ammoCount--;
            ammo = String.valueOf(ammoCount);

        }

        return false;
    }
    public int getTypeFire(){
        return typeFire;
    }

    public double getLife(){return life;}

    public double getScore(){return score;};
    public void addScore(double value){
        score += value;
    }

}