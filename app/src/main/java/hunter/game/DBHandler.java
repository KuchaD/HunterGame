package hunter.game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dave on 06.12.2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "game";
    private static final String TABLE_SCORE = "score";

    private static final String KEY_ID_SCORE = "_id";
    private static final String NAME = "name";
    private static final String KEY_SCORE = "score_value";
    private static final String TIME = "datetime";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCORE_TABLE = "CREATE TABLE " + TABLE_SCORE + "("
                + KEY_ID_SCORE + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME + " NUMBER, "
                + KEY_SCORE + " NUMBER, "
                + TIME + " TEXT"+ ")";

        db.execSQL(CREATE_SCORE_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);

        // Create tables again
        onCreate(db);
    }

    // Adding new score
    public void addScore(int score) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        String time = currentDateandTime;

        values.put(KEY_SCORE, score);
        values.put(TIME,time);// score value
        values.put(NAME,AppContect.name);// score value

        // Inserting Values
        db.insert(TABLE_SCORE, null, values);

        db.close();

    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_SCORE + " ORDER BY "+ KEY_SCORE+ " DESC";
        Cursor data = db.rawQuery(selectQuery, null);
        return data;
    }

}
