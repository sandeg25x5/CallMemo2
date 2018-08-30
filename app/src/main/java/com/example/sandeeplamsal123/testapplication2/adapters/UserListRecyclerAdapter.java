package com.example.sandeeplamsal123.testapplication2.adapters;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.sandeeplamsal123.testapplication2.App;
import com.example.sandeeplamsal123.testapplication2.R;
import com.example.sandeeplamsal123.testapplication2.userdatabases.User;
import com.example.sandeeplamsal123.testapplication2.userdatabases.UserRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserListRecyclerAdapter extends RecyclerView.Adapter<UserListRecyclerAdapter.ViewHolder> {
    private static Context context;
    private List<User> users;
    private LayoutInflater inflater;
    private static UserRepository repository;


    public UserListRecyclerAdapter(Context context, UserRepository repository) {
        this.context = context;
        this.repository = repository;
       // Log.e("test","Hello datas"+repository.getAllUsers().getValue().size());
        inflater = LayoutInflater.from(context);

    }

    class ViewHolder extends RecyclerView.ViewHolder {
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
                            editMemoDialog(context, getLayoutPosition());
                            break;
                        case R.id.delete_memo:
                            App app = new App();
                            repository = new UserRepository(app.getApplication());
                            repository.delete(users.get(getLayoutPosition()).getUserPhoneNumber());
                            notifyDataSetChanged();
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
        holder.toolbar.setTitle(users.get(position).getUserName() + "(" + users.get(position).getUserPhoneNumber() + ")");
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

    public void editMemoDialog(final Context context, final int position) {
        final Dialog editMemoDialog = new Dialog(context);
        editMemoDialog.setContentView(R.layout.edit_memo_dialog);
        final EditText edtEditMemo = editMemoDialog.findViewById(R.id.edt_edit_memo);
        final Button btnEditMemo = editMemoDialog.findViewById(R.id.btn_edit_memo);
        final Button btnCancelMemo = editMemoDialog.findViewById(R.id.btn_cancel_memo);
        editMemoDialog.show();

        btnEditMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newMemo = edtEditMemo.getText().toString();
                String phoneNumber = users.get(position).getUserPhoneNumber();
                String userName = users.get(position).getUserName();
                String currentTime = getCurrentTimeStamp();
                User user = new User(userName, phoneNumber, newMemo, currentTime);
                repository = new UserRepository(new App().getApplication());
                repository.update(user);
                notifyDataSetChanged();
                editMemoDialog.dismiss();
            }
        });

        btnCancelMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMemoDialog.dismiss();
            }
        });
    }

    /**
     * @return yyyy-MM-dd HH:mm:ss formate date as string
     */
    public static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

}
