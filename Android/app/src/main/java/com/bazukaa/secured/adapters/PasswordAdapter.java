package com.bazukaa.secured.adapters;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
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
import com.bazukaa.secured.util.TimeFromTimeStamp;
import java.util.ArrayList;
import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.PasswordHolder> {

    private List<PasswordDetails> passwordDetailsList = new ArrayList<>();
    private OnItemClickListener listener;

    public void setPasswords(List<PasswordDetails> passwordDetailsList){
        this.passwordDetailsList = passwordDetailsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PasswordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PasswordHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pwd_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PasswordHolder holder, int position) {
        PasswordDetails currPwd = passwordDetailsList.get(position);

        holder.titleTv.setText(currPwd.getTitle());
        holder.timeTv.setText(TimeFromTimeStamp
                .formatTime(currPwd
                        .getTimeStamp()
                )
        );
        holder.descTv.setText(currPwd.getDetails());
        holder.pwdTv.setText(currPwd.getPassword());
        holder.arrDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.expandableView.getVisibility() == View.GONE){
                    TransitionManager.beginDelayedTransition(holder.pwdCard, new AutoTransition());
                    holder.expandableView.setVisibility(View.VISIBLE);
                    holder.arrDownBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }
                else{
                    TransitionManager.beginDelayedTransition(holder.pwdCard, new AutoTransition());
                    holder.expandableView.setVisibility(View.GONE);
                    holder.arrDownBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return passwordDetailsList.size();
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

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onDeleteButtonClick(position);
                    }
                }
            });
        }
    }

    public PasswordDetails getPasswordDetailsFromPosition(int position){
        return passwordDetailsList.get(position);
    }

    public PasswordDetails getPasswordDetailPosition(int position){
        return passwordDetailsList.get(position);
    }

    public interface OnItemClickListener{
        void onDeleteButtonClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
