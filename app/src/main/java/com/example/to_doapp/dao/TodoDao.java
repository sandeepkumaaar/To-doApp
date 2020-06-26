package com.example.to_doapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.to_doapp.entities.Todo;

import java.util.List;

@Dao
public interface TodoDao {

    @Query("SELECT * FROM todo ORDER BY id DESC")
    List<Todo> getAllTodoList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTodo(Todo todo);

    @Delete
    void deleteUsers(Todo todo);


}