package candor.example.com.etutiony;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExamListAdapter extends RecyclerView.Adapter<ExamListAdapter.TutionyListViewHolder> {

    ArrayList<ExamItem> list;
    Context context;
    Activity activity;


    public ExamListAdapter(ArrayList<ExamItem> tutionyList, Context context , Activity activity) {
        this.list = tutionyList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ExamListAdapter.TutionyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam, parent , false);
        return new ExamListAdapter.TutionyListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamListAdapter.TutionyListViewHolder holder, int position) {
        ExamItem item = list.get(position);
        holder.name.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TutionyListViewHolder extends RecyclerView.ViewHolder {
        TextView name , number;

        public TutionyListViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }
}
