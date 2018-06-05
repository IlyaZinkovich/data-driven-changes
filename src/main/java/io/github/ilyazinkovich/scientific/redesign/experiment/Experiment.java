package io.github.ilyazinkovich.scientific.redesign.experiment;

import static io.github.ilyazinkovich.scientific.redesign.experiment.ExperimentResult.CANDIDATE_FAILED;
import static io.github.ilyazinkovich.scientific.redesign.experiment.ExperimentResult.RESULTS_DO_NOT_MATCH;
import static io.github.ilyazinkovich.scientific.redesign.experiment.ExperimentResult.RESULTS_MATCH;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Experiment {

  private final Consumer<ExperimentResult> experimentResultConsumer;

  Experiment(final Consumer<ExperimentResult> experimentResultConsumer) {
    this.experimentResultConsumer = experimentResultConsumer;
  }

  public <T> T run(final Supplier<T> control, final Supplier<T> candidate) {
    final T controlResult = control.get();
    CompletableFuture.supplyAsync(candidate)
        .thenApply(candidateResult -> matchResults(controlResult, candidateResult))
        .exceptionally(exception -> CANDIDATE_FAILED)
        .thenAccept(experimentResultConsumer);
    return controlResult;
  }

  private <T> ExperimentResult matchResults(final T controlResult, final T candidateResult) {
    return Optional.ofNullable(controlResult)
        .filter(result -> result.equals(candidateResult))
        .map(result -> RESULTS_MATCH)
        .orElse(RESULTS_DO_NOT_MATCH);
  }
}
