package testpnr.pnr.android.com.mygolahackathoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cardView;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;
    private List<Room> roomsList;


    private void initialize() {
//        amenitiesLV=(ListView)findViewById(R.id.lvAmenities);
//        amenitiesLV.setEnabled(false);
//        amenitiesLV.setOnItemClickListener(null);
//        //amenitiesLV.setScrollContainer(false);
//        String[] values = new String[]{"Gym","Free Wifi","Swimming Pool","Basket Ball"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.amenitiesview, values);
//        amenitiesLV.setAdapter(adapter);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new MyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        roomsList = new ArrayList<>();
        Room room1 = new Room();
        room1.setPrice(1000);
        room1.setThumbnail("room1");
        room1.setTitle("Classic Room");

        Room room2 = new Room();
        room1.setPrice(2000);
        room1.setThumbnail("room2");
        room1.setTitle("Premium Room");

        roomsList.add(room1);
        roomsList.add(room2);

        adapter = new MyRecyclerAdapter(this, roomsList);
        mRecyclerView.setAdapter(adapter);

    }
    //ListView amenitiesLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        cardView = (CardView)findViewById(R.id.cv);
        cardView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cv:
                Intent intent = new Intent(this, Show360.class);
                intent.putExtra("hotelId", 1);
                startActivity(intent);
            break;
        }
    }
}
