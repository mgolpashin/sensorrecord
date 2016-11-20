package de.martingolpashin.sensorrecord.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import de.martingolpashin.sensorrecord.R;

/**
 * Created by martin on 13.11.16.
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder>{
    private List<File> files;

    //TODO Add sensor icon

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

    public void add(File file) {
        this.files.add(0, file);
        notifyItemChanged(0);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FileAdapter(List<File> files) {
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

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final File file = files.get(position);

        holder.fileName.setText(file.getName());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
                openFileIntent.setData(Uri.fromFile(file));
                v.getContext().startActivity(openFileIntent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Delete File")
                    .setMessage("Are you sure you want to delete " + file.getName() + " ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(file.delete()) {
                                    files.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, files.size());
                                }
                            }

                    })
                    .setNegativeButton("No", null)
                    .show();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return files.size();
    }

}
