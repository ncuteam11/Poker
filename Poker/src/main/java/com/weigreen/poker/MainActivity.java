package com.weigreen.poker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow.LayoutParams;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.weigreen.ncu.tfh.bridge.TFHBridgeDataMakeRoom;
import com.weigreen.ncu.tfh.bridge.TFHBridgeDataPlayer;
import com.weigreen.ncu.tfh.bridge.TFHBridgeDataRoomList;
import com.weigreen.ncu.tfh.bridge.TFHBridgeMain;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private Handler mHandler = new Handler();
    private TFHClientSocket socket;


    private ArrayList<Integer> roomPortList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        socket = new TFHClientSocket(MainActivity.this);
        socket.start();
        TableLayout table = (TableLayout) findViewById(R.id.room_list);

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(2, 2, 2, 2);
        resetRoomListTable(table, lp);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void haveNewDataRoom(TFHBridgeMain main){

    }

    public void switchToMakeRoom(){

    }

    /**
     * Make new room done
     * @param view
     */
    public void makeRoomSubmit(View view){
        runMakeRoom();
    }

    public void onRefreshRoomList(View view){
        Log.d("button", "i'm here");
        TableLayout table = (TableLayout) findViewById(R.id.room_list);
        socket.refreshRoom();

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(2, 2, 2, 2);
        resetRoomListTable(table, lp);
    }

    /**
     * Reset the table
     * @param table
     * @param lp
     */
    private void resetRoomListTable(TableLayout table, LayoutParams lp){
        table.removeAllViewsInLayout();
        TableRow tableMainRow = new TableRow(MainActivity.this);

        tableMainRow.setLayoutParams(lp);

        TextView mainID = new TextView(MainActivity.this);
        mainID.setLayoutParams(lp);
        mainID.setText(getString(R.string.table_main_id));
        mainID.setBackgroundResource(R.drawable.cell_shape);

        TextView mainName = new TextView(MainActivity.this);
        mainName.setLayoutParams(lp);
        mainName.setText(getString(R.string.table_main_name));
        mainName.setBackgroundResource(R.drawable.cell_shape);

        TextView mainJoinPeople = new TextView(MainActivity.this);
        mainJoinPeople.setLayoutParams(lp);
        mainJoinPeople.setText(getString(R.string.table_main_people));
        mainJoinPeople.setBackgroundResource(R.drawable.cell_shape);

        TextView mainJoin = new TextView(MainActivity.this);
        mainJoin.setLayoutParams(lp);
        mainJoin.setText(getString(R.string.table_main_join));
        mainJoinPeople.setBackgroundResource(R.drawable.cell_shape);

        tableMainRow.addView(mainID);
        tableMainRow.addView(mainName);
        tableMainRow.addView(mainJoinPeople);
        tableMainRow.addView(mainJoin);

        table.addView(tableMainRow);
    }

    public void returnRoomList(final TFHBridgeDataRoomList data){
        Log.d("SERVER=>CLIENT", "returnRoomList");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                TableLayout table = (TableLayout)findViewById(R.id.room_list);

                LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.setMargins(2, 2, 2, 2);
                resetRoomListTable(table, lp);

                roomPortList = new ArrayList<Integer>();
                for (int i=0; i<data.getRow(); i++){
                    String id = data.getData(i, 0);
                    final String port = data.getData(i, 1);
                    String join = data.getData(i, 2);
                    String name = data.getData(i, 3);
                    roomPortList.add(Integer.getInteger(port));
                    Log.d("room" + i, id);

                    TableRow tableRow = new TableRow(MainActivity.this);
                    tableRow.setLayoutParams(lp);
                    tableRow.setBackgroundColor(Color.BLACK);


                    TextView rowID = new TextView(MainActivity.this);
                    rowID.setLayoutParams(lp);
                    rowID.setText(id);
                    //rowID.setBackgroundResource(R.drawable.cell_shape);
                    rowID.setBackgroundColor(Color.WHITE);
                    rowID.setPadding(1, 1, 1, 1);

                    TextView rowName = new TextView(MainActivity.this);
                    rowName.setLayoutParams(lp);
                    rowName.setText(name);
                    rowName.setBackgroundResource(R.drawable.cell_shape);

                    TextView rowJoinPeople = new TextView(MainActivity.this);
                    rowJoinPeople.setLayoutParams(lp);
                    rowJoinPeople.setText(join);
                    rowJoinPeople.setBackgroundResource(R.drawable.cell_shape);

                    Button rowButton = new Button(MainActivity.this);
                    rowButton.setLayoutParams(lp);
                    rowButton.setText(R.string.table_main_join);
                    rowButton.setOnClickListener(new View.OnClickListener() {
                        private int thisPort = Integer.parseInt(port);

                        public void onClick(View v) {
                            // Perform action on click
                            Log.d("Join room", "port:" + thisPort);
                            Intent intent = new Intent(MainActivity.this, RoomActivity.class);
                            intent.putExtra("port", thisPort);
                            startActivity(intent);
                        }
                    });

                    Button rowButtonTable = new Button(MainActivity.this);
                    rowButtonTable.setLayoutParams(lp);
                    rowButtonTable.setText(R.string.table_main_join);
                    rowButtonTable.setOnClickListener(new View.OnClickListener() {
                        private int thisPort = Integer.parseInt(port);

                        public void onClick(View v) {
                            // Perform action on click
                            Log.d("Join table", "port:" + thisPort);
                            Intent intent = new Intent(MainActivity.this, TableActivity.class);
                            intent.putExtra("port", thisPort);
                            startActivity(intent);
                        }
                    });


                    tableRow.addView(rowID);
                    tableRow.addView(rowName);
                    tableRow.addView(rowJoinPeople);
                    tableRow.addView(rowButton);
                    tableRow.addView(rowButtonTable);

                    table.addView(tableRow);

                }
            }

        });
    }


    /**
     * Use this function to make room
     */
    private void runMakeRoom() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(
                R.layout.alert_dialog_text_entry, null);
        new AlertDialog.Builder(MainActivity.this)
                // .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle(R.string.make_room_title)
                .setView(textEntryView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        EditText text = (EditText) textEntryView
                                .findViewById(R.id.code_input);
                        String code = text.getText().toString();
                        Context context = getApplicationContext();

                        socket.makeRoom(code);
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

								/* User clicked cancel so do some stuff */
                            }
                        }).create().show();

    }


}
