package ua.com.webacademy.beginnerslection11;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog mDialog;

    private SaveTask mSaveTask;
    private GetTask mGetTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mSaveTask != null) {
            mSaveTask.cancel(true);
        }
        if (mGetTask != null) {
            mGetTask.cancel(true);
        }
    }

    public void OnClick(View v) {
        final Student student;

        switch (v.getId()) {
            case R.id.button:
                mDialog = new ProgressDialog(this);
                mDialog.setMessage("Wait...");
                mDialog.setCancelable(false);
                mDialog.show();

                student = new Student("Ivan", "Ivanov", 22);
                StudentIntentService.insertStudent(this, student);
                break;
            case R.id.button2:
                mDialog = new ProgressDialog(this);
                mDialog.setMessage("Wait...");
                mDialog.setCancelable(false);
                mDialog.show();

                StudentIntentService.getStudent(this, 1);
                break;
            case R.id.button3: {
                ServiceConnection connection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        StudentService studentService = ((StudentService.ServiceBinder) iBinder).getService();

                        long id = studentService.saveStudent(new Student("Ivan", "Ivanov", 22));
                        Toast.makeText(MainActivity.this, "id:" + id, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {

                    }
                };

                Intent intent = new Intent(this, StudentService.class);
                bindService(intent, connection, BIND_AUTO_CREATE);
            }
            break;
            case R.id.button4: {
                ServiceConnection connection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        StudentService studentService = ((StudentService.ServiceBinder) iBinder).getService();

                        Student student = studentService.getStudent(1);
                        Toast.makeText(MainActivity.this, student.FirstName, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {

                    }
                };

                Intent intent = new Intent(this, StudentService.class);
                bindService(intent, connection, BIND_AUTO_CREATE);
            }
            case R.id.button5:
                student = new Student("Ivan", "Ivanov", 22);

                mSaveTask = new SaveTask();
                mSaveTask.execute(student);
                break;
            case R.id.button6:
                mGetTask = new GetTask();
                mGetTask.execute(1l);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == StudentIntentService.REQUEST_CODE_SAVE_STUDENT) {
                long id = data.getLongExtra(StudentIntentService.EXTRA_ID, 0);
                Toast.makeText(this, "id:" + id, Toast.LENGTH_SHORT).show();
            } else if (requestCode == StudentIntentService.REQUEST_CODE_GET_STUDENT) {
                Student student = data.getParcelableExtra(StudentIntentService.EXTRA_STUDENT);
                Toast.makeText(this, student.FirstName, Toast.LENGTH_SHORT).show();
            }
        }

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }


    class SaveTask extends AsyncTask<Student, Void, Long> {

        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Long doInBackground(Student... params) {
            long id = 0;

            try {
                Student student = params[0];

                DataBaseHelper helper = new DataBaseHelper(MainActivity.this);
                id = helper.insertStudent(student);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return id;
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);

            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            Toast.makeText(MainActivity.this, "id:" + result, Toast.LENGTH_SHORT).show();
        }
    }

    class GetTask extends AsyncTask<Long, Void, Student> {

        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Student doInBackground(Long... params) {
            Student student = null;

            try {
                long id = params[0];

                DataBaseHelper helper = new DataBaseHelper(MainActivity.this);
                student = helper.getStudent(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return student;
        }

        @Override
        protected void onPostExecute(Student result) {
            super.onPostExecute(result);

            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            if (result == null) {
                Toast.makeText(MainActivity.this, "Student not found", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, result.FirstName, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
