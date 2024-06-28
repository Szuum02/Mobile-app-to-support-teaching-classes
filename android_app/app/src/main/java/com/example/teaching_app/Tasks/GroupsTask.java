package com.example.teaching_app.Tasks;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.teaching_app.DatabaseConnection;
import com.example.teaching_app.MainActivity;
import com.example.teaching_app.R;
import com.example.teaching_app.Teacher.ChooseGroup;
import com.example.teaching_app.Teacher.ChooseLesson;
import com.example.teaching_app.Teacher.PresenceOrActivity;
import com.example.teaching_app.model.Group;
import com.example.teaching_app.retrofit.GroupApi;
import com.example.teaching_app.retrofit.RetrofitService;
import com.example.teaching_app.retrofit.TeacherApi;
import com.example.teaching_app.retrofit.UserApi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupsTask {
    private static final String TAG = "FetchGroupsTask";
    private static final String ERROR_MESSAGE = "Cannot fetch groups";

    private ChooseGroup activity;
    private long id;

    public GroupsTask(ChooseGroup activity, long id) {
        this.activity = activity;
        this.id = id;
    }

    public void findAndShowGroups() {
        RetrofitService retrofitService = new RetrofitService();
        TeacherApi teacherApi = retrofitService.getRetrofit().create(TeacherApi.class);

        teacherApi.findAllGroups(id)
                .enqueue(new Callback<List<Object[]>>() {
                    @Override
                    public void onResponse(Call<List<Object[]>> call, Response<List<Object[]>> response) {
                        List<Object[]> groups = response.body();
                        showGroup(response.body());
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

    private void showGroup(List<Object[]> groups) {
        if (groups != null && !groups.isEmpty()) {
            LinearLayout layout = activity.findViewById(R.id.linearLayout);
            layout.removeAllViews();
            for (Object[] group : groups) {
                Long groupId = ((Double) group[0]).longValue();
                String subject = (String) group[1];

                Button button = new Button(activity);
                button.setText(subject);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLessons(groupId);
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

    private void showLessons(Long groupId){
        Intent intent = new Intent(activity, ChooseLesson.class);
        intent.putExtra("group", groupId);
        Log.d("GRUPAID", String.valueOf(groupId));
        activity.startActivity(intent);
    }
}
