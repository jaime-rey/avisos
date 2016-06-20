package com.fullmind.avisos;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AvisosActivity extends AppCompatActivity {

    private ListView mListView;

    private AvisosDBAdapter mDbAdapter;
    private AvisosSimpleCursorAdapter mCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avisos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView = ( ListView ) findViewById( R.id.avisos_list_view );
        findViewById( R.id.avisos_list_view );
        mListView.setDivider( null );
        mDbAdapter = new AvisosDBAdapter( this );
        mDbAdapter.open();

        if ( savedInstanceState == null ) {
            mDbAdapter.deleteAllReminders();

            mDbAdapter.createReminder("Primer reminder, aviso creado", true);
            mDbAdapter.createReminder("Segundo reminder, aviso nuevo", false);
        }

        Cursor cursor = mDbAdapter.fetchAllReminders();

        String[] from = new String[]{
          AvisosDBAdapter.COL_CONTENT
        };

        int[] to = new  int[]{
                R.id.row_text
        };

        mCursorAdapter = new AvisosSimpleCursorAdapter(
                //context
                AvisosActivity.this,
                //el layout de la fila
                R.layout.avisos_row,
                //cursor
                cursor,
                //desde columnas definidas en la BDD
                from,
                //a las ids de views en el layout
                to,
                //flagg - no usado
                0
        );

        mListView = (ListView) findViewById(R.id.avisos_list_view);


        mListView.setAdapter(mCursorAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_avisos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_nuevo) {
            Log.d( getLocalClassName(), "Crear nuevo aviso" );
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_salir) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
