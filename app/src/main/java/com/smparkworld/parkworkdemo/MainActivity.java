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

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etData;
    private TextView tvResponse;
    private ImageView ivSelectedImage;
    private String selectedImageUri;

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
        findViewById(R.id.btnImageStringSubmit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.btnStringSubmit:
                HashMap<String, String> strData1 = new HashMap<>();
                strData1.put("exampleKey", etData.getText().toString());

                ParkWork.create(this, "")   // Please enter the URI.
                        .setOnResponseListener(new ParkWork.OnResponseListener() {
                            @Override
                            public void onResponse(String response) {
                                tvResponse.setText(response);
                            }
                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setStringData(strData1)
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
                if (selectedImageUri == null || selectedImageUri.length() == 0) break;

                HashMap<String, String> imgData1 = new HashMap<>();
                imgData1.put("exampleKey", selectedImageUri);

                ParkWork.create(this, "")   // Please enter the URI.
                        .setOnResponseListener(new ParkWork.OnResponseListener() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(MainActivity.this, "Result: " + response, Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(MainActivity.this, "Fail!: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setImageData(imgData1)
                        .start();
                break;

            case R.id.btnImageStringSubmit:
                if (selectedImageUri == null || selectedImageUri.length() == 0) {
                    Toast.makeText(this, "Please select an image.", Toast.LENGTH_SHORT).show();
                    break;
                }
                    
                if (etData.getText().length() == 0) {
                    Toast.makeText(this, "Enter the string", Toast.LENGTH_SHORT).show();
                    etData.requestFocus();
                    break;
                }

                HashMap<String, String> imgData2 = new HashMap<>();
                imgData2.put("imgExampleKey", selectedImageUri);

                HashMap<String, String> strData2 = new HashMap<>();
                strData2.put("strExampleKey", etData.getText().toString());

                ParkWork.create(this, "")   // Please enter the URI.
                        .setOnResponseListener(new ParkWork.OnResponseListener() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(MainActivity.this, "Result: " + response, Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(MainActivity.this, "Fail!: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setImageData(imgData2)
                        .setStringData(strData2)
                        .start();
                break;
        }

    }
}
