package com.clbmdev.noteit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    ArrayList<Note> myNotes;

    ItemClicked activity;

    public interface ItemClicked
    {
        void onItemClicked(int index);
    }

    public NoteAdapter(Context context, ArrayList<Note> myNotes) {
        this.myNotes = myNotes;

        activity = (ItemClicked)context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvNote;
        CardView cvBackground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNote = itemView.findViewById(R.id.tvNote);
            cvBackground = itemView.findViewById(R.id.cvBackground);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onItemClicked( myNotes.indexOf((Note)itemView.getTag()));
                }
            });
        }
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View v = inflater.inflate(R.layout.note_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {

        holder.itemView.setTag(myNotes.get(position));

        holder.tvNote.setText(myNotes.get(position).getNote());

        holder.cvBackground.setCardBackgroundColor(myNotes.get(position).getColor());

    }

    @Override
    public int getItemCount() {
        return myNotes.size();
    }
}
