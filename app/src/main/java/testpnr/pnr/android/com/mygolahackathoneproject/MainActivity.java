package testpnr.pnr.android.com.mygolahackathoneproject;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;



public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    CardView cardView;

    private void initialize() {
        amenitiesLV=(ListView)findViewById(R.id.lvAmenities);
        amenitiesLV.setEnabled(false);
        amenitiesLV.setOnItemClickListener(null);
        //amenitiesLV.setScrollContainer(false);
        String[] values = new String[]{"Gym","Free Wifi","Swimming Pool","Basket Ball"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.amenitiesview, values);
        amenitiesLV.setAdapter(adapter);
    }
    ListView amenitiesLV;

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
