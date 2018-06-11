package com.posbeu.sudokuresolver;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    private SurfacePanel surface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Heap.setMainActivity(this);
        LinearLayout lay = (LinearLayout) findViewById(R.id.layout);

        surface = new SurfacePanel(getBaseContext(), null);

        lay.addView(surface);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    private static final int RESULT_SETTINGS = 16;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.viewFinal:

                Intent gameIntent = new Intent(this, ShowImageActivity.class);
                startActivity(gameIntent);

                return true;
            case R.id.solve:

                surface.goSolve();

                return true;
            case R.id.opzioni:

                Intent i = new Intent(this, SettingsActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                showUserSettings();
                break;

        }

    }

    private void showUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        String livello = sharedPrefs.getString("Livello", "NULL");
        ;



        Heap.getActivity().finish();
        Intent i = new Intent(this, MainActivity.class);
        startActivityForResult(i, RESULT_SETTINGS);


    }


}
