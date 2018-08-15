package es.app.attune.attune.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.app.attune.attune.R;
import es.app.attune.attune.classes.DatabaseFunctions;
import es.app.attune.attune.classes.SearchInterfaces;

public class AutomaticModeTabs extends Fragment {

    private Toolbar toolbar;
    private ViewPager image_viewPager;

    private static NewPlayList fragmentNewPlaylist;
    private static AdvancedParameters fragmentAdvancedParameters;

    private OnFragmentInteractionListener mListener;

    public AutomaticModeTabs() {
        // Required empty public constructor
    }

    public static AutomaticModeTabs newInstance(DatabaseFunctions database, SearchInterfaces.ActionListener mActionListener) {
        AutomaticModeTabs fragment = new AutomaticModeTabs();
        fragmentNewPlaylist = NewPlayList.newInstance(database, mActionListener);
        fragmentAdvancedParameters = AdvancedParameters.newInstance(database);

        fragmentNewPlaylist.setAdvancedParametersFragment(fragmentAdvancedParameters);
        fragmentAdvancedParameters.setNewPlaylistFragment(fragmentNewPlaylist);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewPager = Objects.requireNonNull(getView()).findViewById(R.id.viewpager_newplaylist);
        setupViewPager(viewPager);

        TabLayout tabLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(fragmentNewPlaylist, "General");
        adapter.addFragment(fragmentAdvancedParameters, "Avanzados");
        viewPager.setAdapter(adapter);
    }

    public boolean validarFormulario(int mode) {
        boolean correcto_general = false;
        boolean correcto_avanzado = true;
        // Validamos la pestaña General
        correcto_general = fragmentNewPlaylist.ValidarFormulario(mode);
        correcto_avanzado = fragmentAdvancedParameters.ValidarFormulario(mode);

        return (correcto_general && correcto_avanzado);
    }

    public Bitmap getImage() {
        return fragmentNewPlaylist.getImage();
    }

    public String getName() {
        return fragmentNewPlaylist.getName();
    }

    public int getTempo() {
        return NewPlayList.getTempo();
    }

    public String getCategory() {
        return NewPlayList.getCategory();
    }

    public int getDuration() {
        return NewPlayList.getDuration();
    }

    public float getSongDuration() {
        return fragmentNewPlaylist.getSongDuration();
    }

    public float getAcousticness() {
        return AdvancedParameters.getAcousticness();
    }

    public float getDanceability() {
        return AdvancedParameters.getDanceability();
    }

    public float getEnergy() {
        return AdvancedParameters.getEnergy();
    }

    public float getInstrumentalness() {
        return AdvancedParameters.getInstrumentalness();
    }

    public float getLiveness() {
        return AdvancedParameters.getLiveness();
    }

    public float getLoudness() {
        return AdvancedParameters.getLoudness();
    }

    public int getPopularity() {
        return AdvancedParameters.getPopularity();
    }

    public float getSpeechiness() {
        return AdvancedParameters.getSpeechiness();
    }

    public float getValence() {
        return AdvancedParameters.getValence();
    }

    public void dismissProgress() {
        fragmentNewPlaylist.dismissProgress();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_play_list_tabs, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
