package com.example.sandeeplamsal123.testapplication2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


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
        TextView userName, userPhone, userMemo, userCurrentTime, userOptionView;
        Toolbar toolbar;
        View layOut;


        public ViewHolder(View itemView) {
            super(itemView);
            this.layOut = itemView;
            userName = layOut.findViewById(R.id.user_name_txt);
            userPhone = layOut.findViewById(R.id.user_phone_number_txt);
            userMemo = layOut.findViewById(R.id.user_memo_txt);
           // userOptionView = layOut.findViewById(R.id.user_option_view);
            userCurrentTime = layOut.findViewById(R.id.user_current_time_txt);

            toolbar = layOut.findViewById(R.id.card_toolbar);

            toolbar.inflateMenu(R.menu.user_option_menu);

            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    switch (id) {
                        case R.id.edit_memo:

                            break;
                        case R.id.delete_memo:
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
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
        holder.toolbar.setTitle(users.get(position).getUserName()+"("+users.get(position).getUserPhoneNumber()+")");
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
