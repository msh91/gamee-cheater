package io.github.sharifim91.gameecheater.cheater;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.github.sharifim91.gameecheater.R;
import io.github.sharifim91.gameecheater.data.ResponseStatus;

/**
 * Created by sharifi on 6/9/17.
 */

public class CheaterFragment extends Fragment implements CheaterContract.ViewListener {
    private static final String TAG = "CheaterFragment";
    private CheaterContract.ActionListener mActionListener;
    private TextInputEditText editUrl;
    private TextInputLayout layoutUrl;
    private TextInputEditText editScore;
    private TextInputLayout layoutScore;
    private CardView cardSubmit;
    private Button btnSubmit;

    public static CheaterFragment newInstance() {
        Bundle args = new Bundle();
        CheaterFragment fragment = new CheaterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionListener = new CheaterPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        editUrl = (TextInputEditText) root.findViewById(R.id.main_url_edit_text);
        layoutUrl = (TextInputLayout) root.findViewById(R.id.main_url_layout);
        editScore = (TextInputEditText) root.findViewById(R.id.main_score_edit_text);
        layoutScore = (TextInputLayout) root.findViewById(R.id.main_score_layout);
        cardSubmit = (CardView) root.findViewById(R.id.main_submit_card);
        btnSubmit = (Button) root.findViewById(R.id.main_submit_btn);

        return root;
    }

    @Override
    public void setProgressIndicator(boolean active) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showError(ResponseStatus status) {

    }
}
