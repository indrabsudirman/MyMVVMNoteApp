package com.example.mymvvmnoteapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymvvmnoteapp.databinding.NoteItemBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder>{

    private List<Note> notes = new ArrayList<>();




    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteHolder(NoteItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = notes.get(position);
        holder.noteItemBinding.textViewTitle.setText(currentNote.getTitle());
        holder.noteItemBinding.textViewDescription.setText(currentNote.getDescription());
        holder.noteItemBinding.textViewPriority.setText(String.valueOf(currentNote.getPriority()));
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public Note getNoteAt(int position) {
        return notes.get(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        //Using View Binding
        private NoteItemBinding noteItemBinding;

        public NoteHolder(@NonNull NoteItemBinding noteItemBinding) {
            super(noteItemBinding.getRoot());

            this.noteItemBinding = noteItemBinding;
        }
    }
}
