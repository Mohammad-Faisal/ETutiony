package candor.example.com.etutiony.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import candor.example.com.etutiony.ExamItem;

@Database(entities = {ExamItem.class } , version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ExamDao examDao();


    private static volatile AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
