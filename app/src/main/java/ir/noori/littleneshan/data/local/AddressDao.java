package ir.noori.littleneshan.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ir.noori.littleneshan.data.local.entity.AddressEntity;

@Dao
public interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAddress(AddressEntity address);

    @Query("SELECT * FROM tbl_address ORDER BY id DESC")
    LiveData<List<AddressEntity>> getAllAddresses();

    @Query("DELETE FROM tbl_address")
    void clearHistory();
}
