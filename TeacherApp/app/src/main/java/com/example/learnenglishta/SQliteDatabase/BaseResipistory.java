package com.example.learnenglishta.SQliteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.learnenglishta.Model.Order;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

public class BaseResipistory extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.example.callvideo/databases/";
    private static String DB_NAME = "DCourseDB.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public BaseResipistory(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        Log.e("Path 1", DB_PATH);
    }


    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();

            }
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return myDataBase.query("OrderDetail", null, null, null, null, null, null);
    }


    public void insert(Map<Object, Object> map) {
        openDataBase();
        //  myDataBase=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CourseId", String.valueOf(map.get("courseId")));
        contentValues.put("CourseName", String.valueOf(map.get("courseName")));
        contentValues.put("Price", String.valueOf(map.get("price")));
        contentValues.put("Schedule", String.valueOf(map.get("schedule")));
        contentValues.put("TutorPhone", String.valueOf(map.get("tutorPhone")));

        myDataBase.insert("OrderDetail", null, contentValues);
        close();
    }

    public void insert(Order order) {
        openDataBase();
        //  myDataBase=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CourseId", order.getCourseId());
        contentValues.put("CourseName", order.getCourseName());
        contentValues.put("Price", order.getPrice());
        contentValues.put("Schedule", order.getSchedule());
        contentValues.put("TutorPhone", order.getTutorPhone());
        myDataBase.insert("OrderDetail", null, contentValues);
        close();
    }

    public void cleanCart() {
        openDataBase();
        myDataBase.execSQL("DELETE FROM OrderDetail");
        close();
    }

//    public void update(Order order, String courseName, String courseId, String price, String discount) {
//        openDataBase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("CourseName", courseName);
//        contentValues.put("CourseId", courseId);
//        contentValues.put("Price", price);
//        myDataBase.update("OrderDetail", contentValues, "id = ?", new String[]{String.valueOf(order.getCourseId()) + ""});
//        close();
//
//    }

    public ArrayList<Order> getinform() {
        openDataBase();
        ArrayList<Order> orderList = new ArrayList<>();
        ;
        //database = BaseResipistory.initDatabase(this, "StoreManagement.db");
        Cursor c = myDataBase.rawQuery("SELECT * FROM OrderDetail", null);

        if (c.moveToFirst()) {
            do {
                Order order = new Order();
                order.setCourseId(c.getString(c.getColumnIndex("CourseId")));
                order.setCourseName(c.getString(c.getColumnIndex("CourseName")));
                order.setPrice(c.getString(c.getColumnIndex("Price")));
                order.setSchedule(c.getString(c.getColumnIndex("Schedule")));
                order.setTutorPhone(c.getString(c.getColumnIndex("TutorPhone")));
                //khohang.setId(c.getInt(c.getColumnIndex(Contract.InforrmationTable._ID)));
                orderList.add(order);


            } while (c.moveToNext());
        }


        c.close();
        close();
        return orderList;
    }
//    public ArrayList<Store> getListProduct() {
//        Store student = null;
//        ArrayList<Store> StudentList = new ArrayList<>();
//        openDataBase();
//        Cursor cursor = myDataBase.rawQuery("SELECT * FROM Store", null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            student = new Store(cursor.getInt(0),
//                    cursor.getString(1),
//                    cursor.getInt(2),cursor.getDouble(3));
//            StudentList.add(student);
//            cursor.moveToNext();
//        }
//        cursor.close();
//        close();
//        return StudentList;
//    }
}

