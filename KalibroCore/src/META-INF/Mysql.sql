SET foreign_key_checks = 0;

DROP TABLE IF EXISTS `descendant_result`, `metric_result`, `module_result`,
  `range_snapshot`, `metric_configuration_snapshot`, `processing`, `stack_trace_element`, `throwable`,
  `repository`, `project`, `range`, `metric_configuration`, `configuration`, `reading`, `reading_group`;

SET foreign_key_checks = 1;

CREATE TABLE IF NOT EXISTS `reading_group` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL UNIQUE,
  `description` TEXT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `reading` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `group` BIGINT NOT NULL,
  `label` VARCHAR(255) NOT NULL,
  `grade` BIGINT NOT NULL,
  `color` INT NOT NULL,
  CONSTRAINT UNIQUE (`group`,`label`),
  CONSTRAINT FOREIGN KEY (`group`) REFERENCES `reading_group`(`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `configuration` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL UNIQUE,
  `description` TEXT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `metric_configuration` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `configuration` BIGINT NOT NULL,
  `code` VARCHAR(255) NOT NULL,
  `weight` BIGINT NOT NULL,
  `aggregation_form` VARCHAR(255) NOT NULL,
  `compound` TINYINT NOT NULL DEFAULT 0,
  `metric_name` VARCHAR(255) NOT NULL,
  `metric_scope` VARCHAR(255) NOT NULL,
  `metric_description` TEXT DEFAULT NULL,
  `metric_origin` VARCHAR(255) NOT NULL,
  `reading_group` BIGINT DEFAULT NULL,
  CONSTRAINT UNIQUE (`configuration`,`code`),
  CONSTRAINT FOREIGN KEY (`configuration`) REFERENCES `configuration`(`id`) ON DELETE CASCADE,
  CONSTRAINT FOREIGN KEY (`reading_group`) REFERENCES `reading_group`(`id`) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS `range` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `configuration` BIGINT NOT NULL,
  `beginning` BIGINT NOT NULL,
  `end` BIGINT NOT NULL,
  `reading` BIGINT DEFAULT NULL,
  `comments` TEXT DEFAULT NULL,
  CONSTRAINT UNIQUE (`configuration`,`beginning`),
  CONSTRAINT FOREIGN KEY (`configuration`) REFERENCES `metric_configuration`(`id`) ON DELETE CASCADE,
  CONSTRAINT FOREIGN KEY (`reading`) REFERENCES `reading`(`id`) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS `project` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL UNIQUE,
  `description` TEXT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `repository` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `project` BIGINT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `type` VARCHAR(255) NOT NULL,
  `address` VARCHAR(255) NOT NULL,
  `description` TEXT DEFAULT NULL,
  `license` VARCHAR(255) DEFAULT NULL,
  `process_period` INT DEFAULT NULL,
  `configuration` BIGINT NOT NULL,
  CONSTRAINT UNIQUE (`project`,`name`),
  CONSTRAINT FOREIGN KEY (`project`) REFERENCES `project`(`id`) ON DELETE CASCADE,
  CONSTRAINT FOREIGN KEY (`configuration`) REFERENCES `configuration`(`id`) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS `throwable` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `target_string` TEXT NOT NULL,
  `message` TEXT,
  `cause` BIGINT DEFAULT NULL,
  CONSTRAINT FOREIGN KEY (`cause`) REFERENCES `throwable`(`id`) ON DELETE SET NULL
);

CREATE TRIGGER `delete_throwable_cause` AFTER DELETE ON `throwable` FOR EACH ROW BEGIN
  DELETE FROM `throwable` WHERE `id` = OLD.`cause`;
END;

CREATE TABLE IF NOT EXISTS `stack_trace_element` (
  `throwable` BIGINT NOT NULL,
  `index` INT NOT NULL,
  `declaring_class` VARCHAR(255) NOT NULL,
  `method_name` VARCHAR(255) NOT NULL,
  `file_name` VARCHAR(255) DEFAULT NULL,
  `line_number` INT DEFAULT NULL,
  PRIMARY KEY (`throwable`,`index`),
  CONSTRAINT FOREIGN KEY (`throwable`) REFERENCES `throwable`(`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `processing` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `repository` BIGINT NOT NULL,
  `date` BIGINT NOT NULL,
  `state` VARCHAR(255) NOT NULL,
  `error` BIGINT DEFAULT NULL,
  `loading_time` BIGINT DEFAULT NULL,
  `collecting_time` BIGINT DEFAULT NULL,
  `analyzing_time` BIGINT DEFAULT NULL,
  `results_root` BIGINT DEFAULT NULL,
  CONSTRAINT UNIQUE (`repository`,`date`),
  CONSTRAINT FOREIGN KEY (`repository`) REFERENCES `repository`(`id`) ON DELETE CASCADE,
  CONSTRAINT FOREIGN KEY (`error`) REFERENCES `throwable`(`id`) ON DELETE RESTRICT
);

