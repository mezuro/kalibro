SET foreign_key_checks = 0;

CREATE TABLE IF NOT EXISTS `reading_group` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL UNIQUE,
  `description` TEXT DEFAULT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `reading` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `group` BIGINT NOT NULL,
  `label` VARCHAR(255) NOT NULL,
  `grade` BIGINT NOT NULL,
  `color` INT NOT NULL,
  UNIQUE (`group`,`label`),
  FOREIGN KEY (`group`) REFERENCES `reading_group`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `configuration` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL UNIQUE,
  `description` TEXT DEFAULT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `metric_configuration` (
  `id` BIGINT NOT NULL PRIMARY KEY,
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
  UNIQUE (`configuration`,`code`),
  FOREIGN KEY (`configuration`) REFERENCES `configuration`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`reading_group`) REFERENCES `reading_group`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `range` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `configuration` BIGINT NOT NULL,
  `beginning` BIGINT NOT NULL,
  `end` BIGINT NOT NULL,
  `reading` BIGINT DEFAULT NULL,
  `comments` TEXT DEFAULT NULL,
  UNIQUE (`configuration`,`beginning`),
  FOREIGN KEY (`configuration`) REFERENCES `metric_configuration`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`reading`) REFERENCES `reading`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `project` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL UNIQUE,
  `description` TEXT DEFAULT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `repository` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `project` BIGINT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `type` VARCHAR(255) NOT NULL,
  `address` VARCHAR(255) NOT NULL,
  `description` TEXT DEFAULT NULL,
  `license` VARCHAR(255) DEFAULT NULL,
  `process_period` INT DEFAULT NULL,
  `configuration` BIGINT NOT NULL,
  UNIQUE (`project`,`name`),
  FOREIGN KEY (`project`) REFERENCES `project`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`configuration`) REFERENCES `configuration`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `throwable` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `target_string` TEXT NOT NULL,
  `message` TEXT,
  `cause` BIGINT DEFAULT NULL,
  FOREIGN KEY (`cause`) REFERENCES `throwable`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `stack_trace_element` (
  `throwable` BIGINT NOT NULL,
  `index` INT NOT NULL,
  `declaring_class` VARCHAR(255) NOT NULL,
  `method_name` VARCHAR(255) NOT NULL,
  `file_name` VARCHAR(255) DEFAULT NULL,
  `line_number` INT DEFAULT NULL,
  PRIMARY KEY (`throwable`,`index`),
  FOREIGN KEY (`throwable`) REFERENCES `throwable`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `processing` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `repository` BIGINT NOT NULL,
  `date` BIGINT NOT NULL,
  `state` VARCHAR(255) NOT NULL,
  `error` BIGINT DEFAULT NULL,
  `loading_time` BIGINT DEFAULT NULL,
  `collecting_time` BIGINT DEFAULT NULL,
  `analyzing_time` BIGINT DEFAULT NULL,
  `results_root` BIGINT DEFAULT NULL,
  UNIQUE (`repository`,`date`),
  FOREIGN KEY (`repository`) REFERENCES `repository`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`error`) REFERENCES `throwable`(`id`) ON DELETE RESTRICT,
  FOREIGN KEY (`results_root`) REFERENCES `module_result`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `metric_configuration_snapshot` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `processing` BIGINT NOT NULL,
  `code` VARCHAR(255) NOT NULL,
  `weight` BIGINT NOT NULL,
  `aggregation_form` VARCHAR(255) NOT NULL,
  `compound` TINYINT NOT NULL DEFAULT 0,
  `metric_name` VARCHAR(255) NOT NULL,
  `metric_scope` VARCHAR(255) NOT NULL,
  `metric_description` TEXT DEFAULT NULL,
  `metric_origin` VARCHAR(255) NOT NULL,
  UNIQUE (`processing`,`code`),
  FOREIGN KEY (`processing`) REFERENCES `processing`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `range_snapshot` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `configuration_snapshot` BIGINT NOT NULL,
  `beginning` BIGINT NOT NULL,
  `end` BIGINT NOT NULL,
  `comments` TEXT DEFAULT NULL,
  `label` VARCHAR(255) DEFAULT NULL,
  `grade` BIGINT DEFAULT NULL,
  `color` INT DEFAULT NULL,
  UNIQUE (`configuration_snapshot`,`beginning`),
  FOREIGN KEY (`configuration_snapshot`) REFERENCES `metric_configuration_snapshot`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `module_result` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `processing` BIGINT NOT NULL,
  `module_name` TEXT NOT NULL,
  `module_granularity` VARCHAR(255) NOT NULL,
  `grade` BIGINT DEFAULT NULL,
  `parent` BIGINT DEFAULT NULL,
  `height` INT NOT NULL DEFAULT 0,
  FOREIGN KEY (`processing`) REFERENCES `processing`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`parent`) REFERENCES `module_result`(`id`) ON DELETE CASCADE,
  INDEX (`processing`,`module_name`(255)),
  INDEX (`processing`,`height`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `metric_result` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `module_result` BIGINT NOT NULL,
  `configuration` BIGINT NOT NULL,
  `value` BIGINT NOT NULL,
  `error` BIGINT DEFAULT NULL,
  UNIQUE (`module_result`,`configuration`),
  FOREIGN KEY (`module_result`) REFERENCES `module_result`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`configuration`) REFERENCES `metric_configuration_snapshot`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`error`) REFERENCES `throwable`(`id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `descendant_result` (
  `id` BIGINT NOT NULL PRIMARY KEY,
  `module_result` BIGINT NOT NULL,
  `configuration` BIGINT NOT NULL,
  `value` BIGINT NOT NULL,
  FOREIGN KEY (`module_result`) REFERENCES `module_result`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`configuration`) REFERENCES `metric_configuration_snapshot`(`id`) ON DELETE CASCADE,
  INDEX (`module_result`,`configuration`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sequences (
  table_name VARCHAR(50) NOT NULL PRIMARY KEY,
  sequence_count BIGINT
);

INSERT IGNORE INTO sequences VALUES
  ('reading_group', 0),
  ('reading', 0),
  ('configuration', 0),
  ('metric_configuration', 0),
  ('range', 0),
  ('project', 0),
  ('repository', 0),
  ('processing', 0),
  ('throwable', 0),
  ('metric_configuration_snapshot', 0),
  ('range_snapshot', 0),
  ('module_result', 0),
  ('metric_result', 0),
  ('descendant_result', 0);

SET foreign_key_checks = 1;