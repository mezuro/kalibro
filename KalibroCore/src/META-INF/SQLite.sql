PRAGMA foreign_keys = OFF;

DROP TABLE IF EXISTS `descendant_result`;

DROP TABLE IF EXISTS `metric_result`;

DROP TABLE IF EXISTS `module_result`;

DROP TABLE IF EXISTS `range_snapshot`;

DROP TABLE IF EXISTS `metric_configuration_snapshot`;

DROP TABLE IF EXISTS `processing`;

DROP TABLE IF EXISTS `stack_trace_element`;

DROP TABLE IF EXISTS `throwable`;

DROP TABLE IF EXISTS `repository`;

DROP TABLE IF EXISTS `project`;

DROP TABLE IF EXISTS `range`;

DROP TABLE IF EXISTS `metric_configuration`;

DROP TABLE IF EXISTS `configuration`;

DROP TABLE IF EXISTS `reading`;

DROP TABLE IF EXISTS `reading_group`;

PRAGMA foreign_keys = ON;

/* END OF DROP TABLES */

CREATE TABLE IF NOT EXISTS `reading_group` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL UNIQUE,
  `description` TEXT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `reading` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `group` BIGINT NOT NULL REFERENCES `reading_group`(`id`) ON DELETE CASCADE,
  `label` VARCHAR(255) NOT NULL,
  `grade` BIGINT NOT NULL,
  `color` INT NOT NULL,
  UNIQUE (`group`,`label`)
);

CREATE TABLE IF NOT EXISTS `configuration` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL UNIQUE,
  `description` TEXT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `metric_configuration` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `configuration` BIGINT NOT NULL REFERENCES `configuration`(`id`) ON DELETE CASCADE,
  `code` VARCHAR(255) NOT NULL,
  `weight` BIGINT NOT NULL,
  `aggregation_form` VARCHAR(255) NOT NULL,
  `compound` TINYINT NOT NULL DEFAULT 0,
  `metric_name` VARCHAR(255) NOT NULL,
  `metric_scope` VARCHAR(255) NOT NULL,
  `metric_description` TEXT DEFAULT NULL,
  `metric_origin` VARCHAR(255) NOT NULL,
  `reading_group` BIGINT DEFAULT NULL REFERENCES `reading_group`(`id`) ON DELETE RESTRICT,
  UNIQUE (`configuration`,`code`)
);

CREATE TABLE IF NOT EXISTS `range` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `configuration` BIGINT NOT NULL REFERENCES `metric_configuration`(`id`) ON DELETE CASCADE,
  `beginning` BIGINT NOT NULL,
  `end` BIGINT NOT NULL,
  `reading` BIGINT DEFAULT NULL REFERENCES `reading`(`id`) ON DELETE RESTRICT,
  `comments` TEXT DEFAULT NULL,
  UNIQUE (`configuration`,`beginning`)
);

CREATE TABLE IF NOT EXISTS `project` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL UNIQUE,
  `description` TEXT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `repository` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `project` BIGINT NOT NULL REFERENCES `project`(`id`) ON DELETE CASCADE,
  `name` VARCHAR(255) NOT NULL,
  `type` VARCHAR(255) NOT NULL,
  `address` VARCHAR(255) NOT NULL,
  `description` TEXT DEFAULT NULL,
  `license` VARCHAR(255) DEFAULT NULL,
  `process_period` INT DEFAULT NULL,
  `configuration` BIGINT NOT NULL REFERENCES `configuration`(`id`) ON DELETE RESTRICT,
  UNIQUE (`project`,`name`)
);

CREATE TABLE IF NOT EXISTS `throwable` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `target_string` TEXT NOT NULL,
  `message` TEXT,
  `cause` BIGINT DEFAULT NULL REFERENCES `throwable`(`id`) ON DELETE SET NULL
);

CREATE TRIGGER IF NOT EXISTS `delete_throwable_cause` AFTER DELETE ON `throwable`
FOR EACH ROW BEGIN
  DELETE FROM `throwable` WHERE `id` = OLD.`cause`;
END;

