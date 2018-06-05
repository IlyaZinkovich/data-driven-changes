package io.github.ilyazinkovich.scientific.redesign.payments;

import java.math.BigDecimal;

class Pay {

  final Long courierId;
  final BigDecimal amount;

  Pay(final Long courierId, final BigDecimal amount) {
    this.courierId = courierId;
    this.amount = amount;
  }
}
