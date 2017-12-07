package hunter.game;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;

import java.io.Serializable;

public class Setting extends Activity {


    RadioButton r1,r2,r3,r4,r5;
    Button btnSave;
    EditText editName;
    Switch swVolume;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        r1 = (RadioButton) findViewById(R.id.radioButton1);
        r2 = (RadioButton) findViewById(R.id.radioButton2);
        r3 = (RadioButton) findViewById(R.id.radioButton3);
        r4 = (RadioButton) findViewById(R.id.radioButton4);
        r5 = (RadioButton) findViewById(R.id.radioButton5);

        btnSave = (Button)findViewById(R.id.btnSaveChar);
        editName = (EditText)findViewById(R.id.editText2);
        swVolume = (Switch)findViewById(R.id.switch2);

        editName.setText(AppContect.name);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(r1.isChecked())
                    AppContect.image = BitmapFactory.decodeResource(getResources(), R.drawable.c1);
                if(r2.isChecked())
                    AppContect.image = BitmapFactory.decodeResource(getResources(), R.drawable.c2);
                if(r3.isChecked())
                    AppContect.image = BitmapFactory.decodeResource(getResources(), R.drawable.c3);
                if(r4.isChecked())
                    AppContect.image = BitmapFactory.decodeResource(getResources(), R.drawable.character);
                if(r5.isChecked())
                    AppContect.image = BitmapFactory.decodeResource(getResources(), R.drawable.c4);

                AppContect.name = editName.getText().toString();
                if(swVolume.isChecked())
                    AppContect.Sound =true;
                else
                    AppContect.Sound = false;

                finish();

            }
        });

    }
    @Override
    public void onBackPressed() {
        onBackPressed();

    }
}
