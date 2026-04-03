CREATE TABLE charging_TBL (
    stat_id         VARCHAR(30)   NOT NULL,
    stat_nm         VARCHAR(200)  NOT NULL,
    addr            VARCHAR(255)  NULL,
    addr_detail     VARCHAR(255)  NULL,
    location_desc   VARCHAR(255)  NULL,
    lat             DECIMAL(10,7) NOT NULL,
    lng             DECIMAL(10,7) NOT NULL,
    use_time        VARCHAR(100)  NULL,
    busi_nm         VARCHAR(100)  NULL,
    busi_call       VARCHAR(30)   NULL,
    zcode           VARCHAR(10)   NULL,
    zscode          VARCHAR(10)   NULL,
    created_at      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_charging_tbl PRIMARY KEY (stat_id)
);

CREATE INDEX idx_charging_tbl_zcode ON charging_TBL(zcode);
CREATE INDEX idx_charging_tbl_zscode ON charging_TBL(zscode);
CREATE INDEX idx_charging_tbl_lat_lng ON charging_TBL(lat, lng);
CREATE INDEX idx_charging_tbl_stat_nm ON charging_TBL(stat_nm);

SELECT *
FROM charging_tbl;

SELECT *
FROM charging_statoin;