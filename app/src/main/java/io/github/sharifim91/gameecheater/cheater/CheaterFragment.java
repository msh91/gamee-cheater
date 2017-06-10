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
import android.widget.ProgressBar;
import android.widget.Toast;

import io.github.sharifim91.gameecheater.R;
import io.github.sharifim91.gameecheater.api.ApiService;
import io.github.sharifim91.gameecheater.api.ServiceGenerator;
import io.github.sharifim91.gameecheater.data.ResponseStatus;
import io.github.sharifim91.gameecheater.data.repositories.GameeRepositoryIml;

/**
 * Created by sharifi on 6/9/17.
 */

public class CheaterFragment extends Fragment implements CheaterContract.ViewListener {
    private static final String TAG = "CheaterFragment";
    public static final String FAGMENT_NAME = CheaterFragment.class.getName();
    private CheaterContract.ActionListener mActionListener;
    private TextInputEditText editUrl;
    private TextInputLayout layoutUrl;
    private TextInputEditText editScore;
    private TextInputLayout layoutScore;
    private CardView cardSubmit;
    private Button btnSubmit;
    private ProgressBar mProgressBar;

    public static CheaterFragment newInstance() {
        Bundle args = new Bundle();
        CheaterFragment fragment = new CheaterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionListener = new CheaterPresenter(this, new GameeRepositoryIml(ServiceGenerator.createService(ApiService.class)));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mProgressBar = (ProgressBar) root.findViewById(R.id.main_pb);
        editUrl = (TextInputEditText) root.findViewById(R.id.main_url_edit_text);
        layoutUrl = (TextInputLayout) root.findViewById(R.id.main_url_layout);
        editScore = (TextInputEditText) root.findViewById(R.id.main_score_edit_text);
        layoutScore = (TextInputLayout) root.findViewById(R.id.main_score_layout);
        cardSubmit = (CardView) root.findViewById(R.id.main_submit_card);
        btnSubmit = (Button) root.findViewById(R.id.main_submit_btn);

        btnSubmit.setOnClickListener(new OnSubmitClickListener());
        return root;
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if (!isAdded()) {
            return;
        }
        layoutScore.setVisibility(active ? View.GONE : View.VISIBLE);
        layoutUrl.setVisibility(active ? View.GONE : View.VISIBLE);
        cardSubmit.setVisibility(active ? View.GONE : View.VISIBLE);
        mProgressBar.setVisibility(active ? View.VISIBLE : View.GONE);

    }

    @Override
    public void showMessage(String message) {
        if (!isAdded()) {
            return;
        }
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(ResponseStatus status) {
        if (!isAdded()) {
            return;
        }
        setProgressIndicator(false);
        showMessage(status.getErrorMessage(getContext()));
    }

    @Override
    public void showUrlError() {
        editUrl.setError(getString(R.string.error_url_wrog));
        editUrl.requestFocus();
    }

    @Override
    public void showScoreUrl() {
        editScore.setError(getString(R.string.error_score_wrong));
        editScore.requestFocus();
    }

    private class OnSubmitClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mActionListener.attemptSaveScore(editUrl.getText().toString(), editScore.getText().toString());
        }
    }
}
