CREATE TABLE IF NOT EXISTS transactions (
  id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
  ORIGIN_ACCOUNT VARCHAR(255),
  DESTINATION_ACCOUNT VARCHAR(255),
  CURRENCY VARCHAR(3),
  AMOUNT BIGINT,
  CREATED_AT TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_transactions_origin_account ON transactions (origin_account);
CREATE INDEX IF NOT EXISTS idx_transactions_destination_account ON transactions (destination_account);
