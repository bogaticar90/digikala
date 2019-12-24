package Woo.Repository;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.database.sqlite.SQLiteDatabase;

import com.example.digikala.R;
import com.example.digikala.model.productsModels.DaoMaster;
import com.example.digikala.model.productsModels.DaoSession;

public class DBApplication extends Application {


    private static DBApplication application;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        OpenHelper daoOpenHelper = new OpenHelper(this, "digikala.db");
        SQLiteDatabase database = daoOpenHelper.getWritableDatabase();
        daoSession = new DaoMaster(database).newSession();

        application = this;
    }

    public static DBApplication getInstance() {
        return application;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String id = getString(R.string.channel_id);
            String name = getString(R.string.channel_name);
            NotificationChannel channel = new NotificationChannel(
                    id,
                    name,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(getString(R.string.channel_description));

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