CREATE TRIGGER `delete_processing_error` AFTER DELETE ON `processing` FOR EACH ROW BEGIN
  DELETE FROM `throwable` WHERE `id` = OLD.`error`;
END;

CREATE TABLE IF NOT EXISTS `metric_configuration_snapshot` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `processing` BIGINT NOT NULL,
  `code` VARCHAR(255) NOT NULL,
  `weight` BIGINT NOT NULL,
  `aggregation_form` VARCHAR(255) NOT NULL,
  `compound` TINYINT NOT NULL DEFAULT 0,
  `metric_name` VARCHAR(255) NOT NULL,
  `metric_scope` VARCHAR(255) NOT NULL,
  `metric_description` TEXT DEFAULT NULL,
  `metric_origin` VARCHAR(255) NOT NULL,
  CONSTRAINT UNIQUE (`processing`,`code`),
  CONSTRAINT FOREIGN KEY (`processing`) REFERENCES `processing`(`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `range_snapshot` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `configuration_snapshot` BIGINT NOT NULL,
  `beginning` BIGINT NOT NULL,
  `end` BIGINT NOT NULL,
  `comments` TEXT DEFAULT NULL,
  `label` VARCHAR(255) DEFAULT NULL,
  `grade` BIGINT DEFAULT NULL,
  `color` INT DEFAULT NULL,
  CONSTRAINT UNIQUE (`configuration_snapshot`,`beginning`),
  CONSTRAINT FOREIGN KEY (`configuration_snapshot`) REFERENCES `metric_configuration_snapshot`(`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `module_result` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `processing` BIGINT NOT NULL,
  `module_name` TEXT NOT NULL,
  `module_granularity` VARCHAR(255) NOT NULL,
  `grade` BIGINT DEFAULT NULL,
  `parent` BIGINT DEFAULT NULL,
  `height` INT NOT NULL DEFAULT 0,
  CONSTRAINT FOREIGN KEY (`processing`) REFERENCES `processing`(`id`) ON DELETE CASCADE,
  CONSTRAINT FOREIGN KEY (`parent`) REFERENCES `module_result`(`id`) ON DELETE CASCADE,
  INDEX (`processing`,`module_name`(255)),
  INDEX (`processing`,`height`)
);

ALTER TABLE `processing` ADD CONSTRAINT `processing_root`
  FOREIGN KEY (`results_root`) REFERENCES `module_result`(`id`) ON DELETE SET NULL;

CREATE TABLE IF NOT EXISTS `metric_result` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `module_result` BIGINT NOT NULL,
  `configuration` BIGINT NOT NULL,
  `value` BIGINT NOT NULL,
  `error` BIGINT DEFAULT NULL,
  CONSTRAINT `metric_result_configuration` UNIQUE (`module_result`,`configuration`),
  CONSTRAINT FOREIGN KEY (`module_result`) REFERENCES `module_result`(`id`) ON DELETE CASCADE,
  CONSTRAINT FOREIGN KEY (`configuration`) REFERENCES `metric_configuration_snapshot`(`id`) ON DELETE CASCADE,
  CONSTRAINT FOREIGN KEY (`error`) REFERENCES `throwable`(`id`)
);

CREATE TRIGGER `delete_result_error` AFTER DELETE ON `metric_result` FOR EACH ROW BEGIN
  DELETE FROM `throwable` WHERE `id` = OLD.`error`;
END;

CREATE TABLE IF NOT EXISTS `descendant_result` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `metric_result` BIGINT NOT NULL,
  `value` BIGINT NOT NULL,
  CONSTRAINT FOREIGN KEY (`metric_result`) REFERENCES `metric_result`(`id`) ON DELETE CASCADE
);