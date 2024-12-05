package com.example.teachingapp.Teacher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.GenerateReportTask;
import com.example.teachingapp.dtos.PresenceDTO;
import com.example.teachingapp.dtos.ReportDTO;
import com.example.teachingapp.dtos.StudentReportDTO;
import com.example.teachingapp.enums.PresenceType;
import com.example.teachingapp.retrofit.Api.GroupApi;
import com.example.teachingapp.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.NonNull;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GenerateReport extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            long groupId = intent.getLongExtra("group_id", -1);
            long studentId = intent.getLongExtra("student_id", 1);
            if (checkPermission()) {
                GenerateReportTask generateReportTask = new GenerateReportTask(this, groupId, studentId);
                generateReportTask.generateReport();
            }
            else {
                requestPermission();
            }
        }
    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Uzyskano dostęp", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Nie uzyskano dostępu", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}
