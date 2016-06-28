package com.fullmind.avisos;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class AvisosActivity extends AppCompatActivity {

    private ListView mListView;

    private AvisosDBAdapter mDbAdapter;
    private AvisosSimpleCursorAdapter mCursorAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;


    @Override
    @TargetApi( Build.VERSION_CODES.HONEYCOMB )
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avisos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView = (ListView) findViewById(R.id.avisos_list_view);
        findViewById(R.id.avisos_list_view);
        mListView.setDivider(null);
        mDbAdapter = new AvisosDBAdapter(this);
        mDbAdapter.open();

        if (savedInstanceState == null) {
            mDbAdapter.deleteAllReminders();

            mDbAdapter.createReminder("Primer reminder, aviso creado", true);
            mDbAdapter.createReminder("Segundo reminder, aviso nuevo", false);
        }

        Cursor cursor = mDbAdapter.fetchAllReminders();

        String[] from = new String[]{
                AvisosDBAdapter.COL_CONTENT
        };

        int[] to = new int[]{
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

        //Cuando se pulsa en un ítem individual en la listview
        mListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {

            public void onItemClick( AdapterView< ? > parent, View view, final int masterListPosition, long id ){
                AlertDialog.Builder builder = new AlertDialog.Builder(AvisosActivity.this);
                ListView modeListView = new ListView(AvisosActivity.this);
                String[] modes = new String[] { "Editar Aviso", "Borrar Aviso" };
                ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(AvisosActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, modes);
                modeListView.setAdapter(modeAdapter);
                builder.setView(modeListView);
                final Dialog dialog = builder.create();
                dialog.show();

                modeListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick ( AdapterView< ? > parent, View view, int position, long id ) {

                        if ( position == 0 ) {
                            Toast.makeText( AvisosActivity.this, "editar "  + masterListPosition,
                                    Toast.LENGTH_LONG ).show();
                        } else {
                            Toast.makeText(AvisosActivity.this, "borrar "
                                    + masterListPosition, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
            }



        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {

            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) { }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.cam_menu, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_aviso:
                            for (int nC = mCursorAdapter.getCount() - 1; nC >= 0; nC--) {
                                if (mListView.isItemChecked(nC)) {
                                    mDbAdapter.deleteReminderById( getIdFromPosition(nC));
                                }
                            }
                            mode.finish();
                            mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                            return true;
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) { }
            });
        }
    }

    private int getIdFromPosition(int nC) {
        return ( int ) mCursorAdapter.getItemId( nC );
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
            Log.d(getLocalClassName(), "Crear nuevo aviso");
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_salir) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Avisos Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.fullmind.avisos/http/host/path")
        );
        AppIndex.AppIndexApi.start(mClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Avisos Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.fullmind.avisos/http/host/path")
        );
        AppIndex.AppIndexApi.end(mClient, viewAction);
        mClient.disconnect();
    }
}
