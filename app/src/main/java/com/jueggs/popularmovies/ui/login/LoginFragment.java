package com.jueggs.popularmovies.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.jueggs.popularmovies.R;
import com.jueggs.popularmovies.data.service.AccountService;
import com.jueggs.popularmovies.data.service.LoginService;
import com.jueggs.popularmovies.model.Account;
import com.jueggs.popularmovies.model.Login;
import com.jueggs.popularmovies.model.Token;

import static com.jueggs.popularmovies.data.service.LoginService.*;

public class LoginFragment extends Fragment
{
    @Bind(R.id.username) TextInputEditText usernameView;
    @Bind(R.id.password) TextInputEditText passwordView;

    private Login login = new Login();
    private Account account;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        return view;
    }

    @OnClick(R.id.login)
    public void onLogin(View v)
    {
        login.setUsername(usernameView.getText().toString());
        login.setPassword(passwordView.getText().toString());

        new FetchRequestTokenTask(this::onStartRetrievingRequestToken, this::onRequestTokenRetrieved).execute();
    }

    private void onStartRetrievingRequestToken()
    {

    }

    private void onRequestTokenRetrieved(Token token)
    {
        if (token.isSuccess())
        {
            login.setToken(token);
            new AuthenticateTask(this::onAuthenticationCompleted).execute(token.getToken(), login.getUsername(), login.getPassword());
        }
    }

    private void onAuthenticationCompleted(boolean authenticated)
    {
        if (authenticated)
        {
            login.setAuthenticated(true);
            new FetchSessionIdTask(this::onSessionIdRetrieved).execute(login.getToken().getToken());
        }
    }

    private void onSessionIdRetrieved(String sessionId)
    {
        if (sessionId != null)
        {
            login.setSessionId(sessionId);
            new AccountService.FetchAccountDataTask(this::onAccountRetrieved).execute(sessionId);
        }
    }

    private void onAccountRetrieved(Account account)
    {
        if (account != null)
        {
            this.account = account;
            Toast.makeText(getActivity(), "Logged in\n" + account.getUsername(), Toast.LENGTH_SHORT).show();
        }

    }
}
