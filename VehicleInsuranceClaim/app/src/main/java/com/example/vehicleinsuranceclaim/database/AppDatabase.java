package com.example.vehicleinsuranceclaim.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.vehicleinsuranceclaim.dao.AttachPolicyDao;
import com.example.vehicleinsuranceclaim.dao.ClaimImageDao;
import com.example.vehicleinsuranceclaim.dao.ClaimVideoDao;
import com.example.vehicleinsuranceclaim.dao.UserDao;
import com.example.vehicleinsuranceclaim.entity.AttachPolicy;
import com.example.vehicleinsuranceclaim.entity.ClaimImage;
import com.example.vehicleinsuranceclaim.entity.ClaimVideo;
import com.example.vehicleinsuranceclaim.entity.User;

@Database(entities = {User.class, AttachPolicy.class, ClaimImage.class, ClaimVideo.class}, version = 10)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract UserDao userDao();

    public abstract AttachPolicyDao attachPolicyDao();

    public abstract ClaimImageDao claimImageDao();

    public abstract ClaimVideoDao claimVideoDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }

}
