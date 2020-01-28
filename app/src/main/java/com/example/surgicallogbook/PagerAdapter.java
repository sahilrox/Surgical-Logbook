package com.example.surgicallogbook;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.surgicallogbook.Tabs.OperationView;
import com.example.surgicallogbook.Tabs.PatientView;
import com.example.surgicallogbook.Tabs.ProcedureView;


public class PagerAdapter extends FragmentStatePagerAdapter {

    int tabs;

    public PagerAdapter(FragmentManager fm, int tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                PatientView p = new PatientView();
                return p;
            case 1:
                OperationView o = new OperationView();
                return o;
            case 2:
                ProcedureView pr = new ProcedureView();
                return pr;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabs;
    }
}
