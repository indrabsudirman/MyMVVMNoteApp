package com.example.mymvvmnoteapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymvvmnoteapp.databinding.NoteItemBinding;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> {

    private OnItemClickListener listener;

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority() == newItem.getPriority();
        }
    };


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteHolder(NoteItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = getItem(position);
        holder.noteItemBinding.textViewTitle.setText(currentNote.getTitle());
        holder.noteItemBinding.textViewDescription.setText(currentNote.getDescription());
        holder.noteItemBinding.textViewPriority.setText(String.valueOf(currentNote.getPriority()));

        holder.noteItemBinding.getRoot().setOnClickListener(view -> {
            int position1 = holder.getAdapterPosition();
            if (listener != null && position1 != RecyclerView.NO_POSITION) {
                listener.onItemClick(getItem(position1));
            }
        });
    }



    public Note getNoteAt(int position) {
        return getItem(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    static class NoteHolder extends RecyclerView.ViewHolder {
        //Using View Binding
        private NoteItemBinding noteItemBinding;


        public NoteHolder(@NonNull NoteItemBinding noteItemBinding) {
            super(noteItemBinding.getRoot());
            //Kalo dihapus bisa NPE TextView (View) di Item RV
            this.noteItemBinding = noteItemBinding;


        }
    }
}
