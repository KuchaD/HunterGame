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

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {


    private GameThread gameThread;

    private ChibiCharacter chibi1;
    private ArrayList<FireObject> fire = new ArrayList<FireObject>();
    private ArrayList<EnemyCharacter> Enemys = new ArrayList<EnemyCharacter>();
    private Bitmap scaled;
    private Level level1;

    private int enemyNumber = 30;
    private float centerX = 0;
    private float centerY = 0;
    private float baseRadius = 0;
    private float hatRadius = 0;
    private float newX = 0;
    private float newY = 0;
    public boolean Joystick = false;
    private int HEIGHT;
    private int WIDTH;
    private int timer = 0;

    public GameSurface(Context context) {
        super(context);

        // Make Game Surface focusable so it can handle events. .
        this.setFocusable(true);

        // Sét callback.
        this.getHolder().addCallback(this);

    }

    public void update() {

        UI();
        if (Joystick)
            this.chibi1.update();


        ArrayList<FireObject> removeF = new ArrayList<FireObject>();
        ArrayList<EnemyCharacter> removeE = new ArrayList<EnemyCharacter>();

        for (FireObject item : fire) {
            item.update();
            if (item.getX() > WIDTH || item.getX() < 0)
                removeF.add(item);
            if (item.getY() > HEIGHT || item.getY() < 0)
                removeF.add(item);
        }

        for (EnemyCharacter item : Enemys) {
            item.update();
            if (item.getX() > WIDTH || item.getX() < 0) {
                removeE.add(item);
                this.chibi1.hit();
            }
            if (item.getY() > HEIGHT || item.getY() < 0) {
                removeE.add(item);
                this.chibi1.hit();
            }


        }

        fire.removeAll(removeF);
        Enemys.removeAll(removeE);
        Colison();
    }

    private void initJoystic() {
        Canvas canvas = this.getHolder().lockCanvas();
        centerX = canvas.getWidth() / 10;
        centerY = canvas.getHeight() - (canvas.getHeight() / 7);
        baseRadius = canvas.getHeight() / 10;
        hatRadius = canvas.getHeight() / 20;

        newX = centerX;
        newY = centerY;

        HEIGHT = canvas.getHeight();
        WIDTH = canvas.getWidth();

        getHolder().unlockCanvasAndPost(canvas);
    }

    private void newPositonJoy(float X, float Y) {
        newX = X;
        newY = Y;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (this.level1 == null)
            this.level1 = new Level(this, canvas);
        else
            this.level1.draw(canvas);

        //this.level1.draw(canvas);
        drawJoystick(canvas);
        this.chibi1.draw(canvas);
        for (FireObject item : fire) {
            item.draw(canvas);
        }

        for (EnemyCharacter item : Enemys) {
            item.draw(canvas);
        }
        if (this.chibi1.getLife() <= 0) {
            this.gameThread.setRunning(false);
            DrawGameOver(canvas);
        }

    }

    private void drawJoystick(Canvas canvas) {

        Paint colors = new Paint();
        canvas.drawCircle(centerX, centerY, baseRadius, new Paint(Color.RED));
        colors.setColor(Color.YELLOW);
        canvas.drawCircle(newX, newY, hatRadius, colors);

    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap chibiBitmap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.character);
        this.chibi1 = new ChibiCharacter(this, chibiBitmap1, 100, 50);
        initJoystic();


        this.gameThread = new GameThread(this, holder);
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
        boolean retry = true;
        while (retry) {
            try {
                this.gameThread.setRunning(false);

                // Parent thread must wait until the end of GameThread.
                this.gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        double displacement = Math.sqrt(Math.pow(event.getX() - centerX, 2) + Math.pow(event.getY() - centerY, 2));
        if (event.getAction() != MotionEvent.ACTION_UP) {
            if (displacement < baseRadius + 50) {
                newPositonJoy(event.getX(), event.getY());

                int x = (int) event.getX();
                int y = (int) event.getY();

                int movingVectorX = (int) (x - centerX);
                int movingVectorY = (int) (y - centerY);

                Joystick = true;
                this.chibi1.setMovingVector(movingVectorX, movingVectorY);
            } else {
                newPositonJoy(centerX, centerY);
                Joystick = false;
            }

            return true;
        } else {
            newPositonJoy(centerX, centerY);
            Joystick = false;
            if (displacement > baseRadius + 50) {

                float moX = event.getX() - this.chibi1.getX();
                float moY = event.getY() - this.chibi1.getY();

                if (chibi1.shoot()) {
                    fire.add(new FireObject(this, chibi1.getTypeFire(), this.chibi1.getX(), this.chibi1.getY(), moX, moY));
                }
            }
        }
        return false;
    }

    public void UI() {
        Random rando = new Random();

        if (enemyNumber >= 0 && timer == 0) {
            Random rand = new Random();

            int randomNum = rand.nextInt((HEIGHT - 0) + 1) + 0;
            Enemys.add(new EnemyCharacter(this, BitmapFactory.decodeResource(this.getResources(), R.drawable.monster3), WIDTH, randomNum,1));

            enemyNumber--;

        }

        if (timer == 0) {
            int random = rando.nextInt((50 - 2) + 1) + 0;
            timer = random;
        }
        timer--;
    }

    public void Colison() {
        ArrayList<FireObject> removeF = new ArrayList<FireObject>();
        ArrayList<EnemyCharacter> removeE = new ArrayList<EnemyCharacter>();

        for (EnemyCharacter e : Enemys) {
            for (FireObject f : fire) {
                if (((f.getY() - e.getY()) < 40) && ((f.getY() - e.getY()) > -40)) {
                    if (((f.getX() - e.getX()) < 40) && ((f.getX() - e.getX()) > -40)) {
                        if(!(e.hitLife(f.getDemage()))){
                            this.chibi1.addScore(e.getScoreValue());
                            removeE.add(e);
                            removeF.add(f);


                        }


                    }

                }
            }
        }

        Enemys.removeAll(removeE);
        fire.removeAll(removeF);

    }

    public void DrawGameOver(Canvas canvas) {


        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(100);

        canvas.drawText("GAME OVER", canvas.getWidth()/5, canvas.getHeight()/2, paint);
    }
}