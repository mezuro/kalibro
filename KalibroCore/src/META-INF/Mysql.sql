DROP TABLE IF EXISTS `reading_group`, `reading`, `configuration`, `metric_configuration`, `range`,
  `project`, `repository`, `throwable`, `stack_trace_element`, `processing`, `process_time`,
  `metric_configuration_snapshot`, `range_snapshot`, `module_result`, `metric_result`, `descendant_result`;

CREATE TABLE IF NOT EXISTS `reading_group` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR NOT NULL UNIQUE,
  `description` TEXT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `reading` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `group` BIGINT NOT NULL REFERENCES `reading_group`(`id`) ON DELETE CASCADE,
  `label` VARCHAR NOT NULL,
  `grade` BIGINT NOT NULL,
  `color` INT NOT NULL,
  CONSTRAINT `reading_label` UNIQUE (`group`,`label`)
);

CREATE TABLE IF NOT EXISTS `configuration` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR NOT NULL UNIQUE,
  `description` TEXT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `metric_configuration` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `configuration` BIGINT NOT NULL REFERENCES `configuration`(`id`) ON DELETE CASCADE,
  `code` VARCHAR NOT NULL,
  `weight` BIGINT NOT NULL,
  `aggregation_form` VARCHAR NOT NULL,
  `compound` TINYINT NOT NULL DEFAULT 0,
  `metric_name` VARCHAR NOT NULL,
  `metric_scope` VARCHAR NOT NULL,
  `metric_description` TEXT DEFAULT NULL,
  `metric_origin` VARCHAR NOT NULL,
  `reading_group` BIGINT DEFAULT NULL REFERENCES `reading_group`(`id`) ON DELETE RESTRICT,
  CONSTRAINT `metric_configuration_code` UNIQUE (`configuration`,`code`)
);

CREATE TABLE IF NOT EXISTS `range` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `configuration` BIGINT NOT NULL REFERENCES `metric_configuration`(`id`) ON DELETE CASCADE,
  `beginning` BIGINT NOT NULL,
  `end` BIGINT NOT NULL,
  `reading` BIGINT DEFAULT NULL REFERENCES `reading`(`id`) ON DELETE RESTRICT,
  `comments` TEXT DEFAULT NULL,
  CONSTRAINT `range_beginning` UNIQUE (`configuration`,`beginning`)
);

CREATE TABLE IF NOT EXISTS `project` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR NOT NULL UNIQUE,
  `description` TEXT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `repository` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `project` BIGINT NOT NULL REFERENCES `project`(`id`) ON DELETE CASCADE,
  `name` VARCHAR NOT NULL,
  `type` VARCHAR NOT NULL,
  `address` VARCHAR NOT NULL,
  `description` TEXT DEFAULT NULL,
  `license` VARCHAR DEFAULT NULL,
  `process_period` INT DEFAULT NULL,
  `configuration` BIGINT NOT NULL REFERENCES `configuration`(`id`) ON DELETE RESTRICT,
  CONSTRAINT `repository_name` UNIQUE (`project`,`name`)
);

CREATE TABLE IF NOT EXISTS `throwable` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `target_string` TEXT NOT NULL,
  `message` TEXT,
  `cause` BIGINT DEFAULT NULL REFERENCES `throwable`(`id`) ON DELETE SET NULL
)

CREATE TRIGGER `delete_throwable_cause` AFTER DELETE ON `throwable` FOR EACH ROW BEGIN
  DELETE FROM `throwable` WHERE `id` = OLD.`cause`;
END;

CREATE TABLE IF NOT EXISTS `stack_trace_element` (
  `throwable` BIGINT NOT NULL REFERENCES `throwable`(`id`) ON DELETE CASCADE,
  `index` INT NOT NULL,
  `declaring_class` VARCHAR NOT NULL,
  `method_name` VARCHAR NOT NULL,
  `file_name` VARCHAR DEFAULT NULL,
  `line_number` INT DEFAULT NULL,
  PRIMARY KEY (`throwable`,`index`)
);

