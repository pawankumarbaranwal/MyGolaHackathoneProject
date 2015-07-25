package testpnr.pnr.android.com.mygolahackathoneproject;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {


    private void initialize() {
        amenitiesLV=(ListView)findViewById(R.id.lvAmenities);
        amenitiesLV.setEnabled(false);
        amenitiesLV.setOnItemClickListener(null);
        //amenitiesLV.setScrollContainer(false);
        String[] values = new String[]{"Gym","Free Wifi","Swimming Pool","Basket Ball"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.amenitiesview, android.R.id.text1, values);
        amenitiesLV.setAdapter(adapter);
    }
    ListView amenitiesLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }
}
