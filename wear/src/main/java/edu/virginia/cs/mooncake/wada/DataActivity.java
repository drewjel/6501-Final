package edu.virginia.cs.mooncake.wada;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.virginia.cs.mooncake.wada.utils.DateTimeUtil;
import edu.virginia.cs.mooncake.wada.utils.FileUtil;
import edu.virginia.cs.mooncake.wada.utils.ServiceUtil;
import edu.virginia.cs.mooncake.wada.utils.SharedPrefUtil;
import edu.virginia.cs.mooncake.wada.utils.WadaUtils;

public class DataActivity extends Activity {

    private TextView tvFileCount, tvConfig;
    boolean layoutInflated = false;
    Button btnList, btnConfig, btnDeleteAll;
    int deleteType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                tvFileCount = (TextView) stub.findViewById(R.id.tvFileCount);
                tvConfig = (TextView) stub.findViewById(R.id.tvConfig);
                btnList = (Button) stub.findViewById(R.id.btnDataList);
                btnDeleteAll = (Button) stub.findViewById(R.id.btnDataDeleteAll);
                btnConfig = (Button) stub.findViewById(R.id.btnDataConfigSetDefault);
                layoutInflated = true;
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
        if (v.getId() == R.id.btnDataDeleteAll) {
            deleteType = 1;
            AlertDialog alert = getAlertDialogBuilder().create();
            alert.show();

        } else if (v.getId() == R.id.btnDataList) {
            startActivity(new Intent(this, DataListActivity.class));

        } else if (v.getId() == R.id.btnDataConfigSetDefault) {
            deleteType = 2;
            AlertDialog alert = getAlertDialogBuilder().create();
            alert.show();
        }

    }

    public void refresh() {
        int fileCount = FileUtil.fileCount();
        tvFileCount.setText("File Count: " + fileCount);
        if (fileCount > 0) {
            btnList.setEnabled(true);
            btnDeleteAll.setEnabled(true);
        } else {
            btnList.setEnabled(false);
            btnDeleteAll.setEnabled(false);
        }


        if (FileUtil.isConfigAvailable()) {
            btnConfig.setEnabled(true);
            if (FileUtil.readConfig() == null)
                tvConfig.setText("Config: error");
            else
                tvConfig.setText("Config: available");
        } else {
            tvConfig.setText("Config: default");
            btnConfig.setEnabled(false);
        }

    }

    public AlertDialog.Builder getAlertDialogBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        if (deleteType == 1)
            builder.setMessage("Are you sure to delete all files?");
        else if (deleteType == 2)
            builder.setMessage("Are you sure to delete config?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (deleteType == 1)
                    FileUtil.deleteAllFiles();
                else if (deleteType == 2)
                    FileUtil.deleteConfig();
                refresh();
                dialog.dismiss();
            }

        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        return builder;

    }


}

