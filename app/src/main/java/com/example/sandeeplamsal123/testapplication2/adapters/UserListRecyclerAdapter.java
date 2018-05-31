package com.example.sandeeplamsal123.testapplication2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sandeeplamsal123.testapplication2.R;
import com.example.sandeeplamsal123.testapplication2.userdatabases.User;
import com.example.sandeeplamsal123.testapplication2.userdatabases.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserListRecyclerAdapter extends RecyclerView.Adapter<UserListRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<User> users;
    private LayoutInflater inflater;


    public UserListRecyclerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userPhone, userMemo, userCurrentTime,userOptionView;
        View layOut;

        public ViewHolder(View itemView) {
            super(itemView);
            this.layOut = itemView;
            userName = layOut.findViewById(R.id.user_name_txt);
            userPhone = layOut.findViewById(R.id.user_phone_number_txt);
            userMemo = layOut.findViewById(R.id.user_memo_txt);
            userOptionView=layOut.findViewById(R.id.user_option_view);
            userCurrentTime = layOut.findViewById(R.id.user_current_time_txt);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user_record_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.userName.setText(users.get(position).getUserName());
        holder.userPhone.setText(users.get(position).getUserPhoneNumber());
        holder.userMemo.setText(users.get(position).getUserMemo());
        holder.userCurrentTime.setText(users.get(position).getUserCurrentTime());
    }

    @Override
    public int getItemCount() {
        if (users != null) {
            return users.size();
        } else {
            return 0;
        }
    }


    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

}
