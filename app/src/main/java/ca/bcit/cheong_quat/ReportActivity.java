package ca.bcit.cheong_quat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ReportActivity extends AppCompatActivity {

    private DatabaseReference dbRef;
    private ListView lvReports;
    private List<BloodPressure> averageReportList;
    private List<BloodPressure> bloodPressureList;
    private ArrayList<String> idList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_layout);
        dbRef = FirebaseDatabase.getInstance().getReference("bloodpressure");
        lvReports = findViewById(R.id.lvAverageReports);
        averageReportList = new ArrayList<BloodPressure>();
        bloodPressureList= new ArrayList<BloodPressure>();
        idList = new ArrayList<String>();

        TextView reportHeader = findViewById(R.id.reportHeader);
        reportHeader.setText(R.string.report_header);
    }
    @Override
    protected void onStart() {
        super.onStart();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                averageReportList.clear();
                for (DataSnapshot bloodPressureSnapshot : dataSnapshot.getChildren()) {

                    BloodPressure todo = bloodPressureSnapshot.getValue(BloodPressure.class);

                    System.out.println(todo.getUserID());

                    bloodPressureList.add(todo);

                    if (!idList.contains(todo.getUserID())) {
                        idList.add(todo.getUserID());
                    }
                }

                generateReport();

                ReportListAdapter adapter = new ReportListAdapter(ReportActivity.this, averageReportList);
                lvReports.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void generateReport() {

        for (int i = 0; i < idList.size(); i++) {
            BloodPressure report;
            String userID = "";
            int counter = 0;
            double sysTotal = 0;
            double diasTotal = 0;
            double sysAverage = 0;
            double diasAverage = 0;
            String condition = "";

            for (int j = 0; j < bloodPressureList.size(); j++) {
                if(bloodPressureList.get(j).getUserID().equals(idList.get(i))) {
                    userID = bloodPressureList.get(j).getUserID();
                    counter++;
                    sysTotal += bloodPressureList.get(j).getSystolicRead();
                    diasTotal += bloodPressureList.get(j).getDiastolicRead();
                }
            }
            sysAverage = sysTotal / counter;
            diasAverage = diasTotal / counter;

            if(sysAverage <= 120 && diasAverage <= 80){
                condition = getResources().getString(R.string.condition_normal);
            }
            else if(sysAverage > 120 && sysAverage <= 129 && diasAverage <= 80){
                condition = getResources().getString(R.string.condition_elevated);
            }
            else if(sysAverage >= 130 && sysAverage <= 139 || diasAverage > 80 && diasAverage <= 89){
                condition = getResources().getString(R.string.condition_hbp_s1);
            }
            else if(sysAverage >= 180 || diasAverage >= 120){
                condition = getResources().getString(R.string.condition_hypertensive);
            }
            else if(sysAverage >= 140 || diasAverage > 90){
                condition = getResources().getString(R.string.condition_hbp_s2);
            }

            report = new BloodPressure(userID, sysAverage, diasAverage, condition);

            averageReportList.add(report);

            System.out.println("ID: " + report.getUserID());
            System.out.println("Sys Average: " + report.getSystolicAverage());
            System.out.println("Dias Average: " + report.getDiastolicAverage());
        }
    }
}
