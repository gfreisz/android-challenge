package es.npatarino.android.gotchallenge.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import es.npatarino.android.gotchallenge.DetailActivity;
import es.npatarino.android.gotchallenge.HouseCharactersActivity;
import es.npatarino.android.gotchallenge.R;
import es.npatarino.android.gotchallenge.models.GoTHouse;

public class GoTHouseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<GoTHouse> gcs;
    private WeakReference<Activity> mActivity;

    public GoTHouseAdapter(Activity activity) {
        this.gcs = new ArrayList<>();
        this.mActivity = new WeakReference<>(activity);
    }

    public void addAll(List<GoTHouse> collection){
        gcs.addAll(collection);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GotHouseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.got_house_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        GotHouseViewHolder gotCharacterViewHolder = (GotHouseViewHolder) holder;
        gotCharacterViewHolder.render(gcs.get(position));
        ((GotHouseViewHolder) holder).imp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(((GotHouseViewHolder) holder).itemView.getContext(), HouseCharactersActivity.class);
                intent.putExtra("house_id", gcs.get(position).getId());
                intent.putExtra("house_name", gcs.get(position).getName());
                ((GotHouseViewHolder) holder).itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gcs.size();
    }

    class GotHouseViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "GotCharacterViewHolder";
        private ImageView imp;
        private TextView tvn;

        public GotHouseViewHolder(View itemView) {
            super(itemView);
            imp = itemView.findViewById(R.id.ivBackground);
            tvn = itemView.findViewById(R.id.tv_name);
        }

        public void render(final GoTHouse goTHouse) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    URL url = null;
                    Bitmap bmp;
                    try {
                        url = new URL(goTHouse.getUrlImage());
                        bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch (IOException e) {
                        Log.e(TAG, e.getLocalizedMessage());
                        bmp = BitmapFactory.decodeResource(mActivity.get().getResources(), R.drawable.got);
                    }

                    updateUI(bmp, goTHouse.getName());
                }
            }).start();
        }

        private void updateUI(final Bitmap bmp, final String name){
            mActivity.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imp.setImageBitmap(bmp);
                    tvn.setText(name.isEmpty() ? "Unknown" : name);
                }
            });
        }
    }
}
