package com.example.teachingapp.Teacher;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.teachingapp.enums.PresenceType;
import com.example.teachingapp.retrofit.Api.PresenceApi;
import com.example.teachingapp.retrofit.RetrofitService;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherScanQR extends AppCompatActivity {
    private final TeacherScanQR thisIntent = this;
    private long lessonId;
    private long groupId;
    private static final int PERMISSION_REQUEST_CAMERA = 1;
    private final RetrofitService retrofitService = new RetrofitService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            lessonId = intent.getLongExtra("lesson_id", 0); // handle error
            groupId = intent.getLongExtra("group_id", 0); // handle error
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                } else {
                    initQRCodeScanner();
                }
            } else {
                initQRCodeScanner();
            }
        }
    }

    private void initQRCodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                goToPresences();
            } else {
                addStudentPresence(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initQRCodeScanner();
            } else {
                Toast.makeText(this, "Wymagany dostęp do kamery", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void addStudentPresence(String data) {
        Long studentId = Long.parseLong(data);
        LocalDateTime dateTime = LocalDateTime.now();

        PresenceApi presenceApi = retrofitService.getRetrofit().create(PresenceApi.class);
        presenceApi.removeAndAddPresence(studentId, lessonId, dateTime, PresenceType.stringToPresenceType("O")).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(thisIntent, "Dodano obecność", Toast.LENGTH_LONG).show();
                new IntentIntegrator(thisIntent).initiateScan();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // todo -> handle error
            }
        });
    }

    private void goToPresences() {
        Intent intent = new Intent(this, CheckPresence.class);
        intent.putExtra("group_id", groupId);
        intent.putExtra("lesson_id", lessonId);
        this.startActivity(intent);
    }
}
