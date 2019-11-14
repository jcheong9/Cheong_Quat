package ca.bcit.cheong_quat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListReadingActivity extends AppCompatActivity {
    private DatabaseReference dbRef;
    private ListView lvBloodPressure;
    private List<BloodPressure> bloodPressureList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reading);
        dbRef = FirebaseDatabase.getInstance().getReference("bloodpressure");
        lvBloodPressure =  (ListView) findViewById(R.id.lvBloodPressure);
        bloodPressureList = new ArrayList<BloodPressure>();
        lvBloodPressure.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                BloodPressure bloodPressure = bloodPressureList.get(position);

                showUpdateDialog(bloodPressure.getBloodPressureID(),
                        bloodPressure.getUserID(),
                        bloodPressure.getReadDate(),
                        bloodPressure.getReadTime(),
                        bloodPressure.getSystolicRead(),
                        bloodPressure.getDiastolicRead()
                );
                return true;
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bloodPressureList.clear();
                for (DataSnapshot bloodPressureSnapshot : dataSnapshot.getChildren()) {
                    BloodPressure todo = bloodPressureSnapshot.getValue(BloodPressure.class);
                    bloodPressureList.add(todo);
                }

                BloodPressureListAdapter adapter = new BloodPressureListAdapter(ListReadingActivity.this, bloodPressureList);
                lvBloodPressure.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void showUpdateDialog(final String bloodPressureID, String userID, String readDate,
                                  String readTime, int systolicRead, int diastolicRead)  {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView tvUserID = dialogView.findViewById(R.id.etUserID);
        tvUserID.setText(userID);

        final TextView tvSystoRead = dialogView.findViewById(R.id.etSystolicReading);
        tvSystoRead.setText("" + systolicRead);

        final TextView tvDiasRead = dialogView.findViewById(R.id.etDiastolicReading);
        tvDiasRead.setText("" + diastolicRead);

        final Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);

        dialogBuilder.setTitle("Update blood pressure");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = tvUserID.getText().toString().trim();
                //String date = tvDate.getText().toString().trim();
                //String time = tvTime.getText().toString().trim();
                String systoRead = tvSystoRead.getText().toString().trim();
                String diasRead = tvDiasRead.getText().toString().trim();

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

                if (TextUtils.isEmpty(userId)) {
                    tvUserID.setError("You must enter a user ID.");
                    return;
                }
                if (TextUtils.isEmpty(systoRead)) {
                    tvSystoRead.setError("You must enter a systolic reading.");
                    return;
                }
                if (TextUtils.isEmpty(diasRead)) {
                    tvDiasRead.setError("You must enter a diastolic reading.");
                    return;
                }

                updateTodo(bloodPressureID, userId, date, time, Integer.parseInt(systoRead),
                        Integer.parseInt(diasRead));

                alertDialog.dismiss();
            }
        });
        final Button btnDelete = dialogView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTodo(bloodPressureID);
                alertDialog.dismiss();
            }
        });

    }

    private void updateTodo(String bloodPressureID, String userID, String readDate, String readTime,
                            int systolicRead, int diastolicRead) {

        DatabaseReference dbRef1 = dbRef.child(bloodPressureID);
        BloodPressure bloodPressure = new BloodPressure(bloodPressureID, userID, readDate, readTime,
                systolicRead, diastolicRead);
        Task setValueTask = dbRef1.setValue(bloodPressure);
    }

    private void deleteTodo(String id) {
        DatabaseReference dbRef1 = dbRef.child(id);

        Task setRemoveTask = dbRef1.removeValue();
        setRemoveTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(ListReadingActivity.this,
                        "Todo Deleted.",Toast.LENGTH_LONG).show();
            }
        });

        setRemoveTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ListReadingActivity.this,
                        "Something went wrong.\n" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

