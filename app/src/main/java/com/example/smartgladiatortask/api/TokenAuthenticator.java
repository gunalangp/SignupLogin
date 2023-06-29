package com.example.smartgladiatortask.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {
    private final DefaultApi defaultApi;

    public TokenAuthenticator(@NotNull DefaultApi defaultApi) {
        this.defaultApi = defaultApi;
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NotNull Response response) throws IOException {
        return null;
    }
}
