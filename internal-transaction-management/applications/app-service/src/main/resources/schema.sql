CREATE TABLE IF NOT EXISTS transactions (
  id UUID PRIMARY KEY,
  origin_account VARCHAR(255),
  destination_account VARCHAR(255),
  currency VARCHAR(10),
  amount INT,
  created_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_transactions_origin_account ON transactions (origin_account);
CREATE INDEX IF NOT EXISTS idx_transactions_destination_account ON transactions (destination_account);
