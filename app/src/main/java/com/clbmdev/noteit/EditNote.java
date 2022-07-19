package com.clbmdev.noteit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;


public class EditNote extends AppCompatActivity {

    EditText etTitle, etNote;
    Button btnSave;
    RelativeLayout myBackground;

    int color;
    int backColor;
    boolean addNote;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        etTitle = findViewById(R.id.etTitle);
        etNote = findViewById(R.id.etNote);
        btnSave = findViewById(R.id.btnSave);
        myBackground = findViewById(R.id.myBackground);
        color = ContextCompat.getColor(getApplicationContext(), R.color.note_blue);
        backColor = ContextCompat.getColor(getApplicationContext(), R.color.note_blue_light);

        actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setTitle("");

        addNote = getIntent().getBooleanExtra("addNote", false);

        if(addNote)
        {
            changeColor(R.color.note_blue, R.color.note_blue_light);
        }
        else
        {
            String title = getIntent().getStringExtra("title");
            String note = getIntent().getStringExtra("note");
            color = getIntent().getIntExtra("color",
                    ContextCompat.getColor(getApplicationContext(), R.color.note_blue)
            );

            backColor = getIntent().getIntExtra("backColor",
                    ContextCompat.getColor(getApplicationContext(), R.color.note_blue_light)
            );

            etTitle.setText(title);

            etNote.setText(note);

            etTitle.setBackgroundColor(color);

            etNote.setBackgroundColor(color);

            btnSave.setBackgroundColor(color);

            actionBar.setBackgroundDrawable(new ColorDrawable(color));

            myBackground.setBackgroundColor(backColor);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                Window w = getWindow();
                w.setStatusBarColor(backColor);
            }


        }

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String note = etNote.getText().toString().trim();

            Intent intent = new Intent();
            intent.putExtra("addNote", addNote);
            intent.putExtra("title", title);
            intent.putExtra("note", note);
            intent.putExtra("color", color);
            intent.putExtra("backColor", backColor);

            setResult(RESULT_OK, intent);

            EditNote.this.finish();
        });
    }

    /**
     * Change the main color and background color of the note page
     * @param colorID int: the main color of the note
     * @param backgroundColorID int: the background color of the note
     */
    void changeColor(int colorID, int backgroundColorID)
    {
        color = ContextCompat.getColor(getApplicationContext(), colorID);

        backColor = ContextCompat.getColor(getApplicationContext(), backgroundColorID);

        etTitle.setBackgroundColor(color);

        etNote.setBackgroundColor(color);

        btnSave.setBackgroundColor(color);

        actionBar.setBackgroundDrawable(new ColorDrawable(color));

        myBackground.setBackgroundColor(backColor);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window w = getWindow();
            w.setStatusBarColor(backColor);
        }
    } // changeColor

    public void colorToBlue(View v)
    {
        changeColor(R.color.note_blue, R.color.note_blue_light);
    } // colorToBlue

    public void colorToPink(View v)
    {
        changeColor(R.color.note_pink, R.color.note_pink_light);
    } // colorToPink

    public void colorToYellow(View v)
    {
        changeColor(R.color.note_yellow, R.color.note_yellow_light);
    } // colorToYellow

    public void colorToDeepPurple(View v)
    {
        changeColor(R.color.note_deep_purple, R.color.note_deep_purple_light);
    } // colorToDeepPurple

    public void colorToGreen(View v)
    {
        changeColor(R.color.note_green, R.color.note_green_light);
    } // colorToGreen

    public void colorToDeepOrange(View v)
    {
        changeColor(R.color.note_deep_orange, R.color.note_deep_orange_light);
    } // colorToDeepOrange

    public void colorToBlueGrey(View v)
    {
        changeColor(R.color.note_blue_grey, R.color.note_blue_grey_light);
    } // colorToBlueGrey

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.note_details, menu);

        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.delete)
        {

            Intent intent = new Intent();
            intent.putExtra("delete", true);
            intent.putExtra("addNote", addNote);
            setResult(RESULT_OK, intent);
            EditNote.this.finish();
        }

        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
}