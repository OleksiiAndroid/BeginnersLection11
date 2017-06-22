package ua.com.webacademy.beginnerslection11;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnClick(View v) {
        SharedPreferences preferences;
        SharedPreferences.Editor editor;

        switch (v.getId()) {
            case R.id.button:
                preferences = getPreferences(MODE_PRIVATE);
                editor = preferences.edit();

                editor.putString("Test", "test");

                editor.commit();
                break;
            case R.id.button2:
                preferences = getPreferences(MODE_PRIVATE);
                String text1 = preferences.getString("Test", "");

                Toast.makeText(this, text1, Toast.LENGTH_SHORT).show();
                break;
            case R.id.button3:
                preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                editor = preferences.edit();

                editor.putString("Test", "test2");

                editor.commit();
                break;
            case R.id.button4:
                preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                String text3 = preferences.getString("Test", "");

                Toast.makeText(this, text3, Toast.LENGTH_SHORT).show();
                break;
            case R.id.button5:
                Intent intent = new Intent(this, PreferenceActivity.class);
                startActivity(intent);
                break;
            case R.id.button7:
                preferences = PreferenceManager.getDefaultSharedPreferences(this);

                String text4 = preferences.getString("pref2", "");
                Toast.makeText(this, text4, Toast.LENGTH_SHORT).show();
                break;
            case R.id.button8:
                saveInternalFile("MyFile1.txt", "File data");
                break;
            case R.id.button9:
                String text9 = readInternalFile("MyFile1.txt");
                Toast.makeText(this, text9, Toast.LENGTH_SHORT).show();
                break;
            case R.id.button10:
                if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File folder = Environment.getExternalStorageDirectory();
                        folder = new File(folder.getAbsolutePath() + "/MyFolder");

                        saveExternalFile(folder, "MyFile2.txt", "File data");
                    }
                } else {
                    Toast.makeText(this, "You don't have permission for this", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button11:
                if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File folder = Environment.getExternalStorageDirectory();
                        folder = new File(folder.getAbsolutePath() + "/MyFolder");

                        if (folder.exists()) {
                            String text11 = readExternalFile(folder, "MyFile2.txt");
                            Toast.makeText(this, text11, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(this, "You don't have permission for this", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button12:
                Student student = new Student("Ivan", "Ivanov", 22);

                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(student);

                saveInternalFile("Student.txt", json);
                break;
            case R.id.button13:
                String json2 = readInternalFile("Student.txt");

                Gson gson2 = new GsonBuilder().create();
                Student student2 = gson2.fromJson(json2, Student.class);

                Toast.makeText(this, student2.FirstName, Toast.LENGTH_SHORT).show();
                break;
            case R.id.button14:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String[] permissions = {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    };

                    requestPermissions(permissions, 0);
                } else {
                    Toast.makeText(this, "You can request permissions only in Android 6+", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    private void saveInternalFile(String fileName, String data) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(fileName, Context.MODE_PRIVATE)));

            writer.write(data);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readInternalFile(String fileName) {
        try {
            StringBuilder builder = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput(fileName)));
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            reader.close();

            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void saveExternalFile(File folder, String fileName, String data) {
        try {
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File file = new File(folder, fileName);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));

            writer.write(data);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readExternalFile(File folder, String fileName) {
        File file = new File(folder, fileName);

        try {
            if (file.exists()) {
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                reader.close();

                return builder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
