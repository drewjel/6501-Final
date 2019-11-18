package edu.virginia.cs.mooncake.wada;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.virginia.cs.mooncake.wada.utils.FileUtil;

public class DataListActivity extends Activity {

    private TextView tvFileNo, tvFileName;
    boolean layoutInflated = false;
    Button btnPrev, btnNext, btnDelete;
    String fileToDelete = "";
    String[] fileList;
    int currentIndex = 0;
    int fileCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                tvFileNo = (TextView) stub.findViewById(R.id.tvFileNo);
                tvFileName = (TextView) stub.findViewById(R.id.tvFileName);
                btnDelete = (Button) stub.findViewById(R.id.btnDataDelete);
                btnPrev = (Button) stub.findViewById(R.id.btnDataPrev);
                btnNext = (Button) stub.findViewById(R.id.btnDataNext);
                layoutInflated = true;
                updateList();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (layoutInflated)
            updateList();

    }

    public void btnClick(View v) {
        if (v.getId() == R.id.btnDataDelete) {
            AlertDialog alert = getAlertDialogBuilder().create();
            alert.show();

        } else if (v.getId() == R.id.btnDataBack) {
            this.finish();

        } else if (v.getId() == R.id.btnDataNext) {
            currentIndex = (currentIndex + 1) % fileCount;
            refresh();
        } else if (v.getId() == R.id.btnDataPrev) {
            currentIndex = (currentIndex - 1 + fileCount) % fileCount;
            refresh();
        }

    }

    public void updateList() {
        fileCount = FileUtil.fileCount();
        if (fileCount == 0) {
            refresh();
            return;
        }
        fileList = FileUtil.getFileListString();
        refresh();
    }

    public void refresh() {
        if (fileCount == 0) {
            tvFileNo.setText("There is no file available." );
            tvFileName.setText("");
            btnDelete.setEnabled(false);
            btnNext.setEnabled(false);
            btnPrev.setEnabled(false);
        } else {
            tvFileNo.setText("File: " + (currentIndex + 1) + " / " + fileCount);
            tvFileName.setText(fileList[currentIndex]);
            btnDelete.setEnabled(true);
            btnNext.setEnabled(true);
            btnPrev.setEnabled(true);

        }
    }

    public AlertDialog.Builder getAlertDialogBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure to delete all files?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                fileToDelete = fileList[currentIndex];
                FileUtil.deleteFile(fileToDelete);
                currentIndex = 0;
                updateList();
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
