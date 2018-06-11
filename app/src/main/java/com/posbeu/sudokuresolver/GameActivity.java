package com.posbeu.sudokuresolver;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;*/

import java.io.IOException;

public class GameActivity extends Activity {

    private Button bottone1;
    private Button bottone2;
    private Button bottone3;
    private Bitmap out;
    private static int RESULT_LOAD_IMG = 1;
    private static int SELECT_FILE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
     /*   MobileAds.initialize(getApplicationContext(), "ca-app-pub-6114671792914206~9081633054");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/


        bottone1 = (Button) findViewById(R.id.bottone1);
        bottone2 = (Button) findViewById(R.id.bottone2);
      //  bottone3 = (Button) findViewById(R.id.bottone3);

        bottone1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

            }
        });


        bottone2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Heap.getBitmap() == null) {
                    Toast.makeText(GameActivity.this, "Devi scegliere una immagine",
                            Toast.LENGTH_LONG).show();

                    return;
                }
                Intent openPage1 = new Intent(GameActivity.this, MainActivity.class);


                startActivity(openPage1);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
//            else if (requestCode == REQUEST_CAMERA)
//                onCaptureImageResult(data);

            showSelected();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showSelected();
    }

    private void showSelected() {
        if (Heap.getBitmap() == null) return;
        ImageView view = (ImageView) findViewById(R.id.imgView);
        view.setImageBitmap(Heap.getBitmap());
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                Heap.setBitmap(bm);

                Intent intent = new Intent(GameActivity.this, MainActivity.class);


                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
