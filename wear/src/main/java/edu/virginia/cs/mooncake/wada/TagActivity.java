package edu.virginia.cs.mooncake.wada;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import edu.virginia.cs.mooncake.wada.utils.ConstantsUtil;
import edu.virginia.cs.mooncake.wada.utils.FileUtil;
import edu.virginia.cs.mooncake.wada.utils.SharedPrefUtil;

public class TagActivity extends Activity {

    private TextView tvTagSelected;
    RadioGroup radioGroup;
    RadioButton rbLeft, rbRight;
    boolean layoutInflated = false;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                tvTagSelected = (TextView) stub.findViewById(R.id.tvTagAll);
                radioGroup = (RadioGroup) stub.findViewById(R.id.radioGroupHand);
                rbLeft = (RadioButton) stub.findViewById(R.id.radioHandLeft);
                rbRight = (RadioButton) stub.findViewById(R.id.radioHandRight);
                layoutInflated = true;
                transferSharedPrefToTemp();
                refresh();
            }
        });

        context = this.getApplicationContext();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (layoutInflated)
            refresh();

    }

    public void btnClick(View v) {
        if (v.getId() == R.id.btnSelectCancel) {
            this.finish();

        } else if (v.getId() == R.id.btnSelectDone) {
            transferSharedPrefFromTemp();
            this.finish();

        } else if (v.getId() == R.id.btnSelectSubject || v.getId() == R.id.btnSelectActivity || v.getId() == R.id.btnSelectInfo) {
            if (v.getId() == R.id.btnSelectSubject)
                SharedPrefUtil.putSharedPrefInt(ConstantsUtil.SHARED_PREF_TAG_TYPE, 0, context);
            else if (v.getId() == R.id.btnSelectActivity)
                SharedPrefUtil.putSharedPrefInt(ConstantsUtil.SHARED_PREF_TAG_TYPE, 1, context);
            else
                SharedPrefUtil.putSharedPrefInt(ConstantsUtil.SHARED_PREF_TAG_TYPE, 2, context);

            startActivity(new Intent(this.getApplicationContext(), TagSelectActivity.class));
        }
    }

    private void transferSharedPrefToTemp() {
        String sub = SharedPrefUtil.getSharedPref(ConstantsUtil.SHARED_PREF_SUBJECT, context);
        String act = SharedPrefUtil.getSharedPref(ConstantsUtil.SHARED_PREF_ACTIVITY, context);
        String info = SharedPrefUtil.getSharedPref(ConstantsUtil.SHARED_PREF_INFO, context);
        String hand = SharedPrefUtil.getSharedPref(ConstantsUtil.SHARED_PREF_HAND, context);

        SharedPrefUtil.putSharedPref(ConstantsUtil.SHARED_PREF_SUBJECT_TEMP, sub, context);
        SharedPrefUtil.putSharedPref(ConstantsUtil.SHARED_PREF_ACTIVITY_TEMP, act, context);
        SharedPrefUtil.putSharedPref(ConstantsUtil.SHARED_PREF_INFO_TEMP, info, context);

        if (hand != null && hand.equals(ConstantsUtil.SHARED_PREF_RIGHT)) {
            rbLeft.setChecked(false);
            rbRight.setChecked(true);
        } else {
            rbLeft.setChecked(true);
            rbRight.setChecked(false);
        }
    }


    private void transferSharedPrefFromTemp() {
        String sub = SharedPrefUtil.getSharedPref(ConstantsUtil.SHARED_PREF_SUBJECT_TEMP, context);
        String act = SharedPrefUtil.getSharedPref(ConstantsUtil.SHARED_PREF_ACTIVITY_TEMP, context);
        String info = SharedPrefUtil.getSharedPref(ConstantsUtil.SHARED_PREF_INFO_TEMP, context);

        String hand;
        int rbId = radioGroup.getCheckedRadioButtonId();
        if (rbId == R.id.radioHandLeft)
            hand = ConstantsUtil.SHARED_PREF_LEFT;
        else
            hand = ConstantsUtil.SHARED_PREF_RIGHT;

        SharedPrefUtil.putSharedPref(ConstantsUtil.SHARED_PREF_SUBJECT, sub, context);
        SharedPrefUtil.putSharedPref(ConstantsUtil.SHARED_PREF_ACTIVITY, act, context);
        SharedPrefUtil.putSharedPref(ConstantsUtil.SHARED_PREF_INFO, info, context);
        SharedPrefUtil.putSharedPref(ConstantsUtil.SHARED_PREF_HAND, hand, context);
    }

    private void refresh() {
        String subject = SharedPrefUtil.getSharedPref(ConstantsUtil.SHARED_PREF_SUBJECT_TEMP, context);
        String activity = SharedPrefUtil.getSharedPref(ConstantsUtil.SHARED_PREF_ACTIVITY_TEMP, context);
        String info = SharedPrefUtil.getSharedPref(ConstantsUtil.SHARED_PREF_INFO_TEMP, context);
        tvTagSelected.setText("S:" + subject + "; A:" + activity + "; I:" + info);
    }
}
