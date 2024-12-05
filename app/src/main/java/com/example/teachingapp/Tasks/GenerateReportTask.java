package com.example.teachingapp.Tasks;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.teachingapp.R;
import com.example.teachingapp.Teacher.GenerateReport;
import com.example.teachingapp.Teacher.PresenceHistory;
import com.example.teachingapp.dtos.PresenceDTO;
import com.example.teachingapp.dtos.ReportDTO;
import com.example.teachingapp.dtos.StudentReportDTO;
import com.example.teachingapp.enums.PresenceType;
import com.example.teachingapp.retrofit.Api.GroupApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenerateReportTask {
    private final GenerateReport activity;
    private final Long groupId;
    private final Long studentId;

    private final int pageHeight = 1120;
    private final int pageWidth = 792;
    private final int marginLeft = 50;
    private final int marginTop = 80;
    private int lineHeight = 80;
    private int currentY = 0;

    public GenerateReportTask(GenerateReport activity, Long groupId, Long studentId) {
        this.activity = activity;
        this.groupId = groupId;
        this.studentId = studentId;
    }

    public void generateReport() {
        RetrofitService service = new RetrofitService();
        GroupApi groupApi = service.getRetrofit().create(GroupApi.class);
        groupApi.getReportData(groupId).enqueue(new Callback<ReportDTO>() {
            @Override
            public void onResponse(Call<ReportDTO> call, Response<ReportDTO> response) {
                createReport(response.body());
            }

            @Override
            public void onFailure(Call<ReportDTO> call, Throwable t) {
                Toast.makeText(activity, "Błąd serwera", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createReport(ReportDTO reportData) {
        if (reportData == null) {
            return;
        }

        PdfDocument pdfDocument = new PdfDocument();
        Paint title = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight,1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        Canvas canvas = myPage.getCanvas();

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(activity, R.color.black));
        title.setTextSize(50);

        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Raport końcowy", 396, 560, title);

        pdfDocument.finishPage(myPage);

        generateStudentReport(reportData, pdfDocument);

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Raport.pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));

            Toast.makeText(activity, "Wygenerowano raport", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // handling error
            e.printStackTrace();
            Toast.makeText(activity, "Błąd podczas tworzenia raportu", Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();

        Intent intent = new Intent(activity, PresenceHistory.class);
        intent.putExtra("group_id", groupId);
        intent.putExtra("student_id", studentId);
        activity.startActivity(intent);
    }

    private void generateStudentReport(ReportDTO reportData, PdfDocument pdfDocument) {
        for (int i = 0; i < reportData.getStudentReports().size(); i++) {
            StudentReportDTO studentReportDTO = reportData.getStudentReports().get(i);
            PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight,i+1).create();
            PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
            Canvas canvas = myPage.getCanvas();
            Paint paint = new Paint();

            addName(studentReportDTO, paint, canvas);
            addActivity(studentReportDTO.getTotalPoints(), paint, canvas);
            HashMap<PresenceType, Integer> presenceCounter = addPresences(studentReportDTO.getPresences(), paint, canvas);
            addPresenceSummary(presenceCounter, paint, canvas);

            pdfDocument.finishPage(myPage);
        }
    }

    private void addName(StudentReportDTO studentReportDTO, Paint paint, Canvas canvas) {
        lineHeight = 60;
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(35);
        paint.setColor(ContextCompat.getColor(activity, R.color.black));
        currentY = marginTop;
        String studentName = new StringBuilder()
                .append(studentReportDTO.getName())
                .append(" ")
                .append(studentReportDTO.getLastname())
                .append(" - ")
                .append(studentReportDTO.getIndex())
                .toString();
        canvas.drawText(studentName, marginLeft, currentY, paint);
        currentY += lineHeight;
    }

    private void addActivity(long totalPoints, Paint paint, Canvas canvas) {
        lineHeight = 50;
        paint.setTextSize(20);
        canvas.drawText("Aktywność: " + totalPoints, marginLeft, currentY, paint);
        currentY += lineHeight;
    }

    private HashMap<PresenceType, Integer> addPresences(List<PresenceDTO> presenceDTOS, Paint paint, Canvas canvas) {
        lineHeight = 30;
        canvas.drawText("Obecności:", marginLeft, currentY, paint);
        currentY += lineHeight;
        HashMap<PresenceType, Integer> presenceCounter = new HashMap<>();
        for (PresenceType presenceType : PresenceType.values()) {
            if (presenceType != PresenceType.NOT_GIVEN) {
                presenceCounter.put(presenceType, 0);
            }
        }

        for (PresenceDTO presenceDTO : presenceDTOS) {
            PresenceType presenceType = presenceDTO.getPresenceType();
            canvas.drawText(convertDate(presenceDTO.getDate()) + convertPresenceType(presenceType), marginLeft, currentY, paint);
            currentY += lineHeight;
            presenceCounter.put(presenceType, presenceCounter.get(presenceType) + 1);
        }
        return presenceCounter;
    }

    private void addPresenceSummary( HashMap<PresenceType, Integer> presenceCounter, Paint paint, Canvas canvas) {
        currentY += 20;
        canvas.drawText("Podsumowanie:", marginLeft, currentY, paint);
        currentY += lineHeight;
        for (PresenceType presenceType : PresenceType.values()) {
            if (presenceType != PresenceType.NOT_GIVEN) {
                canvas.drawText(convertPresenceType(presenceType) + ": " + presenceCounter.get(presenceType), marginLeft, currentY, paint);
                currentY += lineHeight;
            }
        }
    }

    private String convertDate(String dateString) {
        String[] dateTime = dateString.split("T");
        return String.format("%s, %s - ", dateTime[0], dateTime[1]);
    }

    private String convertPresenceType(PresenceType presenceType) {
        switch (presenceType) {
            case O: return "Obecny";
            case N: return "Nieobecny";
            case S: return "Spóźniony";
            case U: return "Usprawiedliwiony";
            default: return "Błąd";
        }
    }
}
