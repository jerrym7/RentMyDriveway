package com.challenge.danny.rentmydriveway;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRActivity extends AppCompatActivity {

    private EditText etCode;
    private Button btnQRCreate;
    private ImageView QRImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        etCode = findViewById(R.id.qrText);
        btnQRCreate = findViewById(R.id.cQrButton);
        QRImage = findViewById(R.id.QRImageView);
        etCode.setText("PartItApp", TextView.BufferType.EDITABLE);//set default text
        btnQRCreate.setOnClickListener(new View.OnClickListener(){
            //override onclick method
            public void onClick(View v)
            {
                String inputT = etCode.getText().toString().trim();
                if(inputT !=null)
                {
                    try {
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        BitMatrix bitMatrix = multiFormatWriter.encode(inputT, BarcodeFormat.QR_CODE, 1000, 1500);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        QRImage.setImageBitmap(bitmap);
                    }
                    catch (WriterException e)
                    {
                        e.printStackTrace();
                    }
                }

            }

        });
    }



}