package hunter.game;

import android.app.Activity;
import android.database.Cursor;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ScoreList extends Activity {


    ListView lv;
    Button btnSave;
    private DBHandler db;
    ArrayList<String> listData;
    ArrayList<String> listDataToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_list);
        db = new DBHandler(this);

        lv = (ListView) findViewById(R.id.scoreList);
        btnSave = (Button) findViewById(R.id.btnSave);



        Cursor data = db.getData();
        listData = new ArrayList<>();
        listDataToShow = new ArrayList<>();
        while (data.moveToNext()) {

            listData.add(data.getString(1));
            listData.add(data.getString(2));
            listData.add(data.getString(3));
        }


        for(int i =0; i<listData.size();i=i+3){
            listDataToShow.add(listData.get(i+1) + " - "+listData.get(i)+" / " + listData.get(i+2));
        }

        //create the list adapter and set the adapter
        final ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listDataToShow);
        lv.setAdapter(adapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    File myFile = new File(Environment.getExternalStorageDirectory()+"/HightScore.txt");

                    if (myFile.exists()){
                        myFile.delete();
                    }
                    myFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

                    for (String data: listDataToShow) {
                        myOutWriter.write(data);
                        myOutWriter.write("\n");
                    }
                    myOutWriter.close();
                    fOut.close();

                    Toast.makeText(getApplicationContext(), "Uloženo!",
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


}
