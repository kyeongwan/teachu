package kr.ac.ssu.teachu.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kr.ac.ssu.teachu.R;
import kr.ac.ssu.teachu.model.Board;
import kr.ac.ssu.teachu.model.Schedule;
import kr.ac.ssu.teachu.util.AppController;
import kr.ac.ssu.teachu.util.DBManager;

/**
 * Created by lk on 15. 12. 9..
 */
public class ScheduleaddFragment extends DialogFragment {

    Schedule schedule;
    Date date;
    Activity activity;

    public ScheduleaddFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scheduleadd, container);

        final EditText et1 = (EditText) view.findViewById(R.id.et1);
        final EditText et2 = (EditText) view.findViewById(R.id.et2);
        final EditText et3 = (EditText) view.findViewById(R.id.et3);

        Button btClose = (Button) view.findViewById(R.id.bt2);
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBManager.getInstance().write("INSERT INTO schedule (title, time, date, context)" + "VALUES('" + et1.getText().toString() + "', '" + et2.getText().toString() + "', '" + date.getTime() + "', '" + et3.getText().toString() + "')");

                getDialog().dismiss();

            }
        });

        Button btClose2 = (Button) view.findViewById(R.id.bt1);
        btClose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getDialog().dismiss();
                DialogDatePicker();
            }
        });


        // 레이아웃 XML과 뷰 변수 연결

        return view;
    }

    private void DialogDatePicker() {
        Calendar c = Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH);
        int cday = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    // onDateSet method
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date_selected = String.valueOf(monthOfYear + 1) +
                                " /" + String.valueOf(dayOfMonth) + " /" + String.valueOf(year);

                        String from = year + "-" + monthOfYear + "-" + dayOfMonth + "08";
                        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            date = transFormat.parse(from);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                };
        DatePickerDialog alert = new DatePickerDialog(activity, mDateSetListener,
                cyear, cmonth, cday);
        alert.show();
    }
}
