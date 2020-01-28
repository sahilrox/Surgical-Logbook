package com.example.surgicallogbook;

import android.content.Context;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.LogsViewHolder> implements Filterable {

    Context context;
    ArrayList<Logs> logslist;
    ArrayList<Logs> logslistFull;

    private OnItemClickListener listener;

    RecyclerViewAdapter(Context c, ArrayList<Logs> logslist) {
        context = c;
        this.logslist = logslist;
        logslistFull = new ArrayList<>(logslist);
    }

    public void filterList(ArrayList<Logs> filteredList) {
        logslist = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Logs> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0) {
                filteredList.addAll(logslistFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Logs item : logslistFull) {
                    if(item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }

                    if(item.getProcedure().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }

                    if(item.getDate().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            logslist.clear();
            logslist.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

    public  class LogsViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        TextView name, age, date, procedure;


        public LogsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.patientNameCard);
            age = itemView.findViewById(R.id.patientAgeCard);
            date = itemView.findViewById(R.id.dateCard);
            procedure = itemView.findViewById(R.id.patientProcedureCard);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem delete = menu.add(Menu.NONE,1,1,"Delete");
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(listener != null) {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            listener.onDeleteClick(position);
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public LogsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.log_item,viewGroup,false);
        LogsViewHolder lvh = new LogsViewHolder(view);
        return lvh;
    }


    @Override
    public void onBindViewHolder(@NonNull LogsViewHolder logsViewHolder, int i) {
        logsViewHolder.name.setText(logslist.get(i).getName());
        logsViewHolder.age.setText(logslist.get(i).getAge());
        logsViewHolder.date.setText(logslist.get(i).getDate());
        logsViewHolder.procedure.setText(logslist.get(i).getProcedure());
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return logslist.size();
    }
}
