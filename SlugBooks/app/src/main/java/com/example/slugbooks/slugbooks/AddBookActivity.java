package com.example.slugbooks.slugbooks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AddBookActivity extends AppCompatActivity {

    private Uri chosenImage;
    Button addImage;
    public String imageURLstring = "N/A";

    private static final int CHOOSE_IMAGE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);


        addImage = (Button) findViewById(R.id.addImageButtonID);
    }

    //get the image from gllery
    private void openPhotos(View view) {
        ImageView imageView = new ImageView(this);
        Intent photos = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(photos,CHOOSE_IMAGE);
    }

    //get the image url and save it, also as a test post it on the page
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(resultCode==RESULT_OK && requestCode == CHOOSE_IMAGE) {
            chosenImage = data.getData();
            String path = chosenImage.getPath();

            System.out.println("=-=-=-=-=---=-=-=-=-=-= " + path );

            imageURLstring = chosenImage.getPath();
            //bookImage.setImageURI(selectedImage);
            System.out.println("URL is : " + imageURLstring);

            // bookImage.setImageURI(selectedImage);
        }
    }

}
