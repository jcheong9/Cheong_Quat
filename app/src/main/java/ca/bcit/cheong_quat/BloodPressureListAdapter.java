package ca.bcit.cheong_quat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class BloodPressureListAdapter extends ArrayAdapter<BloodPressure> {
    private Activity context;
    private List<BloodPressure> bloodPressureList;

    public BloodPressureListAdapter(Activity context, List<BloodPressure> bloodPressure) {
        super(context, R.layout.list_layout, bloodPressure);
        this.context = context;
        this.bloodPressureList = bloodPressure;
    }
    public BloodPressureListAdapter(Context context, int resource, List<BloodPressure> objects, Activity context1, List<BloodPressure> bloodPressure) {
        super(context, resource, objects);
        this.context = context1;
        this.bloodPressureList = bloodPressure;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView tvUserID= listViewItem.findViewById(R.id.etUserID);
        TextView tvDate = listViewItem.findViewById(R.id.tvDatePicker);
        TextView tvTime = listViewItem.findViewById(R.id.tvTimePicker);
        TextView tvSystoRead = listViewItem.findViewById(R.id.etSystolicReading);
        TextView tvDiasRead = listViewItem.findViewById(R.id.etDiastolicReading);

        BloodPressure bloodPressure = bloodPressureList.get(position);
        tvUserID.setText(bloodPressure.getUserID());
        tvDate.setText(bloodPressure.getReadDate());
        tvTime.setText(bloodPressure.getReadTime());
        tvSystoRead.setText(Integer.toString(bloodPressure.getSystolicRead()));
        tvDiasRead.setText(Integer.toString(bloodPressure.getDiastolicRead()));
        return listViewItem;
    }
}
