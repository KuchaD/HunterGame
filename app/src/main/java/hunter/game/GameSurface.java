package hunter.game;

/**
 * Created by Dave on 16.11.2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {


    private GameThread gameThread;

    private ChibiCharacter chibi1;
    private ArrayList<FireObject> fire = new ArrayList<FireObject>();
    private ArrayList<EnemyCharacter> Enemys = new ArrayList<EnemyCharacter>();
    private ArrayList<Explosion> explosionList = new ArrayList<Explosion>();
    private Level level1;
    private DBHandler db;

    //Joystick
    private float centerX = 0;
    private float centerY = 0;
    private float baseRadius = 0;
    private float hatRadius = 0;
    private float newX = 0;
    private float newY = 0;
    public boolean Joystick = false;

    //canvas
    private int HEIGHT;
    private int WIDTH;

    //LEVEL
    private int enemyNumber[] = {10,5,0,0,0};
    private int[] timer = {0,0,0,0,0};
    private boolean levelUP = false;
    private int level = 1;


    //SOUND
    private static final int MAX_STREAMS=100;
    private int soundIdArrow;
    private int soundIdKill;
    private int soundIdGameOver;
    private int soundIdBackground;
    private boolean soundPoolLoaded;
    private SoundPool soundPool;

    public GameSurface(Context context) {
        super(context);

        this.setFocusable(true);

        this.getHolder().addCallback(this);
        this.initSoundPool();
        db = new DBHandler(context);


        if(AppContect.image ==null)
            AppContect.image = BitmapFactory.decodeResource(getResources(), R.drawable.character);
    }

    private void initSoundPool()  {
        if (Build.VERSION.SDK_INT >= 21 ) {

            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder= new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

            this.soundPool = builder.build();
        }

        else {

            this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }


        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if(AppContect.Sound = true)
                    soundPoolLoaded = true;
                else
                    soundPoolLoaded=false;
                // Playing background sound.
                //playSoundBackground();
            }
        });


       // this.soundIdBackground= this.soundPool.load(this.getContext(), R.raw.arrow,1);


        this.soundIdArrow = this.soundPool.load(this.getContext(), R.raw.arrow,1);
        this.soundIdGameOver = this.soundPool.load(this.getContext(), R.raw.gameover,1);
        this.soundIdKill = this.soundPool.load(this.getContext(), R.raw.kill,1);


    }

    public void playSoundExplosion()  {
        if(this.soundPoolLoaded) {
            float leftVolumn = 0.8f;
            float rightVolumn =  0.8f;
            // Play sound explosion.wav
            int streamId = this.soundPool.play(this.soundIdArrow,leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }

    public void playSoundBackground()  {
        if(this.soundPoolLoaded) {
            float leftVolumn = 0.8f;
            float rightVolumn =  0.8f;
            // Play sound background.mp3
            int streamId = this.soundPool.play(this.soundIdBackground,leftVolumn, rightVolumn, 1, -1, 1f);
        }
    }
    public void playSoundGameOver()  {
        if(this.soundPoolLoaded) {
            float leftVolumn = 0.8f;
            float rightVolumn =  0.8f;
            // Play sound background.mp3
            int streamId = this.soundPool.play(this.soundIdGameOver,leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }
    public void playSoundKill()  {
        if(this.soundPoolLoaded) {
            float leftVolumn = 0.8f;
            float rightVolumn =  0.8f;
            // Play sound background.mp3
            int streamId = this.soundPool.play(this.soundIdKill,leftVolumn, rightVolumn, 1, 0, 1f);
        }
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

        ArrayList<Explosion> removeExpl = new ArrayList<Explosion>();


        for (Explosion ex:explosionList) {

            ex.update();

            if (ex.isFinish()) {
                removeExpl.add(ex);
            }
        }
        explosionList.removeAll(removeExpl);




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
            db.addScore((int)this.chibi1.getScore());


        }

        for(Explosion explosion: explosionList)  {
            explosion.draw(canvas);
        }

        if(levelUP){
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(100);
            canvas.drawText("LEVEL UP "+level, canvas.getWidth()/3, canvas.getHeight()/2, paint);
            try {
                this.gameThread.join(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            levelUP = false;


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
        Bitmap chibiBitmap1 = AppContect.image;
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

        if (enemyNumber[0] >= 0 && timer[0] == 0) {
            Random rand = new Random();

            int randomNum = rand.nextInt((HEIGHT - 0) + 1) + 0;
            Enemys.add(new EnemyCharacter(this, BitmapFactory.decodeResource(this.getResources(), R.drawable.monster3), WIDTH, randomNum,1));

            enemyNumber[0]--;

        }

        if (timer[0] == 0) {
            int random = rando.nextInt((200/level - 2) + 1) + 0;
            timer[0] = random;
        }
        timer[0]--;

        if (enemyNumber[1] >= 0 && timer[1] == 0) {
            Random rand = new Random();

            int randomNum = rand.nextInt((HEIGHT - 0) + 1) + 0;
            Enemys.add(new EnemyCharacter(this, BitmapFactory.decodeResource(this.getResources(), R.drawable.monster14), WIDTH, randomNum,2));

            enemyNumber[1]--;

        }

        if (timer[1] == 0) {
            rando = new Random();
            int random = rando.nextInt((500/level - 2) + 1) + 0;
            timer[1] = random;
        }
        timer[1]--;



        if(enemyNumber[0] == 0 && enemyNumber[1] == 0){
            levelUP = true;
            EnemyCharacter.VELOCITY *= 1.5;
            enemyNumber[0] = (int)(10 * level);
            enemyNumber[1] = (int)(5 * level);
            level++;


        }


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

                            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.boom);
                            Explosion explosion = new Explosion(this, bitmap,e.getX(),e.getY());

                            this.explosionList.add(explosion);
                        }
                        removeF.add(f);

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

        canvas.drawText("GAME OVER", canvas.getWidth()/2, canvas.getHeight()/2, paint);
        playSoundGameOver();


    }

    public void pause() {
        gameThread.setRunning(false);
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }


    public void resume() {
        gameThread.setRunning(true);
        this.gameThread = new GameThread(this,getHolder());
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }





}