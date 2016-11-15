package de.martingolpashin.sensorrecord.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import de.martingolpashin.sensorrecord.R;

/**
 * Created by martin on 13.11.16.
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder>{
    private File[] files;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout layout;
        public TextView fileName;
        public ImageButton deleteButton;

        public ViewHolder(RelativeLayout layout, TextView fileName, ImageButton deleteButton) {
            super(layout);
            this.layout = layout;
            this.fileName = fileName;
            this.deleteButton = deleteButton;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FileAdapter(File[] files) {
        this.files = files;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_file, parent, false);
        TextView fileName = (TextView) layout.findViewById(R.id.file_name);
        ImageButton deleteButton = (ImageButton) layout.findViewById(R.id.button_delete);
        ViewHolder vh = new ViewHolder(layout, fileName, deleteButton);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO open file
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO open delete dialog
            }
        });

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.fileName.setText(files[position].getName());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return files.length;
    }
}
