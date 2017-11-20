package hunter.game;

/**
 * Created by Dave on 16.11.2017.
 */

import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
        import android.view.SurfaceHolder;
        import android.view.SurfaceView;




public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {


    private GameThread gameThread;

    private ChibiCharacter chibi1;
    private Bitmap scaled;
    private Level level1;

    private float centerX = 0;
    private float centerY = 0;
    private float baseRadius = 0;
    private float hatRadius = 0;
    private float newX = 0;
    private float newY = 0;
    public boolean Joystick = false;

    public GameSurface(Context context)  {
        super(context);

        // Make Game Surface focusable so it can handle events. .
        this.setFocusable(true);

        // Sét callback.
        this.getHolder().addCallback(this);
    }

    public void update()  {

        if(Joystick )
        this.chibi1.update();
    }

    private void initJoystic(){
        Canvas canvas = this.getHolder().lockCanvas();
        centerX = canvas.getWidth() / 10;
        centerY = canvas.getHeight() - (canvas.getHeight() / 7 );
        baseRadius = canvas.getHeight()/ 10;
        hatRadius = canvas.getHeight() / 20    ;

        newX = centerX;
        newY = centerY;
        getHolder().unlockCanvasAndPost(canvas);
    }

    private void newPositonJoy(float X,float Y){
        newX = X;
        newY = Y;
    }

    @Override
    public void draw(Canvas canvas)  {
        super.draw(canvas);

        this.level1.draw(canvas);
        drawJoystick(canvas);
        this.chibi1.draw(canvas);


    }

    private void drawJoystick(Canvas canvas){

            Paint colors = new Paint();
            canvas.drawColor(Color.WHITE);
            canvas.drawCircle(centerX,centerY,baseRadius,new Paint(Color.RED));
            colors.setColor(Color.YELLOW);
            canvas.drawCircle(newX,newY,hatRadius,colors);

    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap chibiBitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.chibi1);
        this.chibi1 = new ChibiCharacter(this,chibiBitmap1,100,50);
        initJoystic();
        this.level1 = new Level(this);



        this.gameThread = new GameThread(this,holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry= true;
        while(retry) {
            try {
                this.gameThread.setRunning(false);

                // Parent thread must wait until the end of GameThread.
                this.gameThread.join();
            }catch(InterruptedException e)  {
                e.printStackTrace();
            }
            retry= true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() != MotionEvent.ACTION_UP){
        double displacement = Math.sqrt(Math.pow(event.getX() - centerX,2) + Math.pow(event.getY() - centerY,2));
            if(displacement < baseRadius + 50){
                newPositonJoy(event.getX(),event.getY());

                int x=  (int)event.getX();
                int y = (int)event.getY();

                int movingVectorX = (int) (x- centerX);
                int movingVectorY = (int) (y-  centerY);

                Joystick = true;
                this.chibi1.setMovingVector(movingVectorX,movingVectorY);
            }else
            {
                newPositonJoy(centerX,centerY);
                Joystick = false;
            }

            return true;
        }else{
            newPositonJoy(centerX,centerY);
            Joystick = false;
        }
        return false;
    }

}