CREATE TABLE IF NOT EXISTS `processing` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `repository` BIGINT NOT NULL REFERENCES `repository`(`id`) ON DELETE CASCADE,
  `date` BIGINT NOT NULL,
  `state` VARCHAR NOT NULL,
  `error` BIGINT DEFAULT NULL REFERENCES `throwable`(`id`) ON DELETE RESTRICT,
  `loading_time` BIGINT DEFAULT NULL,
  `collecting_time` BIGINT DEFAULT NULL,
  `analyzing_time` BIGINT DEFAULT NULL,
  `results_root` BIGINT DEFAULT NULL,
  CONSTRAINT `processing_date` UNIQUE (`repository`,`date`)
);

CREATE TRIGGER `delete_processing_error` AFTER DELETE ON `processing` FOR EACH ROW BEGIN
  DELETE FROM `throwable` WHERE `id` = OLD.`error`;
END;

CREATE TABLE IF NOT EXISTS `metric_configuration_snapshot` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `processing` BIGINT NOT NULL REFERENCES `processing`(`id`) ON DELETE CASCADE,
  `code` VARCHAR NOT NULL,
  `weight` BIGINT NOT NULL,
  `aggregation_form` VARCHAR NOT NULL,
  `compound` TINYINT NOT NULL DEFAULT 0,
  `metric_name` VARCHAR NOT NULL,
  `metric_scope` VARCHAR NOT NULL,
  `metric_description` TEXT DEFAULT NULL,
  `metric_origin` VARCHAR NOT NULL,
  CONSTRAINT `metric_configuration_snapshot_code` UNIQUE (`processing`,`code`)
);

CREATE TABLE IF NOT EXISTS `range_snapshot` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `configuration_snapshot` BIGINT NOT NULL REFERENCES `metric_configuration_snapshot`(`id`) ON DELETE CASCADE,
  `beginning` BIGINT NOT NULL,
  `end` BIGINT NOT NULL,
  `comments` TEXT DEFAULT NULL,
  `label` VARCHAR DEFAULT NULL,
  `grade` BIGINT DEFAULT NULL,
  `color` INT DEFAULT NULL,
  CONSTRAINT `range_snapshot_beginning` UNIQUE (`configuration_snapshot`,`beginning`)
);

CREATE TABLE IF NOT EXISTS `module_result` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `processing` BIGINT NOT NULL REFERENCES `processing`(`id`) ON DELETE CASCADE,
  `module_name` TEXT NOT NULL,
  `module_granularity` VARCHAR NOT NULL,
  `grade` BIGINT DEFAULT NULL,
  `parent` BIGINT DEFAULT NULL REFERENCES `module_result`(`id`) ON DELETE CASCADE,
  `height` INT NOT NULL DEFAULT 0,
  CONSTRAINT `module_result_name` UNIQUE (`processing`,`module_name`),
  INDEX `module_result_height` (`processing`,`height`)
);

ALTER TABLE `processing` ADD CONSTRAINT `processing_root`
  FOREIGN KEY (`results_root`) REFERENCES `module_result`(`id`) ON DELETE SET NULL;

CREATE TABLE IF NOT EXISTS `metric_result` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `module_result` BIGINT NOT NULL REFERENCES `module_result`(`id`) ON DELETE CASCADE,
  `configuration` BIGINT NOT NULL REFERENCES `metric_configuration_snapshot`(`id`) ON DELETE CASCADE,
  `value` BIGINT NOT NULL,
  `error` BIGINT DEFAULT NULL REFERENCES `throwable`(`id`),
  CONSTRAINT `metric_result_configuration` UNIQUE (`module_result`,`configuration`),
);

CREATE TRIGGER `delete_result_error` AFTER DELETE ON `metric_result` FOR EACH ROW BEGIN
  DELETE FROM `throwable` WHERE `id` = OLD.`error`;
END;

CREATE TABLE IF NOT EXISTS `descendant_result` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `metric_result` BIGINT NOT NULL REFERENCES `metric_result`(`id`) ON DELETE CASCADE,
  `value` BIGINT NOT NULL
);