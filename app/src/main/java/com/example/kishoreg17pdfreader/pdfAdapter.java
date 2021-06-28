package com.example.kishoreg17pdfreader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class pdfAdapter extends RecyclerView.Adapter<ImageHolder> {

    private Context context;
    private ArrayList<File> fileArrayList;

    public pdfAdapter(Context context, ArrayList<File> fileArrayList) {
        this.context = context;
        this.fileArrayList = fileArrayList;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mview= LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_one_file,parent,false);
        return new ImageHolder(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageHolder holder, int position) {

        if(fileArrayList.size()!=0){

            holder.FileName.setText(fileArrayList.get(position).getName());

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context,PdfViewer.class);
                    intent.putExtra("position",holder.getAdapterPosition());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        }
        else{
            Toast.makeText(context, "No files", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        if(fileArrayList.size() > 0 && fileArrayList != null){
            return fileArrayList.size();
        }
        else{
            return 1;
        }
    }
}
class ImageHolder extends RecyclerView.ViewHolder{

    ImageView PdfIcon;
    TextView FileName;
    CardView cardView;

    public ImageHolder(@NonNull View itemView) {
        super(itemView);

        PdfIcon=itemView.findViewById(R.id.pdf_icon);
        FileName=itemView.findViewById(R.id.pdf_file_name);
        cardView=itemView.findViewById(R.id.single_cardview);
    }
}
