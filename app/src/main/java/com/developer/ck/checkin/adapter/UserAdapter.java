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
import com.developer.ck.checkin.activity.UserModifyActivity;
import com.developer.ck.checkin.model.PinModel;
import com.developer.ck.checkin.model.UserInfoModel;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {


    private LayoutInflater inflater;
    private ArrayList<UserInfoModel> userList;
    Context ctx;

    public UserAdapter(Context ctx, ArrayList<UserInfoModel> userList){
        inflater = LayoutInflater.from(ctx);
        this.userList = userList;
        this.ctx = ctx;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_user, parent, false);
        UserViewHolder holder = new UserViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserInfoModel model = userList.get(position);
        holder.txtUserName.setText(model.getName());
        if(model.isLeader()) {
            holder.txtLeader.setVisibility(View.VISIBLE);
        } else {
            holder.txtLeader.setVisibility(View.GONE);
        }
        holder.txtDate.setText(model.getUpdateDate());
        holder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ctx instanceof UserModifyActivity) {
                    ((UserModifyActivity) ctx).modifyUser(model);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        TextView txtUserName;
        TextView txtLeader;
        TextView txtDate;
        LinearLayout modify;

        public UserViewHolder(View itemView) {
            super(itemView);
            txtUserName = (TextView) itemView.findViewById(R.id.txt_name);
            txtLeader = (TextView) itemView.findViewById(R.id.txt_leader);
            txtDate = (TextView) itemView.findViewById(R.id.txt_date);
            modify = (LinearLayout) itemView.findViewById(R.id.modify);
        }

    }
}
