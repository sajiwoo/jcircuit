CREATE TABLE circuit (
    instance_key VARCHAR2(255) PRIMARY KEY,
    target_path VARCHAR2(255) NOT NULL,
    target_domain VARCHAR2(255),
    sliding_window_type VARCHAR2(50) NOT NULL,
    sliding_window_size NUMBER(10) DEFAULT 100 NOT NULL,
    minimum_number_of_calls NUMBER(10) DEFAULT 100 NOT NULL,
    failure_rate_threshold NUMBER(5, 2) DEFAULT 50.0 NOT NULL,
    slow_call_rate_threshold NUMBER(5, 2) DEFAULT 100.0 NOT NULL,
    slow_call_duration_threshold_ms NUMBER(19) DEFAULT 60000 NOT NULL,
    wait_duration_in_open_state_ms NUMBER(19) DEFAULT 60000 NOT NULL,
    permitted_calls_in_half_open NUMBER(10) DEFAULT 10 NOT NULL,
    max_wait_duration_in_half_open_ms NUMBER(19) DEFAULT 0 NOT NULL,
    auto_transition_to_half_open NUMBER(1) DEFAULT 0 NOT NULL
);
COMMIT;
