package de.martingolpashin.sensorrecord.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.martingolpashin.sensorrecord.R;
import de.martingolpashin.sensorrecord.models.FileStatus;

/**
 * Created by martin on 13.11.16.
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder>{
    private List<File> files;
    private ArrayList<File> openDirs = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout layout;
        public ImageView icon;
        public TextView fileName;
        public ImageButton deleteButton;
        public FileStatus status;

        public ViewHolder(RelativeLayout layout, ImageView icon, TextView fileName, ImageButton deleteButton, FileStatus status) {
            super(layout);
            this.layout = layout;
            this.icon = icon;
            this.fileName = fileName;
            this.deleteButton = deleteButton;
            this.status = status;
        }
    }

    public void add(File file) {
        this.files.add(0, file);
        notifyItemInserted(0);
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
        ImageView icon = (ImageView) layout.findViewById(R.id.icon);
        TextView fileName = (TextView) layout.findViewById(R.id.file_name);
        ImageButton deleteButton = (ImageButton) layout.findViewById(R.id.button_delete);
        return new ViewHolder(layout, icon, fileName, deleteButton, null);
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
        holder.status = this.openDirs.contains(dir) ?  FileStatus.DIR_OPENED : FileStatus.DIR_CLOSED;
        holder.icon.setBackgroundColor(holder.layout.getResources().getColor(R.color.material_grey_600));

        if(holder.status == FileStatus.DIR_OPENED) {
            holder.icon.setImageResource(R.drawable.ic_folder_open_white_24dp);
        } else {
            holder.icon.setImageResource(R.drawable.ic_folder_white_24dp);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.status == FileStatus.DIR_CLOSED) {
                    openDir(holder, dir, position);
                } else if(holder.status == FileStatus.DIR_OPENED) {
                    closeDir(holder, dir);
                }

            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .setTitle("Delete Folder")
                        .setMessage("Are you sure you want to delete " + dir.getName() + " ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(holder.status == FileStatus.DIR_OPENED) {
                                    closeDir(holder, dir);
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
            int holderPosition = holder.getAdapterPosition();
            files.add(holderPosition + 1, f);
            notifyItemInserted(holderPosition + 1);
        }
        holder.status = FileStatus.DIR_OPENED;
        this.openDirs.add(dir);
        holder.icon.setImageResource(R.drawable.ic_folder_open_white_24dp);
    }

    private void closeDir(ViewHolder holder, File dir) {
        for(File f : dir.listFiles()) {
            int index = files.indexOf(f);
            files.remove(index);
            notifyItemRemoved(index);
        }

        holder.status = FileStatus.DIR_CLOSED;
        this.openDirs.remove(dir);
        holder.icon.setImageResource(R.drawable.ic_folder_white_24dp);
    }

    private void configureFile(final ViewHolder holder, final File file, final int position) {
        String fileName = holder.fileName.getText().toString();

        if(fileName.contains("Accelerometer")) {
            holder.status = FileStatus.FILE_ACCELEROMETER;
            holder.icon.setImageResource(R.drawable.accelerometer);
            holder.icon.setBackgroundColor(Color.parseColor("#283593"));
        } else if(fileName.contains("Gyroscope")) {
            holder.status = FileStatus.FILE_GYROSCOPE;
            holder.icon.setImageResource(R.drawable.gyroscope);
            holder.icon.setBackgroundColor(Color.parseColor("#c62828"));
        } else if(fileName.contains("Compass")) {
            holder.status = FileStatus.FILE_COMPASS;
            holder.icon.setImageResource(R.drawable.ic_explore_white_24dp);
            holder.icon.setBackgroundColor(Color.parseColor("#ff8f00"));
        } else if(fileName.contains("GPS")) {
            holder.status = FileStatus.FILE_GPS;
            holder.icon.setImageResource(R.drawable.ic_location_on_white_24dp);
            holder.icon.setBackgroundColor(Color.parseColor("#00838f"));
        }

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
                        .setIcon(R.drawable.ic_warning_black_24dp)
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
