package com.example.heejack.androidassign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


public class MainActivity extends AppCompatActivity {
    private static final int REQ_ADD = 687;
    private static final int REQ_LOGIN = 687;
    public static DBHelper dbHelper;
    FloatingActionButton floatingActionButton;
    SharedPreferences myInfo;
    private ListView listViewLecture, listViewMyLecture;
    @Nullable MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuItem = null;

        myInfo = getSharedPreferences("myInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = myInfo.edit();



        //ture : 로그인 되있음 false : 로그인 안되있음


        listViewLecture = (ListView)findViewById(R.id.listViewAllLecture);


        if (dbHelper == null) {
            dbHelper = new DBHelper(getApplicationContext(), "LECTURE", null, 1);
            Excel();
//            Toast.makeText(this, "DB생성 성공", Toast.LENGTH_SHORT).show();
        }

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, REQ_ADD);
            }
        });


    }

    public void Excel() {
        try{
            String data = "";
            int row=0, column=0;
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("lecutre.xls");
            Workbook workbook = Workbook.getWorkbook(inputStream);

            Sheet sh = workbook.getSheet("Sheet1");
            row = sh.getRows();
            column = sh.getColumns();

            for(int r=0;r<row;r++){
                for(int c=0; c<column;c++){
                    Cell cell = sh.getCell(c,r);
                    data = data+" / "+cell.getContents();
                }
                data= data+"\n";
            }

            Toast.makeText(this, "성공"+data, Toast.LENGTH_SHORT).show();


        }catch (Exception e1){

        }




//        Workbook workbook = null;
//        Sheet sheet = null;
//        try {
//            InputStream inputStream = getBaseContext().getResources().getAssets().open("lecture.xlsx");
//            workbook = Workbook.getWorkbook(inputStream);
//            sheet = workbook.getSheet(0);
//            int MaxColumn = 10, RowStart = 1, RowEnd = sheet.getColumn(1).length - 1, ColumnStart = 0, ColumnEnd = sheet.getRow(1).length;
//
//            String[] strings = new String[10];
//            for (int row = RowStart; row <= RowEnd; row++) {
//                for (int Column = ColumnStart; Column <= ColumnEnd; Column++) {
//                    Log.i("DBHelper", ""+row+", "+Column);
//                    strings[Column] = sheet.getCell(row, Column).getContents();
//                }
//                Lecture lecture = new Lecture(strings);
//                dbHelper.add(lecture);
//            }
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        } catch (BiffException e2) {
//            e2.printStackTrace();
//        } finally {
//
//        }


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if(menuItem != null) {
            if (myInfo.getBoolean("LOGINCHK", false)) {
                menuItem.setTitle("내정보");
            } else {
                menuItem.setTitle("로그인");
            }
        }

        //여기서 사용자의 시간표를 읽어와서 표시해줘야함

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menuItem = menu.getItem(0);
        Log.i("Menu", ""+menuItem.getTitle());

        if(myInfo.getBoolean("LOGINCHK", false)) {
            menuItem.setTitle("내정보");
        }
        else{
            menuItem.setTitle("로그인");
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuProfile:
                //프로필 눌렀을때 로그인 중이면 내정보 화면으로 이동
                if(myInfo.getBoolean("LOGINCHK", false)) {
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }


        return super.onOptionsItemSelected(item);
    }


}
