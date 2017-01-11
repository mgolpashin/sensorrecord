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
import de.martingolpashin.sensorrecord.models.FileStatus;

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
        public FileStatus status;

        public ViewHolder(RelativeLayout layout, TextView fileName, ImageButton deleteButton, FileStatus status) {
            super(layout);
            this.layout = layout;
            this.fileName = fileName;
            this.deleteButton = deleteButton;
            this.status = status;
        }
    }

    public void add(File file) {
        this.files.add(0, file);
        //notifyItemInserted(1);
        notifyDataSetChanged();
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
        ViewHolder vh = new ViewHolder(layout, fileName, deleteButton, null);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final File file = files.get(position);
        holder.fileName.setText(file.getName());
        if(file.isDirectory()) {
            configureDirectory(holder, file, position);
        } else {
            configureFile(holder, file, position);
        }
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    private void configureDirectory(final ViewHolder holder, final File dir, final int position) {
        holder.status = holder.status == FileStatus.DIR_OPENED ? FileStatus.DIR_OPENED : FileStatus.DIR_CLOSED;

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.status == FileStatus.DIR_CLOSED) {
                    openDir(holder, dir, position);
                } else if(holder.status == FileStatus.DIR_OPENED) {
                    closeDir(holder, dir, position);
                }

            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        //TODO change alert icon
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Folder")
                        .setMessage("Are you sure you want to delete " + dir.getName() + " ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(holder.status == FileStatus.DIR_OPENED) {
                                    closeDir(holder, dir, position);
                                }

                                for(File file : dir.listFiles()) {
                                    if(!file.delete()) {
                                        return;
                                    }
                                }
                                if(dir.delete()) {
                                    files.remove(position);
                                    //notifyItemRemoved(position);
                                    notifyDataSetChanged();

                                }
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    private void openDir(ViewHolder holder, File dir, int position) {
        for(File f : dir.listFiles()) {
            files.add(position + 1, f);
            notifyItemInserted(position + 1);
        }
        holder.status = FileStatus.DIR_OPENED;
    }

    private void closeDir(ViewHolder holder, File dir, int position) {
        for(File f : dir.listFiles()) {
            int index = files.indexOf(f);
            files.remove(index);
            notifyItemRemoved(index);
        }

        holder.status = FileStatus.DIR_CLOSED;
    }

    private void configureFile(final ViewHolder holder, final File file, final int position) {
        if(holder.status == null) {
            holder.status = FileStatus.FILE;
        }

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
                        //TODO change alert icon
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete File")
                        .setMessage("Are you sure you want to delete " + file.getName() + " ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(file.delete()) {
                                    files.remove(position);
                                    notifyItemRemoved(position);
                                }
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }
}
