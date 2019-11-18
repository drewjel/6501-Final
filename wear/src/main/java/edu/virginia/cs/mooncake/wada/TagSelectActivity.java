package edu.virginia.cs.mooncake.wada;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

import edu.virginia.cs.mooncake.wada.utils.ConstantsUtil;
import edu.virginia.cs.mooncake.wada.utils.SharedPrefUtil;
import edu.virginia.cs.mooncake.wada.utils.WadaUtils;

public class TagSelectActivity extends Activity {

    private TextView tvTagName, tvCurrentTag;
    boolean layoutInflated = false;
    String[] tagNames = {"Subject", "Activity", "Info"};
    String[] tagList;
    int currentIndex = 0;
    int tagCount;
    Context context;
    int tagType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_select);

        context = this.getApplicationContext();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                tvTagName = (TextView) stub.findViewById(R.id.tvTagName);
                tvCurrentTag = (TextView) stub.findViewById(R.id.tvCurrentTag);
                layoutInflated = true;
                tagType = SharedPrefUtil.getSharedPrefInt(ConstantsUtil.SHARED_PREF_TAG_TYPE, context);
                tagList = WadaUtils.tagList(tagType);
                tagCount = tagList.length;
                refresh();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (layoutInflated)
            refresh();

    }

    public void btnClick(View v) {
        if (v.getId() == R.id.btnTagSelect) {
            String key = ConstantsUtil.SHARED_PREF_SUBJECT_TEMP;
            if (tagType == 1)
                key = ConstantsUtil.SHARED_PREF_ACTIVITY_TEMP;
            else if (tagType == 2)
                key = ConstantsUtil.SHARED_PREF_INFO_TEMP;

            SharedPrefUtil.putSharedPref(key, tagList[currentIndex], context);
            this.finish();

        } else if (v.getId() == R.id.btnTagBack) {
            this.finish();

        } else if (v.getId() == R.id.btnTagNext) {
            currentIndex = (currentIndex + 1) % tagCount;
            refresh();
        } else if (v.getId() == R.id.btnTagPrev) {
            currentIndex = (currentIndex - 1 + tagCount) % tagCount;
            refresh();
        }

    }


    public void refresh() {
        tvTagName.setText(tagNames[tagType] + " (" + (currentIndex + 1) + "/" + tagCount + "): ");
        tvCurrentTag.setText(tagList[currentIndex]);
    }


}

