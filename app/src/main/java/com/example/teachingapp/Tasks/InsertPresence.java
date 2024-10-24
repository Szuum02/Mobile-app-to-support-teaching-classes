package com.example.teachingapp.Tasks;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.teachingapp.R;
import com.example.teachingapp.retrofit.Api.PresenceApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.time.LocalDateTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertPresence {

    private final long studentId;
    private final int presenceTypeId;
    private final long lessonId;
    private final Button button;
    private final List<Button> unpressedButtons;
    private TextView textView;
    private String leftHandTribe;

    public InsertPresence(long studentId, int presenceTypeId, long lessonId,
                          Button button, List<Button> unpressedButtons, TextView textView, String leftHandTribe) {
        this.studentId = studentId;
        this.presenceTypeId = presenceTypeId;
        this.lessonId = lessonId;
        this.button = button;
        this.unpressedButtons = unpressedButtons;
        this.textView = textView;
        this.leftHandTribe = leftHandTribe;
    }

    protected void removeAndAddPresence() {
        RetrofitService retrofitService = new RetrofitService();
        PresenceApi presenceApi = retrofitService.getRetrofit().create(PresenceApi.class);

        presenceApi.removeAndAddPresence(studentId, lessonId, LocalDateTime.now(), presenceTypeId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        Log.d("add", "Success");
                        changeButtonAndTextViewColours();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Log.e("SQLException", "Server error while adding presence");
                    }
                });
    }

    private void changeButtonAndTextViewColours() {
        switch (presenceTypeId) {
            case 1:
                button.setBackgroundResource(R.drawable.o_button);
                if(leftHandTribe.equals("on")){
                    textView.setBackgroundResource(R.drawable.green_textview_left_hand);
                }
                else {
                    textView.setBackgroundResource(R.drawable.green_textview_right_hand);
                }
                break;
            case 2:
                button.setBackgroundResource(R.drawable.n_button);
                if(leftHandTribe.equals("on")){
                    textView.setBackgroundResource(R.drawable.red_textview_left_hand);
                }
                else {
                    textView.setBackgroundResource(R.drawable.red_textview_right_hand);
                }
                break;
            case 3:
                button.setBackgroundResource(R.drawable.s_button);
                if(leftHandTribe.equals("on")){
                    textView.setBackgroundResource(R.drawable.yellow_textview_left_hand);
                }
                else {
                    textView.setBackgroundResource(R.drawable.yellow_textview_right_hand);
                }
                break;
        }
        for (Button otherButton : unpressedButtons) {
            otherButton.setBackgroundResource(R.drawable.gray_button);
        }
    }
}
