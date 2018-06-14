package com.posbeu.sudokuresolver;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.posbeu.sudokuresolver.core.Sudoku;
import com.posbeu.sudokuresolver.core.Table;
import com.posbeu.sudokuresolver.core.TableCell;

public class MainActivity extends Activity {

    public SurfacePanel getSurface() {
        return surface;
    }

    private SurfacePanel surface;


    private Table table=new Table();

    private Sudoku sudoku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Heap.setMainActivity(this);
        LinearLayout lay = findViewById(R.id.board);

        surface = new SurfacePanel(getBaseContext(), null, this);
        lay.addView(surface);

        handleButtons();

        sudoku = new Sudoku(this);
        
    }

    private void setFixedVal(int n){
        if( Heap.selectedCell==null)return;
        TableCell p = Heap.selectedCell;
        table.setFixed(p.getX(),p.getY(), n);
    }

    private void handleButtons() {

        Button b1 = findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFixedVal(1);
            }
        });
        Button b2 = findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFixedVal(2);
            }
        });
        Button b3 = findViewById(R.id.b3);
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFixedVal(3);
            }
        });
        Button b4 = findViewById(R.id.b4);
        b4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFixedVal(4);
            }
        });
        Button b5 = findViewById(R.id.b5);
        b5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFixedVal(5);
            }
        });
        Button b6 = findViewById(R.id.b6);
        b6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFixedVal(6);
            }
        });
        Button b7 = findViewById(R.id.b7);
        b7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFixedVal(7);
            }
        });
        Button b8 = findViewById(R.id.b8);
        b8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFixedVal(8);
            }
        });
        Button b9 = findViewById(R.id.b9);
        b9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFixedVal(9);
            }
        });


        Button solve = findViewById(R.id.solve);
        solve.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sudoku.go();
            }
        });

        Button clean = findViewById(R.id.clean);
        clean.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sudoku.setClean();
            }
        });

        Button cleanAll = findViewById(R.id.cleanAll);
        cleanAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sudoku.cleanAll();
            }
        });
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

    public Sudoku getSudoku() {
        return sudoku;
    }
    public Table getTable() {
        return table;
    }

    public void update() {
        surface.update();
    }
}
