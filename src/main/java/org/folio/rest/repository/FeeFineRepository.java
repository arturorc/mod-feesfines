package org.folio.rest.repository;


import java.util.Optional;

import org.folio.rest.domain.FeeFineNoticeContext;
import org.folio.rest.jaxrs.model.Account;
import org.folio.rest.jaxrs.model.Feefine;
import org.folio.rest.persist.PostgresClient;

import io.vertx.core.Future;

public class FeeFineRepository {

  private static final String FEEFINES_TABLE = "feefines";

  private PostgresClient pgClient;

  public FeeFineRepository(PostgresClient pgClient) {
    this.pgClient = pgClient;
  }

  public Future<FeeFineNoticeContext> loadFeefine(FeeFineNoticeContext context) {
    Optional<String> optionalFeeFineId = Optional.ofNullable(context)
      .map(FeeFineNoticeContext::getAccount)
      .map(Account::getFeeFineId);

    if (!optionalFeeFineId.isPresent()) {
      return Future.failedFuture(new IllegalArgumentException("Fee fine id is not present"));
    }

    Future<Feefine> future = Future.future();
    pgClient.getById(FEEFINES_TABLE, optionalFeeFineId.get(), Feefine.class, future);
    return future.map(context::withFeefine);
  }
}
