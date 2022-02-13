package com.developer.ck.checkin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.developer.ck.checkin.R;
import com.developer.ck.checkin.activity.PasswordInputActivity;
import com.developer.ck.checkin.model.KeyboardModel;

import java.util.ArrayList;

public class KeyboardAdapter extends RecyclerView.Adapter<KeyboardAdapter.KeyboardViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<KeyboardModel> keyboardList;
    Context ctx;

    public KeyboardAdapter(Context ctx, ArrayList<KeyboardModel> keyboardList){
        inflater = LayoutInflater.from(ctx);
        this.keyboardList = keyboardList;
        this.ctx = ctx;
    }

    @Override
    public KeyboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_keyboard, parent, false);
        KeyboardViewHolder holder = new KeyboardViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(KeyboardViewHolder holder, int position) {
        KeyboardModel model = keyboardList.get(position);
        int value = model.getValue();
        if(value != -1) {
            holder.txtKeyboard.setText(String.valueOf(value));
            holder.txtKeyboard.setVisibility(View.VISIBLE);
            holder.linearBackSpace.setVisibility(View.GONE);
        } else {
            holder.txtKeyboard.setVisibility(View.GONE);
            holder.linearBackSpace.setVisibility(View.VISIBLE);
        }

        holder.containerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ctx instanceof PasswordInputActivity) {
                    ((PasswordInputActivity) ctx).clickKeyboard(value);
                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return keyboardList.size();
    }

    class KeyboardViewHolder extends RecyclerView.ViewHolder{

        TextView txtKeyboard;
        LinearLayout linearBackSpace;
        RelativeLayout containerView;

        public KeyboardViewHolder(View itemView) {
            super(itemView);
            txtKeyboard = (TextView) itemView.findViewById(R.id.txt_keyboard);
            linearBackSpace = (LinearLayout) itemView.findViewById(R.id.linear_backspace);
            containerView = (RelativeLayout) itemView.findViewById(R.id.container_view);
        }

    }
}
