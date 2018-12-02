package candor.example.com.etutiony;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;



@Dao
public interface ExamDao {

    @Insert
    public void addExam(ExamItem examItem);

    @Query("select * from ExamItem")
    public List<ExamItem> getExams();

    @Delete
    public void deleteExam(ExamItem examItem);

    @Update
    public void updateExam(ExamItem examItem);

}
