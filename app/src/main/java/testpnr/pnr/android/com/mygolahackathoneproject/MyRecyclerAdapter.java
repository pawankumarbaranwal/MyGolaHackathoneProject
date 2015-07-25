package testpnr.pnr.android.com.mygolahackathoneproject;

/**
 * Created by bhargavsarvepalli on 26/07/15.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.CustomViewHolder> {
    private List<Room> roomList;
    private Context mContext;

    public MyRecyclerAdapter(Context context, List<Room> feedItemList) {
        this.roomList = feedItemList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        Room feedItem = roomList.get(i);
        //Setting text view title
        customViewHolder.textView.setText(feedItem.getTitle());
        customViewHolder.price.setText(String.valueOf(feedItem.getPrice()));
    }

    @Override
    public int getItemCount() {
        return (null != roomList ? roomList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView textView;
        protected TextView price;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
            this.textView = (TextView) view.findViewById(R.id.title);
            this.price = (TextView) view.findViewById(R.id.price);

        }
    }
}