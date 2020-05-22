package com.github.jasync.sql.db;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

public interface TestConnection extends Connection {

    @NotNull
    CompletableFuture<Connection> connect();

    @NotNull
    CompletableFuture<Connection> disconnect();

    @NotNull
    <A> CompletableFuture<A> inTransaction(@NotNull Function1<? super Connection, ? extends CompletableFuture<A>> f);

    boolean isConnected();

    @NotNull
    CompletableFuture<Boolean> releasePreparedStatement(@NotNull String query);

    @NotNull
    CompletableFuture<QueryResult> sendPreparedStatement(@NotNull String query, @NotNull List<? extends Object> values, boolean release);

    @NotNull
    CompletableFuture<QueryResult> sendPreparedStatement(@NotNull String query, @NotNull List<? extends Object> values);

    @NotNull
    CompletableFuture<QueryResult> sendPreparedStatement(@NotNull String query);

    @NotNull
    CompletableFuture<QueryResult> sendQuery(@NotNull String query);

}
