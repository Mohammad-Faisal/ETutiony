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

public class TutionyListAdapter extends RecyclerView.Adapter<TutionyListAdapter.TutionyListViewHolder> {

    ArrayList<TutionyItem> list;
    Context context;
    Activity activity;


    public TutionyListAdapter(ArrayList<TutionyItem> tutionyList, Context context , Activity activity) {
        this.list = tutionyList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TutionyListAdapter.TutionyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutiony, parent , false);
        return new TutionyListAdapter.TutionyListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TutionyListAdapter.TutionyListViewHolder holder, int position) {
        TutionyItem item = list.get(position);

        holder.description.setText(item.getDescription());
        int remainingDays = item.getCycle() - item.getCurrent_days();
        holder.days.setText(remainingDays + " days left");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TutionyListViewHolder extends RecyclerView.ViewHolder {
        TextView description , days;

        public TutionyListViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.item_tutiony_description);
            days = itemView.findViewById(R.id.item_tutiony_days);
        }
    }
}
