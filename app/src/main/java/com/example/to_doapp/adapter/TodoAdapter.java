package com.example.to_doapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_doapp.R;
import com.example.to_doapp.activities.AddDataActivity;
import com.example.to_doapp.activities.MainActivity;
import com.example.to_doapp.database.TodoDatabase;
import com.example.to_doapp.entities.Todo;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> implements Filterable {

    private Context context;
    private List<Todo> todoList;
    private List<Todo> todoListFiltered;
    private List<Integer> indexOfCheckedData = new ArrayList<>();

    public TodoAdapter(Context context,List<Todo> todoList) {
        this.context = context;
        this.todoList = todoList;
        this.todoListFiltered = todoList;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TodoViewHolder holder, final int position) {
        holder.setTodo(todoList.get(position));
        if(todoList.get(position).isCheckboxChecked()) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#D3D3D3"));
            holder.checkBox.setChecked(true);
            indexOfCheckedData.add(position);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    todoList.get(position).setCheckboxChecked(true);
                    todoList.add(todoList.get(position));
                    saveToDb(todoList.get(position));
                    todoList.remove(position);
                    holder.checkBox.setChecked(false);
                    notifyDataSetChanged();
                }else {
                    holder.checkBox.setChecked(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoListFiltered.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if(charSequence == null || charSequence.length() == 0){
                    filterResults.count = todoList.size();
                    filterResults.values = todoList;
                }else{
                    List<Todo> resultsModel = new ArrayList<>();
                    String searchStr = charSequence.toString().toLowerCase().trim();
                    for(Todo itemsModel:todoList){
                        if(itemsModel.getTitle().toLowerCase().contains(searchStr)){
                            resultsModel.add(itemsModel);
                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                todoListFiltered.clear();
                todoListFiltered.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    void saveToDb(final Todo todo)
    {
        @SuppressLint("StaticFieldLeak")
        class SaveTodoTask extends AsyncTask<Void, Void, Void>{
            @Override
            protected Void doInBackground(Void... voids) {
                TodoDatabase.getTodoDatabase(context).todoDao().insertTodo(todo);
                return null;
            }

        }
        new SaveTodoTask().execute();
    }

    void deleteToDb(final Todo todo)
    {
        @SuppressLint("StaticFieldLeak")
        class SaveTodoTask extends AsyncTask<Void, Void, Void>{
            @Override
            protected Void doInBackground(Void... voids) {
                TodoDatabase.getTodoDatabase(context).todoDao().deleteUsers(todo);
                return null;
            }
        }
        new SaveTodoTask().execute();
    }


    class TodoViewHolder extends RecyclerView.ViewHolder {

        TextView tvtitle, tvDesc;
        CheckBox checkBox;
        CardView cardView;

        TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvtitle = itemView.findViewById(R.id.tv_title);
            tvDesc = itemView.findViewById(R.id.tv_description);
            checkBox = itemView.findViewById(R.id.checkbox);
            cardView = itemView.findViewById(R.id.cardview);
        }

        void setTodo(Todo todo){
            tvtitle.setText(todo.getTitle());
            tvDesc.setText(todo.getDescription());
        }
    }
}
