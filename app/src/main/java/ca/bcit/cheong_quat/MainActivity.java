package ca.bcit.cheong_quat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvReadingTime =  findViewById(R.id.tvTimePicker);
        TextView tvDisplayDate = findViewById(R.id.tvDatePicker);

        tvReadingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });
        tvDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

    }
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        TextView tvReadinTime = (TextView) findViewById(R.id.tvTimePicker);
        String minWithZero= "";
        if(minute%10 != 0){
            minWithZero = "0" + minute;
        }else{
            minWithZero += minute;
        }
        tvReadinTime.setText(hourOfDay + ":" + minWithZero);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        TextView tvDisplayDate = findViewById(R.id.tvDatePicker);
        month = month + 1;
        String date = month + "/" + day + "/" + year;
        tvDisplayDate.setText(date);
    }
}
