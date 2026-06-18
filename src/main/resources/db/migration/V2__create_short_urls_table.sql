CREATE TABLE short_urls (
    id BIGSERIAL PRIMARY KEY,
    original_url TEXT NOT NULL,
    short_code VARCHAR(8) UNIQUE NOT NULL,
    click_count BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,

    user_id BIGINT NOT NULL,
    CONSTRAINT fk_short_url_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);