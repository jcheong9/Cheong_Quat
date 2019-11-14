package ca.bcit.cheong_quat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    DatabaseReference dbRef;
    private EditText etUserID;
    private TextView tvCondition;
    private EditText etSystolicReading;
    private EditText etDiastolicReading;
    private Button btnDiastolicReading;
    private Button btnListBPView;
    private Button btnViewReport;
    private LinearLayout llMainActivityLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbRef = FirebaseDatabase.getInstance().getReference("bloodpressure");
        btnDiastolicReading = findViewById(R.id.btnDiastolicReading);
        etUserID = findViewById(R.id.etUserID);
        etSystolicReading = findViewById(R.id.etSystolicReading);
        etDiastolicReading = findViewById(R.id.etDiastolicReading);
        tvCondition = findViewById(R.id.tvCondition);
        btnListBPView = findViewById(R.id.btnListBPView);
        btnViewReport = findViewById(R.id.btnViewReport);
        llMainActivityLayout = findViewById(R.id.llMainActivityLayout);
        setDateAndTime();
        threadFunc();
        if (savedInstanceState != null) {
            String time = savedInstanceState.getString("time");
            String date = savedInstanceState.getString("date");
            String condition = savedInstanceState.getString("condition");
            tvCondition.setText(condition);
        }

        btnDiastolicReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDiastolicReading();
            }
        });
        btnListBPView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToListBloodPressureView();
            }
        });
        btnViewReport.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goToReportView();
            }
        });

    }
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        TextView tvReadingTime = (TextView) findViewById(R.id.tvTimePicker);
        String minWithZero= "";
        if((minute/10) == 0.0){
            minWithZero = "0" + minute;
        }else{
            minWithZero += minute;
        }
    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        TextView tvDisplayDate = findViewById(R.id.tvDatePicker);
        month = month + 1;
        String date = month + "/" + day + "/" + year;
        tvDisplayDate.setText(date);
    }
    private void addDiastolicReading(){
        String userID = etUserID.getText().toString().trim();
        String systolicReading = etSystolicReading.getText().toString().trim();
        String diastolicReading = etDiastolicReading.getText().toString().trim();

        if (TextUtils.isEmpty(userID)) {
            Toast.makeText(this, "You must enter a user ID.", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(systolicReading)) {
            Toast.makeText(this, "You must enter a systolic reading.", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(diastolicReading)) {
            Toast.makeText(this, "You must enter a diastolic reading.", Toast.LENGTH_LONG).show();
            return;
        }
        int sRead = Integer.parseInt(systolicReading);
        int dRead = Integer.parseInt(diastolicReading);

        String id = dbRef.push().getKey();
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        String minWithZero= "";
        if((minute/10.0) == 0.0){
            minWithZero = "0" + minute;
        }else{
            minWithZero += minute;
        }
        String date = month + "/" + day + "/" + year;
        String time = hour + ":" + minWithZero;

        BloodPressure bloodPressure = new BloodPressure(id, userID, date, time, sRead, dRead);

        Task setValueTask = dbRef.child(id).setValue(bloodPressure);

        setValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(MainActivity.this, "Todo added.", Toast.LENGTH_LONG).show();
                etUserID.setText("");
                etSystolicReading.setText("");
                etDiastolicReading.setText("");
                tvCondition.setText("");
                setDateAndTime();
            }
        });

        setValueTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,
                        "something went wrong.\n" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void processConditionReading(){
        String systolicReading = etSystolicReading.getText().toString().trim();
        String diastolicReading = etDiastolicReading.getText().toString().trim();

        if(TextUtils.isEmpty(systolicReading)){
            return;
        }
        if (TextUtils.isEmpty(diastolicReading)) {
            return;
        }
        int sRead = Integer.parseInt(systolicReading);
        int dRead = Integer.parseInt(diastolicReading);
        if(sRead <= 120 && dRead <= 80){
            tvCondition.setText(R.string.condition_normal);
        }
        else if(sRead > 120 && sRead <= 129 && dRead <= 80){
            tvCondition.setText(R.string.condition_elevated);
        }
        else if(sRead >= 130 && sRead <= 139 || dRead > 80 && dRead <= 89){
            tvCondition.setText(R.string.condition_hbp_s1);
        }
        else if(sRead >= 180 || dRead >= 120){
            if(!tvCondition.getText().toString().equals("Hypertensive Crisis")||
                    tvCondition.getText().toString().isEmpty())
                warningHypertensiveCrisis();
            tvCondition.setText(R.string.condition_hypertensive);
        }
        else if(sRead >= 140 || dRead > 90){
            tvCondition.setText(R.string.condition_hbp_s2);
        }
    }
    private void warningHypertensiveCrisis(){
        Toast.makeText(getApplicationContext(),"Warning too high blood pressure!",Toast.LENGTH_SHORT).show();
    }
    private void setDateAndTime(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String minWithZero= "";
        if((minute/10.0) == 0.0){
            minWithZero = "0" + minute;
        }else{
            minWithZero += minute;
        }
        month = month + 1;
        String date = month + "/" + day + "/" + year;
    }
    private void goToListBloodPressureView(){
        Intent intent = new Intent(this, ListReadingActivity.class);
        startActivity(intent);
    }

    private void goToReportView() {
        Intent intent = new Intent(this, ReportActivity.class);
        startActivity(intent);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("condition", tvCondition.getText().toString());
    }
    private void threadFunc() {
        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!this.isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                processConditionReading();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();
    }

}
