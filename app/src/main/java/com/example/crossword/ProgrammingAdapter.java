package com.example.crossword;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.crossword.helpers.Preferences;
import com.example.crossword.models.Synonym;
import com.example.crossword.utils.Constants;

import java.util.ArrayList;

public class ProgrammingAdapter extends RecyclerView.Adapter<ProgrammingAdapter.ProgrammingViewHolder> {
  private ArrayList<Synonym> synonyms;
  private RecyclerView rv;
  private MainActivity activity;

  public ProgrammingAdapter(ArrayList<Synonym> synonyms , MainActivity activity)
    {
        this.synonyms=synonyms;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ProgrammingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
      rv = (RecyclerView) parent;
      return new ProgrammingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProgrammingViewHolder programmingViewHolder, int i) {

      final Synonym synonym = synonyms.get(i);
      programmingViewHolder.textView.setVisibility(View.VISIBLE);
      programmingViewHolder.textView.setText(synonym.getKey1());
      programmingViewHolder.textView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if(Preferences.hasPreference(Constants.FIRST_ENTRY)){
            TextView firstItemTextView = (TextView) rv.findViewHolderForAdapterPosition(Preferences.getInt(Constants.FIRST_ENTRY_ID, -1)).itemView.findViewById(R.id.tv);
            String secondEntry = programmingViewHolder.textView.getText().toString();
            if(Preferences.getString(Constants.FIRST_ENTRY, "NA").contentEquals(secondEntry)) {
              if (programmingViewHolder.getAdapterPosition() != Preferences.getInt(Constants.FIRST_ENTRY_ID, -1)) {
                  try {
                    programmingViewHolder.textView.setVisibility(View.GONE);
                    firstItemTextView.setVisibility(View.GONE);
                    boolean isAnyViewVisible = false;

                    for (int k=0; k< synonyms.size(); k++ ){
                      TextView tv = (TextView) rv.findViewHolderForAdapterPosition(k).itemView.findViewById(R.id.tv);
                      if(tv.getVisibility() ==  View.VISIBLE){
                        isAnyViewVisible = true;
                        break;
                      }
                    }

                    if(!isAnyViewVisible) {
                      RelativeLayout gameLayout = activity.findViewById(R.id.gameLayout);
                      RelativeLayout summaryLayout = activity.findViewById(R.id.summaryLayout);
                      gameLayout.setVisibility(View.GONE);
                      summaryLayout.setVisibility(View.VISIBLE);

                    }

                  } catch (Exception e) {
                    e.printStackTrace();
                  }
              }
              firstItemTextView.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
              Preferences.removePreference(Constants.FIRST_ENTRY);
              Preferences.removePreference(Constants.FIRST_ENTRY_ID);
            } else{
              firstItemTextView.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
              Preferences.removePreference(Constants.FIRST_ENTRY);
              Preferences.removePreference(Constants.FIRST_ENTRY_ID);
            }
          } else {
            programmingViewHolder.textView.setBackgroundColor(activity.getResources().getColor(R.color.green));
            Preferences.putInt(Constants.FIRST_ENTRY_ID, programmingViewHolder.getAdapterPosition());
            Preferences.putString(Constants.FIRST_ENTRY, synonym.getKey2());
          }
        }
      });

    }

    @Override
    public int getItemCount() {
        return synonyms.size();
    }

    public class ProgrammingViewHolder extends RecyclerView.ViewHolder{
      public TextView textView;
      public ProgrammingViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.tv);
    }
}


}

