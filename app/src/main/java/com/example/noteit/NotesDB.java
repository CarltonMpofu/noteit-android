package com.example.noteit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class NotesDB {

    // each field/column name needs to have an underscore
    private final String KEY_ROW_ID = "_id";
    private final String KEY_TITLE = "_title";
    private final String KEY_NOTE = "_note";
    private final String KEY_NOTE_COLOR = "note_color";
    private final String KEY_BACKGROUND_COLOR = "background_color";

    private final String DATABASE_NAME = "NotesDB";
    private final String DATABASE_TABLE = "NotesTable";
    private final int DATABASE_VERSION = 1;

    private SQLiteDatabase myDatabase;
    private final Context myContext;
    private DBHelper myHelper;

    public NotesDB(Context context)
    {
        myContext = context;
    }


    class DBHelper extends SQLiteOpenHelper
    {

        public DBHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        // Creates a database table if it does not exist
        {
            String sql = "CREATE TABLE " + DATABASE_TABLE + " (" +
                    KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_TITLE + " TEXT NOT NULL, " +
                    KEY_NOTE + " TEXT NOT NULL, " +
                    KEY_NOTE_COLOR + " INTEGER NOT NULL, " +
                    KEY_BACKGROUND_COLOR + " INTEGER NOT NULL);";

            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        // Creates a new database table when the database version is changed
        {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }

    } // DBHelper

    /**
     * creates a connection to the database
     * @return NotesDB the database
     * @throws SQLException
     */
    public NotesDB open() throws SQLException
    {
        myHelper = new DBHelper(myContext);
        myDatabase = myHelper.getWritableDatabase();
        return this;
    } // open

    /**
     * Stops the connection to the database
     */
    public void close()
    {
        myHelper.close();
    } // close

    /**
     * Makes and adds new note entries to the database
     * @param title String: the title of the Note
     * @param note String: the actual text of the Note
     * @param noteColor int: the main color of the Note
     * @param backgroundColor int: The background color of the Note
     * @return long: the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long createEntry(String title, String note, int noteColor, int backgroundColor)
    {
        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE, title);
        cv.put(KEY_NOTE, note);
        cv.put(KEY_NOTE_COLOR, noteColor);
        cv.put(KEY_BACKGROUND_COLOR, backgroundColor);

        return myDatabase.insert(DATABASE_TABLE, null,cv);
    } // createEntry

    /**
     * Delete the note with the given row id from the database
     * @param rowId String: the id of the row to delete
     * @return int:
     * the number of rows affected if a whereClause is passed in, 0 otherwise.
     */
    public long deleteEntry(String rowId)
    {
        return myDatabase.delete(DATABASE_TABLE, KEY_ROW_ID + "=?", new String[] {rowId});
    } // deleteEntry

    /**
     * Update or change the values of the Note at the given row id
     * @param rowId String: the id of the row to update
     * @param title String: the title of the Note
     * @param note String: the actual text of the Note
     * @param noteColor int: The main color of the Note
     * @param backgroundColor int: The background color of the Note
     * @return int:
     * the number of rows affected
     */
    public long updateEntry(String rowId, String title, String note, int noteColor, int backgroundColor)
    {
        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE, title);
        cv.put(KEY_NOTE, note);
        cv.put(KEY_NOTE_COLOR, noteColor);
        cv.put(KEY_BACKGROUND_COLOR, backgroundColor);

        return myDatabase.update(DATABASE_TABLE, cv, KEY_ROW_ID + "=?", new String[] {rowId});
    } // updateEntry

    /**
     * Get the list of notes in database
     * @return ArrayList the list of notes stored in the database
     */
    public ArrayList<Note> getData()
    {
        ArrayList<Note> result = new ArrayList<Note>();

        // Columns you want to access from the database
        String[] columns = new String[] {KEY_ROW_ID, KEY_TITLE, KEY_NOTE,
                KEY_NOTE_COLOR, KEY_BACKGROUND_COLOR};

        // Cursor that points at the rows in the database table
        Cursor cursor = myDatabase.query(DATABASE_TABLE, columns, null,
                null, null,null,null);

        // Get the index of each column in the table using the cursor
        int idIndex = cursor.getColumnIndex(KEY_ROW_ID);
        int titleIndex = cursor.getColumnIndex(KEY_TITLE);
        int noteIndex = cursor.getColumnIndex(KEY_NOTE);
        int noteColorIndex = cursor.getColumnIndex(KEY_NOTE_COLOR);
        int backgroundColorIndex = cursor.getColumnIndex(KEY_BACKGROUND_COLOR);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            // Use cursor to get data values in each row,
            // Create a Note object and add to the list of notes (result)
        {
//            result = result + cursor.getInt(idIndex) + ": " + cursor.getString(titleIndex) +
//                    " " + cursor.getString(noteIndex) + " " + cursor.getInt(noteColorIndex) +
//                    " " + cursor.getInt(backgroundColorIndex) + "\n";

            Note note = new Note(cursor.getString(noteIndex),
                    cursor.getInt(noteColorIndex), cursor.getInt(backgroundColorIndex));
            note.setTitle(cursor.getString(titleIndex));
            note.setID(cursor.getInt(idIndex));

            result.add(note);
        }

        // close the cursor
        cursor.close();

        return result;
    } // getData
} // NotesDB
