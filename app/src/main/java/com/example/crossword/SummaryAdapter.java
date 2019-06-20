package com.example.crossword;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.crossword.models.Synonym;

import java.util.ArrayList;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder> {
    private ArrayList<Synonym> synonyms;
    private RecyclerView rv;
    private MainActivity activity;

    public SummaryAdapter(ArrayList<Synonym> synonyms , MainActivity activity)
    {
        this.synonyms=synonyms;
        this.activity = activity;

    }

    @NonNull
    @Override
    public SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_summary, parent, false);
        rv = (RecyclerView) parent;
        return new SummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SummaryViewHolder summaryViewHolder, int i) {
        final Synonym synonym = synonyms.get(i);

        summaryViewHolder.key1.setText(synonym.getKey1());
        summaryViewHolder.key2.setText(synonym.getKey2());
        summaryViewHolder.description.setText(synonym.getDescription());
    }

    @Override
    public int getItemCount() {
        return synonyms.size();
    }

    public class SummaryViewHolder extends RecyclerView.ViewHolder{
        public TextView key1;
        public TextView key2;
        public TextView description;
        public SummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            key1 = (TextView) itemView.findViewById(R.id.key1);
            key2 = (TextView) itemView.findViewById(R.id.key2);
            description = (TextView) itemView.findViewById(R.id.description);
        }

    }


}

