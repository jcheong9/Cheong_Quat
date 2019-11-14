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

public class ReportListAdapter extends ArrayAdapter<BloodPressure> {
    private Activity context;
    private List<BloodPressure> bloodPressureList;

    public ReportListAdapter(Activity context, List<BloodPressure> bloodPressure) {
        super(context, R.layout.list_report_layout, bloodPressure);
        this.context = context;
        this.bloodPressureList = bloodPressure;
    }
    public ReportListAdapter(Context context, int resource, List<BloodPressure> objects, Activity context1, List<BloodPressure> bloodPressure) {
        super(context, resource, objects);
        this.context = context1;
        this.bloodPressureList = bloodPressure;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_report_layout, null, true);

        TextView tvUserID= listViewItem.findViewById(R.id.etUserID);
        TextView tvSystoRead = listViewItem.findViewById(R.id.tvSysAverage);
        TextView tvDiasRead = listViewItem.findViewById(R.id.tvDiasAverage);
        TextView tvAvgCondition = listViewItem.findViewById(R.id.tvAverageCondition);

        BloodPressure bloodPressure = bloodPressureList.get(position);
        tvUserID.setText(bloodPressure.getUserID());
        String sysFormat = String.format("%.2f", bloodPressure.getSystolicAverage());
        String diasFormat = String.format("%.2f", bloodPressure.getDiastolicAverage());

        tvSystoRead.setText(sysFormat);
        tvDiasRead.setText(diasFormat);
        tvAvgCondition.setText(bloodPressure.getCondition());
        return listViewItem;
    }
}
