package com.sample.gs_gram.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sample.gs_gram.Data.SubjectData;
import com.sample.gs_gram.R;

import java.util.ArrayList;

public class SaveAdapter extends RecyclerView.Adapter<SaveAdapter.ViewHolder> {

    private ArrayList<SubjectData> data;
    private OnItemClickListener listener;

    public SaveAdapter(ArrayList<SubjectData> data){
        this.data = data;
    }

    @NonNull
    @Override
    public SaveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_subject_view, parent, false);
        SaveAdapter.ViewHolder viewHolder = new SaveAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SaveAdapter.ViewHolder holder, int position) {
        SubjectData item = data.get(position);

        holder.textview_subject.setText(item.getSubject());
        holder.textview_divition.setText(item.getDivition());
        holder.textview_credit.setText(item.getCredit());

        holder.button_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition(); // 클릭한 아이템의 위치 저장
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClick(data.get(clickedPosition));
                    remove(clickedPosition); // 선택된 항목 삭제
                }
            }
        });

    }
    public void remove(int position){
        try{
            data.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {return (data != null ? data.size() : 0);}

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textview_divition, textview_subject, textview_credit;
        private Button button_choose;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textview_divition = itemView.findViewById(R.id.textview_divition);
            textview_subject = itemView.findViewById(R.id.textview_subject);
            textview_credit = itemView.findViewById(R.id.textview_credit);
            button_choose = itemView.findViewById(R.id.button_choose);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(SubjectData clickedData);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
