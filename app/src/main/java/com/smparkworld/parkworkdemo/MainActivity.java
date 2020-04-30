package com.smparkworld.parkworkdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.smparkworld.parkimagepicker.ParkImagePicker;
import com.smparkworld.parkwork.ParkWork;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etData;
    private TextView tvResponse;
    private ImageView ivSelectedImage;
    private String selectedImageUri;
    private HashMap<String, String> outputData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etData = findViewById(R.id.etData);
        tvResponse = findViewById(R.id.tvResponse);
        ivSelectedImage = findViewById(R.id.ivSelectedImage);
        findViewById(R.id.btnStringSubmit).setOnClickListener(this);
        findViewById(R.id.btnImagePicker).setOnClickListener(this);
        findViewById(R.id.btnImageSubmit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnStringSubmit:
                outputData = new HashMap<>();
                outputData.put("testKey", etData.getText().toString());

                ParkWork.create(this, "")   // Please enter the URI.
                        .setListener(new ParkWork.OnResponseListener() {
                            @Override
                            public void onResponse(String response) {
                                tvResponse.setText(response);
                            }
                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setData(outputData)
                        .start();
                break;

            case R.id.btnImagePicker:

                // This is ImagePicker library. "https://github.com/Park-SM/ParkImagePicker"
                ParkImagePicker.create(this)
                        .setOnSelectedListener(new ParkImagePicker.OnSingleSelectedListener() {
                            @Override
                            public void onImageSelected(@NonNull String uri) {
                                selectedImageUri = uri;
                                Glide.with(MainActivity.this).load(uri).into(ivSelectedImage);
                            }
                        })
                        .start();
                break;

            case R.id.btnImageSubmit:
                if (selectedImageUri == null) break;

                outputData = new HashMap<>();
                outputData.put("testKey", selectedImageUri);

                ParkWork.create(this, "")   // Please enter the URI.
                        .setListener(new ParkWork.OnResponseListener() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(MainActivity.this, "Result: " + response, Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(MainActivity.this, "Fail!: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setData(outputData)
                        .setType(ParkWork.WORK_IMAGE)
                        .start();

                break;
        }

    }
}
