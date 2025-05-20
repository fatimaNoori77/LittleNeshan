package ir.noori.littleneshan.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ir.noori.littleneshan.data.local.entity.AddressEntity;

@Database(entities = {AddressEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AddressDao addressDao();

}
