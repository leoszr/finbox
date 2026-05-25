CREATE INDEX IF NOT EXISTS idx_transactions_user_occurred_on ON transactions(user_id, occurred_on DESC);
CREATE INDEX IF NOT EXISTS idx_transactions_user_category_occurred_on ON transactions(user_id, category_id, occurred_on DESC);
CREATE INDEX IF NOT EXISTS idx_transactions_user_box_occurred_on ON transactions(user_id, box_id, occurred_on DESC);
CREATE INDEX IF NOT EXISTS idx_saving_boxes_user_active_default ON saving_boxes(user_id, default_box) WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_budgets_user_active ON budgets(user_id, active);
