package com.example.notes.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.Model.Note;
import com.example.notes.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Context mContext;
    private List<Note> NoteList = new ArrayList<>();


    NoteAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.note_item, viewGroup, false);

        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder viewHolder, int i) {

        viewHolder.Title.setText(NoteList.get(i).getTitle());
        viewHolder.Desc.setText(NoteList.get(i).getDescription());
//        viewHolder.Priority.setText(String.valueOf(NoteList.get(i).getPriority()));
    }

    @Override
    public int getItemCount() {
        return NoteList.size();
    }

    public void setList(List<Note> NoteList) {
        this.NoteList = NoteList;
        notifyDataSetChanged();
    }

    public Note getNotePostion(int postion) {

        return NoteList.get(postion);
    }

    public void onItemMove(int fromPos, int toPos) {

        if (fromPos < toPos) {
            for (int i = fromPos; i < toPos; i++) {
                Collections.swap(NoteList, i, i + 1);
            }
        } else {
            for (int i = fromPos; i > toPos; i--) {
                Collections.swap(NoteList, i, i - 1);
            }
        }
        notifyItemMoved(fromPos, toPos);
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView Title, Desc;
        public NoteViewHolder(View view) {
            super(view);
            Title = view.findViewById(R.id.NoteTitle);
            Desc = view.findViewById(R.id.NoteDesc);
//            Priority = view.findViewById(R.id.NotePriority);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int pos = getAdapterPosition();
                    Intent i = new Intent(mContext, AddActivity.class);
                    i.putExtra("NoteID", NoteList.get(pos).getId());
                    i.putExtra("title", Title.getText());
                    i.putExtra("desc", Desc.getText());
//                    i.putExtra("priority", NoteList.get(pos).getPriority());
                    mContext.startActivity(i);
                }
            });
        }
    }
}