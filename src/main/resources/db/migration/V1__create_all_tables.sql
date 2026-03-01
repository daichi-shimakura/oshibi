-- ============================================
-- oshibi MVP - Flyway Migration
-- V1__create_all_tables.sql
-- PostgreSQL
-- ============================================

-- -------------------------------------------
-- 1. users（ログイン情報）
-- -------------------------------------------
CREATE TABLE users (
    user_id       BIGSERIAL    PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- -------------------------------------------
-- 2. accounts（アカウント情報）
-- -------------------------------------------
CREATE TABLE accounts (
    account_id        BIGSERIAL    PRIMARY KEY,
    user_id           BIGINT       NOT NULL UNIQUE,
    account_type      VARCHAR(20)  NOT NULL,
    display_name      VARCHAR(100) NOT NULL,
    description       TEXT,
    profile_image_url VARCHAR(500),
    x_url             VARCHAR(500),
    instagram_url     VARCHAR(500),
    youtube_url       VARCHAR(500),
    tiktok_url        VARCHAR(500),
    note_url          VARCHAR(500),
    podcast_url       VARCHAR(500),
    created_at        TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_accounts_user
        FOREIGN KEY (user_id) REFERENCES users (user_id)
);

-- account_type の値チェック
ALTER TABLE accounts
    ADD CONSTRAINT chk_account_type
    CHECK (account_type IN ('FAN', 'LIVE_STAFF'));

-- -------------------------------------------
-- 3. comedian_profiles（芸人プロフィール）
--    PKはaccount_id（識別関係）
-- -------------------------------------------
CREATE TABLE comedian_profiles (
    account_id        BIGINT       PRIMARY KEY,
    unit_type         VARCHAR(20)  NOT NULL,
    agency            VARCHAR(100),
    member_names      TEXT,
    profile_image_url VARCHAR(500),
    created_at        TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_comedian_profiles_account
        FOREIGN KEY (account_id) REFERENCES accounts (account_id)
);

-- unit_type の値チェック
ALTER TABLE comedian_profiles
    ADD CONSTRAINT chk_unit_type
    CHECK (unit_type IN ('PIN', 'COMBI', 'TRIO', 'GROUP'));

-- -------------------------------------------
-- 4. venues（会場マスタ）
-- -------------------------------------------
CREATE TABLE venues (
    venue_id        BIGSERIAL    PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    prefecture      VARCHAR(20),
    address         VARCHAR(500),
    nearest_station VARCHAR(200),
    google_maps_url VARCHAR(500),
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- -------------------------------------------
-- 5. lives（ライブ情報）
-- -------------------------------------------
CREATE TABLE lives (
    live_id              BIGSERIAL    PRIMARY KEY,
    created_by           BIGINT       NOT NULL,
    venue_id             BIGINT,
    title                VARCHAR(200) NOT NULL,
    live_type            VARCHAR(30)  NOT NULL,
    description          TEXT,
    date                 DATE         NOT NULL,
    open_time            TIME,
    start_time           TIME,
    price_advance        INT,
    price_door           INT,
    ticket_method        VARCHAR(30),
    has_streaming        BOOLEAN      NOT NULL DEFAULT FALSE,
    streaming_price      INT,
    streaming_start_date DATE,
    streaming_end_date   DATE,
    flyer_url            VARCHAR(500),
    created_at           TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at           TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_lives_created_by
        FOREIGN KEY (created_by) REFERENCES accounts (account_id),
    CONSTRAINT fk_lives_venue
        FOREIGN KEY (venue_id) REFERENCES venues (venue_id)
);

-- live_type の値チェック
ALTER TABLE lives
    ADD CONSTRAINT chk_live_type
    CHECK (live_type IN ('NETA', 'TALK', 'KIKAKU', 'YOSEN', 'BATTLE', 'TANDOKU', 'TWOMAN', 'UNIT', 'OTHER'));

-- ticket_method の値チェック
ALTER TABLE lives
    ADD CONSTRAINT chk_ticket_method
    CHECK (ticket_method IS NULL OR ticket_method IN ('OKICHIKE', 'TICKET_SITE', 'CONVENIENCE', 'DOOR', 'OTHER'));

-- -------------------------------------------
-- 6. live_performers（芸人別ライブ情報・中間テーブル）
-- -------------------------------------------
CREATE TABLE live_performers (
    live_performer_id BIGSERIAL    PRIMARY KEY,
    live_id           BIGINT       NOT NULL,
    account_id        BIGINT       NOT NULL,
    display_order     INT,
    status            VARCHAR(20)  NOT NULL DEFAULT 'TENTATIVE',
    neta_count        INT,
    neta_type         VARCHAR(200),
    pre_comment       TEXT,
    created_at        TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_live_performers_live
        FOREIGN KEY (live_id) REFERENCES lives (live_id),
    CONSTRAINT fk_live_performers_comedian
        FOREIGN KEY (account_id) REFERENCES comedian_profiles (account_id),
    CONSTRAINT uk_live_comedian
        UNIQUE (live_id, account_id)
);

-- status の値チェック
ALTER TABLE live_performers
    ADD CONSTRAINT chk_performer_status
    CHECK (status IN ('CONFIRMED', 'TENTATIVE'));

-- ============================================
-- インデックス（検索パフォーマンス用）
-- ============================================

-- ライブ一覧：日付での検索・ソートが最も頻繁
CREATE INDEX idx_lives_date ON lives (date);

-- ライブ一覧：都道府県フィルター時にvenue経由で絞り込む
CREATE INDEX idx_venues_prefecture ON venues (prefecture);

-- ライブ詳細：出演者一覧の取得
CREATE INDEX idx_live_performers_live_id ON live_performers (live_id);

-- 芸人詳細 + マイライブ：出演ライブ一覧の取得
CREATE INDEX idx_live_performers_account_id ON live_performers (account_id);

-- マイライブ：自分が登録したライブの検索
CREATE INDEX idx_lives_created_by ON lives (created_by);
