package hunter.game;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.Serializable;


public class MainActivity extends Activity {

    GameSurface sur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set No Title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        sur = new GameSurface(this);
        this.setContentView(sur);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
    }

    @Override
    public void onBackPressed() {
        sur.pause();
        Intent Intent = new Intent(getApplicationContext(), menu.class);
        Intent.putExtra("surface", (Serializable) sur);
        startActivity(Intent);

    }
    @Override
    public void onPause(){
        super.onPause();
        sur.pause();

    }



}