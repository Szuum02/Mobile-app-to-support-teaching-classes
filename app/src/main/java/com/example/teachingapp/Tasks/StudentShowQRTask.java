package com.example.teachingapp.Tasks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.teachingapp.R;
import com.example.teachingapp.Student.StudentMainPage;
import com.example.teachingapp.Student.StudentShowQR;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class StudentShowQRTask {
    private final StudentShowQR activity;
    private final Long studentId;
    private SharedPreferences sharedPreferences;
    private Button returnButton;

    public StudentShowQRTask(StudentShowQR activity, Long studentId, SharedPreferences sharedPreferences) {
        this.activity = activity;
        this.studentId = studentId;
        this.sharedPreferences = sharedPreferences;
        this.returnButton = activity.findViewById(R.id.return_button);
    }

    public void startTask() {
        generateQRCode();
        setUpReturnButton();
    }

    private void generateQRCode() {
        //ImageView for generated QR code
        ImageView qrImage = activity.findViewById(R.id.qr_code);
        MultiFormatWriter mWriter = new MultiFormatWriter();
        try {
            //BitMatrix class to encode entered text and set Width & Height
            BitMatrix mMatrix = mWriter.encode(studentId.toString(), BarcodeFormat.QR_CODE, 900,900);
            BarcodeEncoder mEncoder = new BarcodeEncoder();
            Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
            qrImage.setImageBitmap(mBitmap);//Setting generated QR code to imageView
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void setUpReturnButton() {
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, StudentMainPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);
            }
        });
    }
}
