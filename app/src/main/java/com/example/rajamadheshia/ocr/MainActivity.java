package com.example.rajamadheshia.ocr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class MainActivity extends AppCompatActivity {


    ImageView mImageView;
    Bitmap imageBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView)findViewById(R.id.imageView);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            //imageBitmap = invert(imageBitmap);
            mImageView.setImageBitmap(imageBitmap);


        }
    }

    public void takeImage(View view) {
        dispatchTakePictureIntent();
    }

    public void ocr(View view) {
        //Bitmap bitmap =  BitmapFactory.decodeResource(getApplicationContext().getResources(),R.id.imageView);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if(!textRecognizer.isOperational()){
            Toast.makeText(getApplicationContext(),"Couldn't load the Text",Toast.LENGTH_SHORT).show();
        }
        else{
            Frame frame = new Frame.Builder().setBitmap(imageBitmap).build();

            SparseArray<TextBlock> item = textRecognizer.detect(frame);

            StringBuilder text = new StringBuilder();

            for(int i=0 ;i<item.size();i++){
                TextBlock myitems = item.valueAt(i);
                text.append(myitems.getValue());
                text.append("\n");
            }
            Intent intent = new Intent(MainActivity.this,Main2Activity.class);
            intent.putExtra("data",text.toString());
            startActivity(intent);

        }
    }

//    private static final int RGB_MASK = 0x000000;
//
//    public Bitmap invert(Bitmap original) {
//        // Create mutable Bitmap to invert, argument true makes it mutable
//        Bitmap inversion = original.copy(Bitmap.Config.ARGB_8888, true);
//
//        // Get info about Bitmap
//        int width = inversion.getWidth();
//        int height = inversion.getHeight();
//        int pixels = width * height;
//
//        // Get original pixels
//        int[] pixel = new int[pixels];
//        inversion.getPixels(pixel, 0, width, 0, 0, width, height);
//
//        // Modify pixels
//        for (int i = 0; i < pixels; i++)
//            pixel[i] ^= RGB_MASK;
//        inversion.setPixels(pixel, 0, width, 0, 0, width, height);
//
//        // Return inverted Bitmap
//        return inversion;
//    }
}
