CREATE TABLE saving_boxes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(80) NOT NULL,
    normalized_name VARCHAR(80) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'BRL',
    balance NUMERIC(14,2) NOT NULL DEFAULT 0,
    target_amount NUMERIC(14,2),
    default_box BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at TIMESTAMPTZ
);
CREATE UNIQUE INDEX uk_active_saving_box_name_per_user ON saving_boxes(user_id, normalized_name) WHERE deleted_at IS NULL;
CREATE UNIQUE INDEX uk_active_default_saving_box_per_user ON saving_boxes(user_id) WHERE default_box = true AND deleted_at IS NULL;
CREATE INDEX idx_saving_boxes_user_active ON saving_boxes(user_id) WHERE deleted_at IS NULL;
ALTER TABLE transactions ADD CONSTRAINT fk_transactions_box FOREIGN KEY (box_id) REFERENCES saving_boxes(id);
CREATE INDEX idx_transactions_user_box ON transactions(user_id, box_id);
