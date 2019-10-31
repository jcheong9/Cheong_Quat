package ca.bcit.cheong_quat;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
}

