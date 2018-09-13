package com.mzdhr.roomvssqlite;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mzdhr.roomvssqlite.Room.MainActivityViewModel;
import com.mzdhr.roomvssqlite.Room.NoteEntity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });


        // Preparing our note objects
        NoteEntity noteEntity1 = new NoteEntity("This is first note", "Body for this note");
        NoteEntity noteEntity2 = new NoteEntity("This is first note", "Body for this note");

        // Prepare ViewModel
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        // Insert
        mViewModel.insert(noteEntity1);
        mViewModel.insert(noteEntity2);

        // Update
        mViewModel.update(new NoteEntity(1,"User", "Body"));

        // Delete
        mViewModel.delete(noteEntity2);

        // List all notes
        mViewModel.getAllNotes().observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(@Nullable List<NoteEntity> noteEntities) {
                for (int i = 0; i < noteEntities.size(); i++) {
                    Log.d(TAG, "Title: " + noteEntities.get(i).getTitle() + " Body: " + noteEntities.get(i).getBody());
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
