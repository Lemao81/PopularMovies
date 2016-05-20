package com.jueggs.popularmovies.data.service;

import com.jueggs.popularmovies.model.Token;

public interface Callback
{
    interface StartRetrievingRequestToken
    {
        void onRetrievingRequestTokenStarted();
    }

    interface RetrieveRequestToken
    {
        void onRequestTokenRetrieved(Token token);
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
}
