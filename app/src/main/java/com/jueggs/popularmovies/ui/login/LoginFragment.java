package com.jueggs.popularmovies.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.data.service.AuthenticateService;
import com.jueggs.popularmovies.data.service.FetchRequestTokenService;
import com.jueggs.popularmovies.data.service.FetchSessionIdService;
import com.jueggs.popularmovies.model.Login;
import com.jueggs.popularmovies.model.Token;

public class LoginFragment extends Fragment
{
    @Bind(R.id.username) TextInputEditText usernameView;
    @Bind(R.id.password) TextInputEditText passwordView;

    private Login login=new Login();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.login)
    public void onLogin(View v)
    {
        login.setUsername(usernameView.getText().toString());
        login.setPassword(passwordView.getText().toString());

        new FetchRequestTokenService(this::onStartRetrievingRequestToken, this::onRequestTokenRetrieved).execute();
    }

    private void onStartRetrievingRequestToken()
    {

    }

    private void onRequestTokenRetrieved(Token token)
    {
        if (token.isSuccess())
        {
            login.setToken(token);
            new AuthenticateService(this::onAuthenticationStarted,
                    this::onAuthenticationCompleted).execute(token.getToken(), login.getUsername(), login.getPassword());
        }
    }

    private void onAuthenticationStarted()
    {

    }

    private void onAuthenticationCompleted(boolean authenticated)
    {
        if (authenticated)
        {
            login.setAuthenticated(true);
            new FetchSessionIdService(this::onRetrieveSessionIdStarted, this::onSessionIdRetrieved).execute(login.getToken().getToken());
        }
    }

    private void onSessionIdRetrieved(String sessionId)
    {
        if (sessionId != null)
            login.setSessionId(sessionId);
    }

    private void onRetrieveSessionIdStarted()
    {

    }
}
