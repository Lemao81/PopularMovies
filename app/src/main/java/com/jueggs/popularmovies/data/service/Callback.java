package com.jueggs.popularmovies.data.service;

import com.jueggs.popularmovies.model.Account;
import com.jueggs.popularmovies.model.RequestToken;

public interface Callback
{
    interface StartRetrievingRequestToken
    {
        void onRetrievingRequestTokenStarted();
    }

    interface RetrieveRequestToken
    {
        void onRequestTokenRetrieved(RequestToken token);
    }

    interface StartAuthentication
    {
        void onAuthenticationStarted();
    }

    interface Authentication
    {
        void onAuthenticationCompleted(boolean authenticated);
    }

    interface StartRetrievingSessionId
    {
        void onRetrievingSessionIdStarted();
    }

    interface RetrieveSessionId
    {
        void onSessionIdRetrieved(String sessionId);
    }

    interface RetrieveAccount
    {
        void onAccountRetrieved(Account account);
    }
}
