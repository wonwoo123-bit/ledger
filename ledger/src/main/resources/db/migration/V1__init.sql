-- =========================================
-- 초기 스키마 (ledger)
-- =========================================
SET NAMES utf8mb4;
SET time_zone = '+09:00';

-- 1) 사용자
CREATE TABLE IF NOT EXISTS users (
  id              BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  email           VARCHAR(320) NOT NULL UNIQUE COMMENT '로그인 이메일(유니크)',
  password_hash   VARCHAR(255) NOT NULL COMMENT '비밀번호 해시(평문 저장 금지)',
  name            VARCHAR(100) NOT NULL COMMENT '사용자 표시명',
  role            ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER' COMMENT '권한',
  status          ENUM('ACTIVE','SUSPENDED','DELETED') NOT NULL DEFAULT 'ACTIVE' COMMENT '계정 상태',
  created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시각',
  updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='사용자 계정';

-- 2) 카테고리
CREATE TABLE IF NOT EXISTS categories (
  id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  name       VARCHAR(100) NOT NULL UNIQUE COMMENT '카테고리명(유니크)',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시각'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='지출/수입 분류';

-- 초기 데이터(있는 경우 무시)
INSERT IGNORE INTO categories(name) VALUES
('식비'),('카페'),('교통'),('주거'),('통신'),('쇼핑'),('의료'),('급여'),('기타');

-- 3) 예산
CREATE TABLE IF NOT EXISTS budgets (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  user_id        BIGINT NOT NULL COMMENT 'FK: users.id',
  month_ym       CHAR(7) NOT NULL COMMENT '월(YYYY-MM)',
  category_id    BIGINT NOT NULL COMMENT 'FK: categories.id',
  limit_amount   BIGINT NOT NULL COMMENT '예산 한도(원)',
  created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시각',
  updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',
  CONSTRAINT fk_budgets_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_budgets_cat  FOREIGN KEY (category_id) REFERENCES categories(id),
  UNIQUE KEY uk_budget_user_month_cat (user_id, month_ym, category_id),
  KEY idx_budget_month (month_ym, category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='카테고리별 월 예산';

-- 4) 전표/영수증
CREATE TABLE IF NOT EXISTS documents (
  id               BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  user_id          BIGINT NOT NULL COMMENT 'FK: users.id(소유자)',
  type             ENUM('EXPENSE','INCOME') NOT NULL DEFAULT 'EXPENSE' COMMENT '거래 유형',
  title            VARCHAR(200) COMMENT '전표 제목(선택)',
  store_name       VARCHAR(200) COMMENT '상호/거래처명',
  pay_date         DATE COMMENT '거래일',
  total_amount     BIGINT COMMENT '총 금액(양수)',
  payment_method   ENUM('CARD','CASH','TRANSFER','WALLET','UNKNOWN') DEFAULT 'UNKNOWN' COMMENT '결제수단',
  raw_text         MEDIUMTEXT COMMENT 'OCR 추출 원문',          -- ★ MEDIUMTEXT 고정
  file_url         VARCHAR(1000) NOT NULL COMMENT '업로드 파일 경로/URL',
  status           ENUM('IMPORTED','CONFIRMED') NOT NULL DEFAULT 'IMPORTED' COMMENT '상태',
  created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시각',
  updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',
  CONSTRAINT fk_docs_user FOREIGN KEY (user_id) REFERENCES users(id),
  KEY idx_docs_user_date (user_id, pay_date DESC, id DESC),
  KEY idx_docs_type_date (type, pay_date, id),
  KEY idx_docs_store (store_name),
  KEY idx_docs_status (status),
  KEY idx_docs_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='전표/영수증(지출·수입)';

-- 5) 품목 상세
CREATE TABLE IF NOT EXISTS line_items (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  document_id   BIGINT NOT NULL COMMENT 'FK: documents.id',
  name          VARCHAR(200) COMMENT '품목명',
  quantity      DECIMAL(12,2) DEFAULT 1 COMMENT '수량',
  unit_price    DECIMAL(12,2) COMMENT '단가',
  category_id   BIGINT NULL COMMENT 'FK: categories.id',
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시각',
  CONSTRAINT fk_items_doc FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE,
  CONSTRAINT fk_items_cat FOREIGN KEY (category_id) REFERENCES categories(id),
  KEY idx_items_doc (document_id),
  KEY idx_items_cat (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='전표의 품목 상세';

-- 6) 태그
CREATE TABLE IF NOT EXISTS document_tags (
  document_id BIGINT NOT NULL COMMENT 'FK: documents.id',
  tag         VARCHAR(50) NOT NULL COMMENT '태그 라벨',
  PRIMARY KEY (document_id, tag),
  CONSTRAINT fk_dtag_doc FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='전표 태그(키워드)';

-- 7) 이슈
CREATE TABLE IF NOT EXISTS issues (
  id               BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  reporter_user_id BIGINT NOT NULL COMMENT 'FK: users.id(신고자)',
  assignee_user_id BIGINT NULL COMMENT 'FK: users.id(담당자)',
  severity         ENUM('LOW','MEDIUM','HIGH','CRITICAL') NOT NULL DEFAULT 'LOW' COMMENT '심각도',
  status           ENUM('OPEN','TRIAGED','IN_PROGRESS','RESOLVED','CLOSED') NOT NULL DEFAULT 'OPEN' COMMENT '처리 상태',
  title            VARCHAR(200) NOT NULL COMMENT '이슈 제목',
  description      TEXT COMMENT '이슈 상세',
  created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시각',
  updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',
  resolved_at      TIMESTAMP NULL COMMENT '해결 시각',
  CONSTRAINT fk_issues_reporter FOREIGN KEY (reporter_user_id) REFERENCES users(id),
  CONSTRAINT fk_issues_assignee FOREIGN KEY (assignee_user_id) REFERENCES users(id),
  KEY idx_issues_status (status, severity, created_at DESC),
  KEY idx_issues_reporter (reporter_user_id, created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='사용자 불편/버그 신고';

-- 8) 이슈 코멘트
CREATE TABLE IF NOT EXISTS issue_comments (
  id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  issue_id   BIGINT NOT NULL COMMENT 'FK: issues.id',
  user_id    BIGINT NOT NULL COMMENT 'FK: users.id(작성자)',
  body       TEXT NOT NULL COMMENT '댓글 내용',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '작성 시각',
  CONSTRAINT fk_ic_issue FOREIGN KEY (issue_id) REFERENCES issues(id) ON DELETE CASCADE,
  CONSTRAINT fk_ic_user  FOREIGN KEY (user_id)  REFERENCES users(id),
  KEY idx_ic_issue_time (issue_id, created_at ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='이슈 댓글';

-- 9) 인앱 알림
CREATE TABLE IF NOT EXISTS notifications (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  user_id      BIGINT NOT NULL COMMENT 'FK: users.id(수신자)',
  title        VARCHAR(200) NOT NULL COMMENT '알림 제목',
  message      VARCHAR(1000) NOT NULL COMMENT '알림 내용',
  status       ENUM('SENT','READ','DISMISSED') NOT NULL DEFAULT 'SENT' COMMENT '알림 상태',
  related_type VARCHAR(50) NULL COMMENT '연결 객체 타입',
  related_id   BIGINT NULL COMMENT '연결 객체 PK',
  created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시각',
  read_at      TIMESTAMP NULL COMMENT '읽은 시각',
  CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users(id),
  KEY idx_notif_user_status (user_id, status, created_at DESC),
  KEY idx_notif_related (related_type, related_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='인앱 알림';
