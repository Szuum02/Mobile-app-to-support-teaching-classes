package com.example.teachingapp.Tasks;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.teachingapp.R;
import com.example.teachingapp.Teacher.ChooseGroup;
import com.example.teachingapp.Teacher.ChooseLesson;
import com.example.teachingapp.Teacher.PresenceOrActivity;
import com.example.teachingapp.retrofit.Api.GroupApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonTask {
    private static final String TAG = "FetchGroupsTask";
    private static final String ERROR_MESSAGE = "Cannot fetch groups";

    private ChooseLesson activity;
    private long groupId;

    public LessonTask(ChooseLesson activity, long groupId) {
        this.activity = activity;
        this.groupId = groupId;
    }

    public void findAndShowLessons() {
        RetrofitService retrofitService = new RetrofitService();
        GroupApi groupApi = retrofitService.getRetrofit().create(GroupApi.class);
        groupApi.getLessons(groupId)
                .enqueue(new Callback<List<Object[]>>() {
                    @Override
                    public void onResponse(Call<List<Object[]>> call, Response<List<Object[]>> response) {
                        showLessons(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Object[]>> call, Throwable t) {
                        Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(ChooseGroup.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });
    }

//    @Override
//    protected List<String> doInBackground(Integer... teacherIds) {
//        Connection connection = null;
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//        List<String> groups = new ArrayList<>();
//
//        try {
//            connection = DatabaseConnection.getConnection();
//            if (connection != null) {
//                String query = "SELECT group_id, subject FROM Groups WHERE teacher_id = ?";
//                statement = connection.prepareStatement(query);
//                statement.setInt(1, id);
//
//                resultSet = statement.executeQuery();
//
//                while (resultSet.next()) {
//                    int groupId = resultSet.getInt("group_id");
//                    String groupName = resultSet.getString("subject");
//
//                    groups.add(Integer.toString(groupId));
//                    groups.add(groupName);
//                }
//            }
//        } catch (SQLException e) {
//            Log.e("TAG1", "SQLException: " + e.getMessage());
//        } finally {
//            // Closing database resources
//            try {
//                if (resultSet != null) resultSet.close();
//                if (statement != null) statement.close();
//                if (connection != null) connection.close();
//            } catch (SQLException e) {
//                Log.e("TAG2", "SQLException: " + e.getMessage());
//            }
//        }

//        return groups;
//    }
//
//    @Override
//    protected void onPostExecute(List<String> groups) {
//        super.onPostExecute(groups);
//        if (groups != null && groups.size() > 0) {
//            LinearLayout layout = activity.findViewById(R.id.linearLayout);
//            layout.removeAllViews();
//            for (int i = 0; i < groups.size(); i += 2) {
//                Button button = new Button(activity);
//                button.setText(groups.get(i + 1));
//                final int groupId = Integer.parseInt(groups.get(i));
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        goToChosenGroup(groupId);
//                    }
//                });
//                layout.addView(button);
//            }
//        } else {
//            Toast.makeText(activity, "Brak grup do wyświetlenia", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void showLessons(List<Object[]> lessons) {
        if (lessons != null && !lessons.isEmpty()) {
            LinearLayout layout = activity.findViewById(R.id.linearLayout);
            layout.removeAllViews();
            for (Object[] lesson : lessons) {
                Long lessonId = ((Double) lesson[0]).longValue();
                String subject = (String) lesson[1];

                Button button = new Button(activity);
                button.setText(subject);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToChosenGroup(lessonId);
                    }
                });
                layout.addView(button);
            }
//
//            for (int i = 0; i < groups.size(); i += 2) {
//                Button button = new Button(activity);
//                button.setText(groups.get(i + 1));
//                final int groupId = Integer.parseInt(groups.get(i));
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        goToChosenGroup(groupId);
//                    }
//                });
//                layout.addView(button);
//            }
        } else {
            Toast.makeText(activity, "Brak grup do wyświetlenia", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToChosenGroup(Long lessonId){
        Intent intent = new Intent(activity, PresenceOrActivity.class);
        intent.putExtra("lesson", lessonId);
        Log.d("lessonId", String.valueOf(lessonId));
        activity.startActivity(intent);
    }
}
