package com.example.to_doapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.to_doapp.R;
import com.example.to_doapp.adapter.TodoAdapter;
import com.example.to_doapp.database.TodoDatabase;
import com.example.to_doapp.entities.Todo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD_TODO = 1;
    Toolbar toolbarMain;
    SearchView searchView;
    Button btnAdd;
    public static List<Todo> todoList;
    List<Todo> tempTodoList;
    List<Todo> temp1TodoList;
    RecyclerView recyclerViewTodo;
    TodoAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbarMain = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbarMain);
        btnAdd = findViewById(R.id.btnAdd);
        searchView = findViewById(R.id.searchview);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddDataActivity.class));
            }
        });

        recyclerViewTodo = findViewById(R.id.recyclerview_todo);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewTodo.setLayoutManager(linearLayoutManager);

        todoList = new ArrayList<>();
        tempTodoList = new ArrayList<>();
        temp1TodoList = new ArrayList<>();

        todoList.clear();
        todoAdapter = new TodoAdapter(this,todoList);
        recyclerViewTodo.setAdapter(todoAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                todoAdapter.getFilter().filter(newText);
                return false;
            }
        });

        getTodoList();
    }

    private void getTodoList() {
        @SuppressLint("StaticFieldLeak")
        class GetTodoTask extends AsyncTask<Void, Void, List<Todo>>{
            @Override
            protected List<Todo> doInBackground(Void... voids) {
                return TodoDatabase
                        .getTodoDatabase(getApplicationContext())
                        .todoDao().getAllTodoList();
            }
            @Override
            protected void onPostExecute(List<Todo> todos) {
                super.onPostExecute(todos);
                if (todoList.size() == 0){
                    todoList.addAll(todos);
                }else {
                    todoList.add(0,todos.get(0));
                }
                for(int i=0;i<todoList.size();i++) {
                    if(todoList.get(i).isCheckboxChecked()) {
                        tempTodoList.add(todoList.get(i));
                    }
                    else {
                        temp1TodoList.add(todoList.get(i));
                    }
                }
                todoList.clear();
                for(int i=0;i<temp1TodoList.size();i++) {
                    todoList.add(temp1TodoList.get(i));
                 }
                for(int i=0;i<tempTodoList.size();i++) {

                    todoList.add(tempTodoList.get(i));

                }
                todoAdapter.notifyDataSetChanged();
                recyclerViewTodo.smoothScrollToPosition(0);
            }
        }
        new GetTodoTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_TODO && resultCode == RESULT_OK){
            getTodoList();
        }
    }
}
