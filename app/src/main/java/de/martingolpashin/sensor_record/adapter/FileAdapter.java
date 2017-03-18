package de.martingolpashin.sensor_record.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
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

import de.martingolpashin.sensor_record.R;
import de.martingolpashin.sensor_record.models.FileStatus;
import de.martingolpashin.sensor_record.utils.FileHandler;

/**
 * Created by martin on 13.11.16.
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder>{
    private List<File> files;
    private ArrayList<File> openDirs = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout layout;
        public ImageView icon;
        public TextView iconText;
        public TextView fileName;
        public ImageButton shareButton;
        public ImageButton deleteButton;
        public FileStatus status;

        public ViewHolder(RelativeLayout layout, ImageView icon, TextView iconText, TextView fileName, ImageButton shareButton, ImageButton deleteButton, FileStatus status) {
            super(layout);
            this.layout = layout;
            this.icon = icon;
            this.iconText = iconText;
            this.fileName = fileName;
            this.shareButton = shareButton;
            this.deleteButton = deleteButton;
            this.status = status;
        }
    }

    public void add(File file) {
        this.files.add(0, file);
        notifyDataSetChanged();
    }

    public FileAdapter(List<File> files) {
        this.files = files;
    }

    @Override
    public FileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_file, parent, false);
        ImageView icon = (ImageView) layout.findViewById(R.id.icon);
        TextView iconText = (TextView) layout.findViewById(R.id.icon_text);
        TextView fileName = (TextView) layout.findViewById(R.id.file_name);
        ImageButton shareButton = (ImageButton) layout.findViewById(R.id.button_share);
        ImageButton deleteButton = (ImageButton) layout.findViewById(R.id.button_delete);
        return new ViewHolder(layout, icon, iconText, fileName, shareButton, deleteButton, null);
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

        holder.iconText.setVisibility(View.GONE);
        holder.icon.setVisibility(View.VISIBLE);

        holder.icon.setBackgroundResource(R.color.material_grey_600);

        if(holder.status == FileStatus.DIR_OPENED) {
            holder.icon.setImageResource(R.drawable.ic_folder_open_white_24dp);
        } else {
            holder.icon.setImageResource(R.drawable.ic_folder_white_24dp);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.status == FileStatus.DIR_CLOSED) {
                    openDir(holder, dir);
                } else if(holder.status == FileStatus.DIR_OPENED) {
                    closeDir(holder, dir);
                }

            }
        });

        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File zipFile = FileHandler.convertToZip(dir);
                startIntent(v.getContext(), zipFile, Intent.ACTION_SEND);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete Folder")
                        .setMessage("Are you sure you want to delete " + dir.getName() + " ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (holder.status == FileStatus.DIR_OPENED) {
                                    closeDir(holder, dir);
                                }

                                for (File file : dir.listFiles()) {
                                    if (!file.delete()) {
                                        return;
                                    }
                                }
                                if (dir.delete()) {
                                    files.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, getItemCount());
                                }
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    private void openDir(ViewHolder holder, File dir) {
        for(File f : dir.listFiles()) {
            int holderPosition = holder.getAdapterPosition();
            files.add(holderPosition + 1, f);
            notifyItemInserted(holderPosition + 1);
            notifyItemRangeChanged(holderPosition + 1, getItemCount());
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
            notifyItemRangeChanged(index, getItemCount());
        }

        holder.status = FileStatus.DIR_CLOSED;
        this.openDirs.remove(dir);
        holder.icon.setImageResource(R.drawable.ic_folder_white_24dp);
    }

    private void configureFile(final ViewHolder holder, final File file, final int position) {
        String fileName = holder.fileName.getText().toString();
        holder.status = FileStatus.FILE;

        holder.iconText.setVisibility(View.VISIBLE);
        holder.icon.setVisibility(View.GONE);

        if (fileName.endsWith(".zip")) {
            holder.iconText.setText("ZIP");
            holder.iconText.setBackgroundResource(R.color.material_brown_400);
        } else if(fileName.contains("Accelerometer") && !fileName.contains("Linear")) {
            holder.iconText.setText("A");
            holder.iconText.setBackgroundResource(R.color.material_red_400);
        } else if(fileName.contains("AccelerometerLinear")) {
            holder.iconText.setText("AL");
            holder.iconText.setBackgroundResource(R.color.material_pink_400);
        } else if(fileName.contains("AmbientTemperature")) {
            holder.iconText.setText("AT");
            holder.iconText.setBackgroundResource(R.color.material_purple_400);
        } else if(fileName.contains("Compass")) {
            holder.iconText.setText("C");
            holder.iconText.setBackgroundResource(R.color.material_deep_purple_400);
        } else if(fileName.contains("GPS")) {
            holder.iconText.setText("GPS");
            holder.iconText.setBackgroundResource(R.color.material_indigo_400);
        } else if(fileName.contains("Gravity")) {
            holder.iconText.setText("Gr");
            holder.iconText.setBackgroundResource(R.color.material_blue_400);
        } else if(fileName.contains("Gyroscope")) {
            holder.iconText.setText("Gy");
            holder.iconText.setBackgroundResource(R.color.material_light_blue_400);
        } else if(fileName.contains("Light")) {
            holder.iconText.setText("L");
            holder.iconText.setBackgroundResource(R.color.material_cyan_400);
        } else if(fileName.contains("Pressure")) {
            holder.iconText.setText("Pre");
            holder.iconText.setBackgroundResource(R.color.material_teal_400);
        } else if(fileName.contains("Proximity")) {
            holder.iconText.setText("Pro");
            holder.iconText.setBackgroundResource(R.color.material_green_400);
        } else if(fileName.contains("RotationVector")) {
            holder.iconText.setText("RV");
            holder.iconText.setBackgroundResource(R.color.material_light_green_400);
        }

        if(holder.status == null) {
            holder.status = FileStatus.FILE;
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(v.getContext(), file, Intent.ACTION_VIEW);
            }
        });

        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(v.getContext(), file, Intent.ACTION_SEND);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete File")
                        .setMessage("Are you sure you want to delete " + file.getName() + " ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (file.delete()) {
                                    files.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, getItemCount());
                                }
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    private void startIntent(Context context, File file, String action) {
        Intent intent = new Intent(action);
        Uri fileUri = FileProvider.getUriForFile(context, "de.martingolpashin.sensor_record.files", file);
        String mime = context.getContentResolver().getType(fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        switch (action) {
            case Intent.ACTION_SEND:
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Sensor Record data");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
                intent.setType(mime);
                break;
            case Intent.ACTION_VIEW:
                intent.setDataAndType(fileUri, mime);
                break;
        }

        context.startActivity(intent);
    }
}
