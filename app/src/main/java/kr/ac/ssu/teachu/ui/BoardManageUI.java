package kr.ac.ssu.teachu.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import kr.ac.ssu.teachu.R;
import kr.ac.ssu.teachu.adapter.StudentListAdapter;
import kr.ac.ssu.teachu.model.StudentArray;

/**
 * Created by nosubin on 2015-12-03.
 */
public class BoardManageUI extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_student_listview);
        ListView listView = (ListView)findViewById(R.id.student_listview);
        ArrayList<StudentArray> arrayList = new ArrayList<StudentArray>();
        StudentListAdapter adapter = new StudentListAdapter(getApplicationContext(),R.layout.activity_student,arrayList);

        arrayList.add(new StudentArray("수빈"));
        arrayList.add(new StudentArray("경완"));

        listView.setAdapter(adapter);
    }


}