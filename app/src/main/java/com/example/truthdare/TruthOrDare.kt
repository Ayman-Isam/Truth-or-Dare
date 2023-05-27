package com.example.truthdare

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Entity(tableName = "truth_or_dare")
data class TruthOrDare(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "is_truth") val isTruth: Boolean
)

@Dao
interface TruthOrDareDao {
    @Query("SELECT * FROM truth_or_dare WHERE is_truth = 1")
    fun getTruths(): List<TruthOrDare>

    @Query("SELECT * FROM truth_or_dare WHERE is_truth = 0")
    fun getDares(): List<TruthOrDare>

    @Insert
    fun insert(truthOrDare: TruthOrDare)

    @Update
    fun update(truthOrDare: TruthOrDare)

    @Delete
    fun delete(truthOrDare: TruthOrDare)
}

@Database(entities = [TruthOrDare::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun truthOrDareDao(): TruthOrDareDao
}

class TruthOrDareRepository(private val context: Context) {
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "truth-or-dare"
    ).build()

    suspend fun getTruths(): List<TruthOrDare> = withContext(Dispatchers.IO) {
        db.truthOrDareDao().getTruths()
    }

    suspend fun getDares(): List<TruthOrDare> = withContext(Dispatchers.IO) {
        db.truthOrDareDao().getDares()
    }

    suspend fun insert(truthOrDare: TruthOrDare) = withContext(Dispatchers.IO) {
        db.truthOrDareDao().insert(truthOrDare)
    }
    suspend fun update(truthOrDare: TruthOrDare) = withContext(Dispatchers.IO) {
        db.truthOrDareDao().update(truthOrDare)
    }

    // Add the delete function
    suspend fun delete(truthOrDare: TruthOrDare) = withContext(Dispatchers.IO) {
        db.truthOrDareDao().delete(truthOrDare)
    }
}
