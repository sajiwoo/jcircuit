CREATE TABLE circuit (
    instance_key VARCHAR(255) PRIMARY KEY,
    target_path VARCHAR(255) NOT NULL,
    target_domain VARCHAR(255) NOT NULL,
    sliding_window_type VARCHAR(50) NOT NULL DEFAULT 'COUNT_BASED',
    sliding_window_size INT NOT NULL DEFAULT 100,
    minimum_number_of_calls INT NOT NULL DEFAULT 100,
    failure_rate_threshold REAL NOT NULL DEFAULT 50.0,
    slow_call_rate_threshold REAL NOT NULL DEFAULT 100.0,
    slow_call_duration_threshold_ms BIGINT NOT NULL DEFAULT 60000,
    wait_duration_in_open_state_ms BIGINT NOT NULL DEFAULT 60000,
    permitted_calls_in_half_open INT NOT NULL DEFAULT 10,
    max_wait_duration_in_half_open_ms BIGINT NOT NULL DEFAULT 0,
    auto_transition_to_half_open BOOLEAN NOT NULL DEFAULT FALSE
);
