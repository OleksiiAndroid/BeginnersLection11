package ua.com.webacademy.beginnerslection11;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class StudentService extends Service {

    private ServiceBinder mBinder;

    public StudentService() {
        mBinder = new ServiceBinder();
    }

    public long saveStudent(Student student) {

        return new DataBaseHelper(getApplicationContext()).insertStudent((student));
    }

    public Student getStudent(long id) {
        return new DataBaseHelper(getApplicationContext()).getStudent(id);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    class ServiceBinder extends Binder {
        public StudentService getService() {
            return StudentService.this;
        }
    }
}
