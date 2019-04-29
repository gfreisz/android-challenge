package es.npatarino.android.gotchallenge.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.npatarino.android.gotchallenge.R;
import es.npatarino.android.gotchallenge.adapters.GoTHouseAdapter;
import es.npatarino.android.gotchallenge.models.GoTManager;

public class GoTHousesListFragment extends Fragment {
    private static final String TAG = "GoTHousesListFragment";

    private GoTHouseAdapter adp;
    private ContentLoadingProgressBar pb;

    public GoTHousesListFragment() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        pb = (ContentLoadingProgressBar) rootView.findViewById(R.id.pb);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv);

        adp = new GoTHouseAdapter(getActivity());
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setHasFixedSize(true);
        rv.setAdapter(adp);

        adp.addAll(GoTManager.getInstance(getContext()).getAllHouses());
        adp.notifyDataSetChanged();
        pb.hide();

        return rootView;
    }
}
