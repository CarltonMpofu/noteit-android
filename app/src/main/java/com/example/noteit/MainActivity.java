package com.example.noteit;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.SQLException;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NoteAdapter.ItemClicked {

    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    SearchView searchView;

    ArrayList<Note> notes;

    int selectedNoteIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.search);

        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        notes = new ArrayList<>();

        try
        { // get notes from database
            NotesDB db = new NotesDB(this);
            db.open();
            notes = db.getData();
            db.close();
        }
        catch (SQLException e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        myAdapter = new NoteAdapter(MainActivity.this, notes);

        recyclerView.setAdapter(myAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String title)
            // Finds a list of notes with the specified title
            {
                ArrayList<Note> notesFiltered = new ArrayList<>();
                if(title.length() == 0)
                    notesFiltered = notes;
                else
                {
                    for (Note note : notes)
                    {
                        if (note.getTitle().toLowerCase().startsWith(title.toLowerCase())) {
                            notesFiltered.add(note);
                        }
                    }
                }

                myAdapter = new NoteAdapter(MainActivity.this, notesFiltered);
                recyclerView.setAdapter(myAdapter);

                return false;
            }
        });

    searchView.setOnCloseListener(() ->
    // User done searching
    // Show all Notes, by resetting adapter
    {
        myAdapter = new NoteAdapter(MainActivity.this, notes);
        recyclerView.setAdapter(myAdapter);
        return false;
    });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.add)
        {
            Intent intent = new Intent(MainActivity.this, EditNote.class);
            intent.putExtra("addNote", true);
            getResult.launch(intent);

        }
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected

    private final ActivityResultLauncher<Intent> getResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    // Reset searchView and my adapter
                    searchView.setIconified(true);
                    searchView.clearFocus();
                    myAdapter = new NoteAdapter(MainActivity.this, notes);
                    recyclerView.setAdapter(myAdapter);

                    if(result.getResultCode() == Activity.RESULT_OK)
                    { // RESULT_OK

                        Intent intent = result.getData();

                        if (intent != null)
                        {
                            boolean addNote = intent.getBooleanExtra("addNote", true);
                            boolean deleteNote = intent.getBooleanExtra("delete", false);

                            if(deleteNote)
                            { // delete note
                                if(!addNote)
                                { // DELETE the selected note or note that has been already created
                                    try
                                    {
                                        NotesDB db = new NotesDB(MainActivity.this);
                                        db.open();
                                        db.deleteEntry("" + notes.get(selectedNoteIndex).getID());
                                        db.close();
                                    }
                                    catch (SQLException e)
                                    {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    notes.remove(selectedNoteIndex);
                                    myAdapter.notifyItemRemoved(selectedNoteIndex);
                                }
                            }
                            else // not deleting
                            {
                                String title = intent.getStringExtra("title");
                                String noteText = intent.getStringExtra("note");
                                int color = intent.getIntExtra("color",
                                        ContextCompat.getColor(getApplicationContext(), R.color.note_blue)
                                );

                                int backColor = intent.getIntExtra("backColor",
                                        ContextCompat.getColor(getApplicationContext(), R.color.note_blue_light)
                                );

                                Note note = new Note(noteText, color, backColor);
                                note.setTitle(title);

                                if(addNote)
                                {  // add the note
                                    long entryId = 0;
                                    try
                                    {
                                        NotesDB db = new NotesDB(MainActivity.this);
                                        db.open();
                                        entryId = db.createEntry(title, noteText, color, backColor);
//                                        Toast.makeText(MainActivity.this, "id: " + entryId, Toast.LENGTH_SHORT).show();
                                        db.close();
                                    }
                                    catch (SQLException exc)
                                    {
                                        Toast.makeText(MainActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    note.setID((int)entryId);

                                    notes.add(note);
                                    myAdapter.notifyItemInserted(notes.size()-1);
                                }

                                else
                                { // update the note
                                    try
                                    {
                                        NotesDB db = new NotesDB(MainActivity.this);
                                        db.open();
                                        db.updateEntry("" + notes.get(selectedNoteIndex).getID(), title, noteText, color, backColor);
                                        db.close();
                                    }
                                    catch (SQLException e)
                                    {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    note.setID(notes.get(selectedNoteIndex).getID());
                                    notes.set(selectedNoteIndex, note);
                                    myAdapter.notifyItemChanged(selectedNoteIndex);
                                }
                            }
                        }
                    }
                    else
                    { // RESULT NOT OK, DO NOTHING
//                        Toast.makeText(MainActivity.this, "Failure!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    public void onItemClicked(int index) {
        selectedNoteIndex = index;
        Note note = notes.get(index);

        Intent intent = new Intent(MainActivity.this, EditNote.class);
        intent.putExtra("addNote", false);
        intent.putExtra("title", note.getTitle());
        intent.putExtra("note", note.getNote());
        intent.putExtra("color", note.getColor());
        intent.putExtra("backColor", note.getBackgroundColor());

        getResult.launch(intent);
    } // onItemClicked
}