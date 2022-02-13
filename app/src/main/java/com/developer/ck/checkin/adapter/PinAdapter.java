package com.developer.ck.checkin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.developer.ck.checkin.R;
import com.developer.ck.checkin.activity.PasswordInputActivity;
import com.developer.ck.checkin.model.KeyboardModel;
import com.developer.ck.checkin.model.PinModel;

import java.util.ArrayList;

public class PinAdapter extends RecyclerView.Adapter<PinAdapter.PinViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<PinModel> pinList;
    Context ctx;
    int width;

    public PinAdapter(Context ctx, ArrayList<PinModel> pinList, int width){
        inflater = LayoutInflater.from(ctx);
        this.pinList = pinList;
        this.width = width;
        this.ctx = ctx;
    }

    @Override
    public PinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_pin, parent, false);
        view.getLayoutParams().width = width;
        PinViewHolder holder = new PinViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PinViewHolder holder, int position) {
        PinModel model = pinList.get(position);
        String value = model.getValue();
        if(model.isStatus()) {
            holder.txtActive.setVisibility(View.VISIBLE);
            holder.txtPin.setVisibility(View.GONE);
        } else {
            holder.txtActive.setVisibility(View.GONE);
            holder.txtPin.setVisibility(View.VISIBLE);
            holder.txtPin.setText(value);
        }



    }



    @Override
    public int getItemCount() {
        return pinList.size();
    }

    class PinViewHolder extends RecyclerView.ViewHolder{

        TextView txtPin;
        TextView txtActive;

        public PinViewHolder(View itemView) {
            super(itemView);
            txtPin = (TextView) itemView.findViewById(R.id.txt_pin);
            txtActive = (TextView) itemView.findViewById(R.id.txt_active);
        }

    }
}
