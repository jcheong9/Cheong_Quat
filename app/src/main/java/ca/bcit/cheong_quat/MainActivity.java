package ca.bcit.cheong_quat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvReadinTime = (TextView) findViewById(R.id.tvTimePicker);

        tvReadinTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });

    }
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        TextView tvReadinTime = (TextView) findViewById(R.id.tvTimePicker);
        tvReadinTime.setText("Hour: " + hourOfDay + "Minute: " + minute);
    }
}
