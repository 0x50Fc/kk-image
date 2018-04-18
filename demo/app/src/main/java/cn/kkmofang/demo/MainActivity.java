package cn.kkmofang.demo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

import cn.kkmofang.image.ImageGravity;
import cn.kkmofang.image.Image;
import cn.kkmofang.image.ImageStyle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

            InputStream in = getAssets().open("app.png");

            try {

                Bitmap bitmap = BitmapFactory.decodeStream(in);

                ImageStyle style = new ImageStyle(0,0, 0, 1.0f, getResources().getDisplayMetrics().density, ImageGravity.RESIZE_ASPECT_FILL);

                Image image = new Image(bitmap,style);

                View imageView = findViewById(R.id.imageView);

                imageView.setBackground(image);
            }
            finally {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
