package kr.ac.ssu.teachu.ui;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import kr.ac.ssu.teachu.R;

/**
 * Created by nosubin on 2015-12-09.
 */
public class Pref_UI extends PreferenceActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting);
    }

}
