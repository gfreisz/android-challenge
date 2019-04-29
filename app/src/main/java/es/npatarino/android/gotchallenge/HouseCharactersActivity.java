package es.npatarino.android.gotchallenge;

import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import es.npatarino.android.gotchallenge.adapters.GoTCharacterAdapter;
import es.npatarino.android.gotchallenge.models.GoTHouse;
import es.npatarino.android.gotchallenge.models.GoTManager;

public class HouseCharactersActivity extends AppCompatActivity {
    private GoTCharacterAdapter adp;
    private ContentLoadingProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_characters);

        final String house_name = getIntent().getStringExtra("house_name");
        final String house_id = getIntent().getStringExtra("house_id");

        Toolbar toolbar = findViewById(R.id.t);
        toolbar.setTitle(house_name);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        pb = findViewById(R.id.pb);
        RecyclerView rv = findViewById(R.id.rv);

        adp = new GoTCharacterAdapter(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        rv.setAdapter(adp);

        GoTHouse house = new GoTHouse(house_id, house_name, null);

        adp.addAll(GoTManager.getInstance(this).getAllCharactersFromHouse(house));
        adp.notifyDataSetChanged();
        pb.hide();
    }
}
