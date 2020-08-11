package com.bazukaa.secured.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bazukaa.secured.R;
import com.bazukaa.secured.models.PasswordDetails;

import java.util.ArrayList;
import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.PasswordHolder> {

    private List<PasswordDetails> passwordDetails = new ArrayList<>();

    @NonNull
    @Override
    public PasswordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PasswordHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pwd_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordHolder holder, int position) {
        PasswordDetails currPwd = passwordDetails.get(position);

        holder.titleTv.setText(currPwd.getTitle());

    }

    @Override
    public int getItemCount() {
        return passwordDetails.size();
    }

    public class PasswordHolder extends RecyclerView.ViewHolder {
        private TextView titleTv, timeTv, descTv, pwdTv;
        private Button arrDownBtn, deleteBtn;
        private CardView pwdCard;
        private RelativeLayout expandableView;


        public PasswordHolder(@NonNull View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.card_pwd_tv_title);
            timeTv = itemView.findViewById(R.id.card_pwd_tv_time);
            descTv = itemView.findViewById(R.id.card_pwd_ev_tv_desc);
            pwdTv = itemView.findViewById(R.id.card_pwd_ev_tv_pwd);
            arrDownBtn = itemView.findViewById(R.id.card_pwd_btn_down);
            deleteBtn = itemView.findViewById(R.id.card_pwd_ev_btn_pwd_dlt);
            pwdCard = itemView.findViewById(R.id.card_pwd_cv_pwdCard);
            expandableView = itemView.findViewById(R.id.expandable_layout);
        }
    }
}
