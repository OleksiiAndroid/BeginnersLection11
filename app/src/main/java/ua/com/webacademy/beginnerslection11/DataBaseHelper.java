package ua.com.webacademy.beginnerslection11;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(Context context) {
        super(context, "MyDB12.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Student.TABLE_NAME + " ("
                + Student.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Student.COLUMN_FIRST_NAME + " TEXT NOT NULL,"
                + Student.COLUMN_LAST_NAME + " TEXT NOT NULL,"
                + Student.COLUMN_AGE + " INTEGER NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertStudent(Student student) {
        long id = 0;
        SQLiteDatabase db = getWritableDatabase();

        try {
            ContentValues values = new ContentValues();

            values.put(Student.COLUMN_FIRST_NAME, student.FirstName);
            values.put(Student.COLUMN_LAST_NAME, student.LastName);
            values.put(Student.COLUMN_AGE, student.Age);

            id = db.insert(Student.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public int updateStudent(Student student) {
        int count = 0;
        SQLiteDatabase db = getWritableDatabase();

        try {
            ContentValues values = new ContentValues();

            values.put(Student.COLUMN_FIRST_NAME, student.FirstName);
            values.put(Student.COLUMN_LAST_NAME, student.LastName);
            values.put(Student.COLUMN_AGE, student.Age);

            count = db.update(Student.TABLE_NAME, values, Student.COLUMN_ID + "=" + student.id, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    public ArrayList<Student> getStudents() {
        ArrayList<Student> students = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.query(Student.TABLE_NAME, null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Student student = new Student();

                    student.id = cursor.getLong(cursor.getColumnIndex(Student.COLUMN_ID));
                    student.FirstName = cursor.getString(cursor.getColumnIndex(Student.COLUMN_FIRST_NAME));
                    student.LastName = cursor.getString(cursor.getColumnIndex(Student.COLUMN_LAST_NAME));
                    student.Age = cursor.getLong(cursor.getColumnIndex(Student.COLUMN_AGE));

                    students.add(student);

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return students;
    }

    public Student getStudent(long id) {
        Student student = null;
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.query(Student.TABLE_NAME, null, Student.COLUMN_ID + "=" + id, null, null, null, null);

            if (cursor.moveToFirst()) {
                student = new Student();

                student.id = cursor.getLong(cursor.getColumnIndex(Student.COLUMN_ID));
                student.FirstName = cursor.getString(cursor.getColumnIndex(Student.COLUMN_FIRST_NAME));
                student.LastName = cursor.getString(cursor.getColumnIndex(Student.COLUMN_LAST_NAME));
                student.Age = cursor.getLong(cursor.getColumnIndex(Student.COLUMN_AGE));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return student;
    }

    public int deleteStudent(long id) {
        int count = 0;
        SQLiteDatabase db = getWritableDatabase();

        try {
            count = db.delete(Student.TABLE_NAME, Student.COLUMN_ID + "=" + id, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }
}
