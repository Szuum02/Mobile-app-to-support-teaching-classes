package com.example.teachingapp.Tasks;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.teachingapp.R;
import com.example.teachingapp.Teacher.CheckActivity;
import com.example.teachingapp.Teacher.CheckPresence;
import com.example.teachingapp.Teacher.ChooseAction;
import com.example.teachingapp.Teacher.ShowQR;
import com.example.teachingapp.Teacher.TeacherScanQR;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class TeacherShowQRTask {
    private final ShowQR activity;
    private final Long groupId;
    private final Long lessonId;
    private ImageButton presenceButton;
    private ImageButton activityButton;
    private ImageButton scanButton;
    private ImageButton returnButton;

    public TeacherShowQRTask(ShowQR activity, Long groupId, Long lessonId) {
        this.activity = activity;
        this.groupId = groupId;
        this.lessonId = lessonId;
        this.presenceButton = activity.findViewById(R.id.calendar_button);
        this.scanButton = activity.findViewById(R.id.scan_qr_code_button);
        this.activityButton = activity.findViewById(R.id.plus_minus_button);
        this.returnButton = activity.findViewById(R.id.return_button);
    }

    public void startTask() {
        generateQRCode();
        setUpButtons();
    }

    private void generateQRCode() {
        //ImageView for generated QR code
        ImageView qrImage = activity.findViewById(R.id.qr_code);
        MultiFormatWriter mWriter = new MultiFormatWriter();
        try {
            //BitMatrix class to encode entered text and set Width & Height
            BitMatrix mMatrix = mWriter.encode(lessonId.toString(), BarcodeFormat.QR_CODE, 900,900);
            BarcodeEncoder mEncoder = new BarcodeEncoder();
            Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
            qrImage.setImageBitmap(mBitmap);//Setting generated QR code to imageView
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void setUpButtons() {
        presenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CheckPresence.class);
                intent.putExtra("group_id", groupId);
                intent.putExtra("lesson_id", lessonId);
                activity.startActivity(intent);
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TeacherScanQR.class);
                intent.putExtra("group_id", groupId);
                intent.putExtra("lesson_id", lessonId);
                activity.startActivity(intent);
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ChooseAction.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);
            }
        });

        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CheckActivity.class);
                intent.putExtra("group_id", groupId);
                intent.putExtra("lesson_id", lessonId);
                activity.startActivity(intent);
            }
        });
    }
}
