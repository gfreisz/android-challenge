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
import android.widget.SearchView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import es.npatarino.android.gotchallenge.DetailActivity;
import es.npatarino.android.gotchallenge.models.GoTCharacter;
import es.npatarino.android.gotchallenge.R;
import es.npatarino.android.gotchallenge.models.GoTManager;

public class GoTCharacterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SearchView.OnQueryTextListener{

    private List<GoTCharacter> gcs;
    private WeakReference<Activity> mActivity;

    public GoTCharacterAdapter(Activity activity) {
        this.gcs = new ArrayList<>();
        this.mActivity = new WeakReference<>(activity);
    }

    public void addAll(List<GoTCharacter> collection) {
        gcs.addAll(collection);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GotCharacterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.got_character_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        GotCharacterViewHolder gotCharacterViewHolder = (GotCharacterViewHolder) holder;
        gotCharacterViewHolder.render(gcs.get(position));
        ((GotCharacterViewHolder) holder).imp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(((GotCharacterViewHolder) holder).itemView.getContext(), DetailActivity.class);
                intent.putExtra("description", gcs.get(position).getDescription());
                intent.putExtra("name", gcs.get(position).getName());
                intent.putExtra("imageUrl", gcs.get(position).getUrlImage());
                ((GotCharacterViewHolder) holder).itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gcs.size();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s.length() > 0)
            gcs = GoTManager.getInstance(mActivity.get()).getCharactersBySearchValue(s);
        else
            gcs  = GoTManager.getInstance(mActivity.get()).getAllCharacters();

        return true;
    }

    class GotCharacterViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "GotCharacterViewHolder";
        ImageView imp;
        TextView tvn;

        public GotCharacterViewHolder(View itemView) {
            super(itemView);
            imp = itemView.findViewById(R.id.ivBackground);
            tvn = itemView.findViewById(R.id.tv_name);
        }

        public void render(final GoTCharacter goTCharacter) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    URL url = null;
                    Bitmap bmp;
                    try {
                        url = new URL(goTCharacter.getUrlImage());
                        bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch (IOException e) {
                        Log.e(TAG, e.getLocalizedMessage());
                        bmp = BitmapFactory.decodeResource(mActivity.get().getResources(), R.drawable.got);
                    }

                    updateUI(bmp, goTCharacter.getName());
                }
            }).start();
        }

        private void updateUI(final Bitmap bmp, final String name){
            mActivity.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imp.setImageBitmap(bmp);
                    tvn.setText(name);
                }
            });
        }
    }
}