CREATE TABLE IF NOT EXISTS `stack_trace_element` (
  `throwable` BIGINT NOT NULL REFERENCES `throwable`(`id`) ON DELETE CASCADE,
  `index` INT NOT NULL,
  `declaring_class` VARCHAR(255) NOT NULL,
  `method_name` VARCHAR(255) NOT NULL,
  `file_name` VARCHAR(255) DEFAULT NULL,
  `line_number` INT DEFAULT NULL,
  PRIMARY KEY (`throwable`,`index`)
);

CREATE TABLE IF NOT EXISTS `processing` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `repository` BIGINT NOT NULL REFERENCES `repository`(`id`) ON DELETE CASCADE,
  `date` BIGINT NOT NULL,
  `state` VARCHAR(255) NOT NULL,
  `error` BIGINT DEFAULT NULL REFERENCES `throwable`(`id`) ON DELETE RESTRICT,
  `loading_time` BIGINT DEFAULT NULL,
  `collecting_time` BIGINT DEFAULT NULL,
  `analyzing_time` BIGINT DEFAULT NULL,
  `results_root` BIGINT DEFAULT NULL,
  UNIQUE (`repository`,`date`)
);

CREATE TRIGGER IF NOT EXISTS `delete_processing_error` AFTER DELETE ON `processing`
FOR EACH ROW BEGIN
  DELETE FROM `throwable` WHERE `id` = OLD.`error`;
END;

CREATE TABLE IF NOT EXISTS `metric_configuration_snapshot` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `processing` BIGINT NOT NULL REFERENCES `processing`(`id`) ON DELETE CASCADE,
  `code` VARCHAR(255) NOT NULL,
  `weight` BIGINT NOT NULL,
  `aggregation_form` VARCHAR(255) NOT NULL,
  `compound` TINYINT NOT NULL DEFAULT 0,
  `metric_name` VARCHAR(255) NOT NULL,
  `metric_scope` VARCHAR(255) NOT NULL,
  `metric_description` TEXT DEFAULT NULL,
  `metric_origin` VARCHAR(255) NOT NULL,
  UNIQUE (`processing`,`code`)
);

CREATE TABLE IF NOT EXISTS `range_snapshot` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `configuration_snapshot` BIGINT NOT NULL REFERENCES `metric_configuration_snapshot`(`id`) ON DELETE CASCADE,
  `beginning` BIGINT NOT NULL,
  `end` BIGINT NOT NULL,
  `comments` TEXT DEFAULT NULL,
  `label` VARCHAR(255) DEFAULT NULL,
  `grade` BIGINT DEFAULT NULL,
  `color` INT DEFAULT NULL,
  UNIQUE (`configuration_snapshot`,`beginning`)
);

CREATE TABLE IF NOT EXISTS `module_result` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `processing` BIGINT NOT NULL REFERENCES `processing`(`id`) ON DELETE CASCADE,
  `module_name` TEXT NOT NULL,
  `module_granularity` VARCHAR(255) NOT NULL,
  `grade` BIGINT DEFAULT NULL,
  `parent` BIGINT DEFAULT NULL REFERENCES `module_result`(`id`) ON DELETE CASCADE,
  `height` INT NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS `module_name` ON `module_result`(`processing`,`module_name`);

CREATE INDEX IF NOT EXISTS `height` ON `module_result`(`processing`,`height`);

CREATE TABLE IF NOT EXISTS `metric_result` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `module_result` BIGINT NOT NULL REFERENCES `module_result`(`id`) ON DELETE CASCADE,
  `configuration` BIGINT NOT NULL REFERENCES `metric_configuration_snapshot`(`id`) ON DELETE CASCADE,
  `value` BIGINT NOT NULL,
  `error` BIGINT DEFAULT NULL REFERENCES `throwable`(`id`),
  UNIQUE (`module_result`,`configuration`)
);

CREATE TRIGGER IF NOT EXISTS `delete_result_error` AFTER DELETE ON `metric_result`
FOR EACH ROW BEGIN
  DELETE FROM `throwable` WHERE `id` = OLD.`error`;
END;

CREATE TABLE IF NOT EXISTS `descendant_result` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `metric_result` BIGINT NOT NULL REFERENCES `metric_result`(`id`) ON DELETE CASCADE,
  `value` BIGINT NOT NULL
);