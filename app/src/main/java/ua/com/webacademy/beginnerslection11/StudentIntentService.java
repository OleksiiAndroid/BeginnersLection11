package ua.com.webacademy.beginnerslection11;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;

public class StudentIntentService extends IntentService {
    public static final String ACTION_SAVE_STUDENT = "ua.com.webacademy.beginnerslection12.action.SAVE_STUDENT";
    public static final String ACTION_GET_STUDENT = "ua.com.webacademy.beginnerslection12.action.GET_STUDENT";

    public static final String EXTRA_STUDENT = "ua.com.webacademy.beginnerslection12.extra.STUDENT";
    public static final String EXTRA_ID = "ua.com.webacademy.beginnerslection12.extra.ID";
    public static final String EXTRA_PENDING_INTENT = "ua.com.webacademy.beginnerslection12.extra.PENDING_INTENT";

    public static final int REQUEST_CODE_SAVE_STUDENT = 1;
    public static final int REQUEST_CODE_GET_STUDENT = 2;

    public static void insertStudent(Activity activity, Student student) {
        Intent intent = new Intent(activity, StudentIntentService.class);

        PendingIntent pendingIntent = activity.createPendingResult(REQUEST_CODE_SAVE_STUDENT, intent, 0);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);

        intent.setAction(ACTION_SAVE_STUDENT);
        intent.putExtra(EXTRA_STUDENT, student);

        activity.startService(intent);
    }

    public static void getStudent(Activity activity, long id) {
        Intent intent = new Intent(activity, StudentIntentService.class);

        PendingIntent pendingIntent = activity.createPendingResult(REQUEST_CODE_GET_STUDENT, intent, 0);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);

        intent.setAction(ACTION_GET_STUDENT);
        intent.putExtra(EXTRA_ID, id);

        activity.startService(intent);
    }

    public StudentIntentService() {
        super("StudentIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

            PendingIntent pendingIntent = intent.getParcelableExtra(EXTRA_PENDING_INTENT);
            Intent resultIntent = new Intent();

            final String action = intent.getAction();
            if (ACTION_SAVE_STUDENT.equals(action)) {
                Student student = intent.getParcelableExtra(EXTRA_STUDENT);
                long id = helper.insertStudent(student);

                resultIntent.putExtra(EXTRA_ID, id);
            } else if (ACTION_GET_STUDENT.equals(action)) {
                long id = intent.getLongExtra(EXTRA_ID, 0);
                Student student = helper.getStudent(id);

                resultIntent.putExtra(EXTRA_STUDENT, student);
            }

            try {
                pendingIntent.send(this, Activity.RESULT_OK, resultIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
