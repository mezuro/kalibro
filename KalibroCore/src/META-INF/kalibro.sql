CREATE TABLE IF NOT EXISTS `READING_GROUP` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
);

CREATE TABLE IF NOT EXISTS `READING` (
  `id` bigint(20) NOT NULL,
  `group` bigint(20) NOT NULL,
  `label` varchar(255) NOT NULL,
  `grade` bigint(20) NOT NULL,
  `color` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_READING_group` (`group`),
  CONSTRAINT `FK_READING_group` FOREIGN KEY (`group`) REFERENCES `READING_GROUP` (`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `CONFIGURATION` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
);

CREATE TABLE IF NOT EXISTS `METRIC_CONFIGURATION` (
  `id` bigint(20) NOT NULL,
  `configuration` bigint(20) NOT NULL,
  `code` varchar(255) NOT NULL,
  `weight` bigint(20) NOT NULL,
  `aggregation_form` varchar(255) NOT NULL,
  `compound` tinyint(1) NOT NULL DEFAULT '0',
  `metric_name` varchar(255) NOT NULL,
  `metric_scope` varchar(255) NOT NULL,
  `metric_description` varchar(255) DEFAULT NULL,
  `metric_origin` varchar(255) NOT NULL,
  `reading_group` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_METRIC_CONFIGURATION_configuration` (`configuration`),
  KEY `FK_METRIC_CONFIGURATION_reading_group` (`reading_group`),
  CONSTRAINT `FK_METRIC_CONFIGURATION_configuration` FOREIGN KEY (`configuration`) REFERENCES `CONFIGURATION` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_METRIC_CONFIGURATION_reading_group` FOREIGN KEY (`reading_group`) REFERENCES `READING_GROUP` (`id`)
);

CREATE TABLE IF NOT EXISTS `RANGE` (
  `id` bigint(20) NOT NULL,
  `configuration` bigint(20) NOT NULL,
  `beginning` bigint(20) NOT NULL,
  `end` bigint(20) NOT NULL,
  `reading` bigint(20) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_RANGE_configuration` (`configuration`),
  KEY `FK_RANGE_reading` (`reading`),
  CONSTRAINT `FK_RANGE_configuration` FOREIGN KEY (`configuration`) REFERENCES `METRIC_CONFIGURATION` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_RANGE_reading` FOREIGN KEY (`reading`) REFERENCES `READING` (`id`)
);

CREATE TABLE IF NOT EXISTS `THROWABLE` (
  `id` bigint(20) NOT NULL,
  `target_string` longtext NOT NULL,
  `message` longtext,
  `cause` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_THROWABLE_cause` (`cause`),
  CONSTRAINT `FK_THROWABLE_cause` FOREIGN KEY (`cause`) REFERENCES `THROWABLE` (`id`)
);

CREATE TABLE IF NOT EXISTS `STACK_TRACE_ELEMENT` (
  `id` bigint(20) NOT NULL,
  `throwable` bigint(20) DEFAULT NULL,
  `index` int(11) NOT NULL,
  `declaring_class` varchar(255) NOT NULL,
  `method_name` varchar(255) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `line_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_STACK_TRACE_ELEMENT_throwable` (`throwable`),
  CONSTRAINT `FK_STACK_TRACE_ELEMENT_throwable` FOREIGN KEY (`throwable`) REFERENCES `THROWABLE` (`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `PROJECT` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
);

CREATE TABLE IF NOT EXISTS `REPOSITORY` (
  `id` bigint(20) NOT NULL,
  `project` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `license` varchar(255) DEFAULT NULL,
  `process_period` int(11) DEFAULT NULL,
  `configuration` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_REPOSITORY_project` (`project`),
  KEY `FK_REPOSITORY_configuration` (`configuration`),
  CONSTRAINT `FK_REPOSITORY_project` FOREIGN KEY (`project`) REFERENCES `PROJECT` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_REPOSITORY_configuration` FOREIGN KEY (`configuration`) REFERENCES `CONFIGURATION` (`id`)
);

CREATE TABLE IF NOT EXISTS `PROCESSING` (
  `id` bigint(20) NOT NULL,
  `repository` bigint(20) NOT NULL,
  `date` bigint(20) NOT NULL,
  `state` varchar(255) NOT NULL,
  `error` bigint(20) DEFAULT NULL,
  `results_root` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_PROCESSING_repository` (`repository`),
  KEY `FK_PROCESSING_error` (`error`),
  CONSTRAINT `FK_PROCESSING_repository` FOREIGN KEY (`repository`) REFERENCES `REPOSITORY` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_PROCESSING_error` FOREIGN KEY (`error`) REFERENCES `THROWABLE` (`id`)
);

CREATE TABLE IF NOT EXISTS `PROCESS_TIME` (
  `id` bigint(20) NOT NULL,
  `processing` bigint(20) NOT NULL,
  `state` int(11) NOT NULL,
  `time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_PROCESS_TIME_processing` (`processing`),
  CONSTRAINT `FK_PROCESS_TIME_processing` FOREIGN KEY (`processing`) REFERENCES `PROCESSING` (`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `METRIC_CONFIGURATION_SNAPSHOT` (
  `id` bigint(20) NOT NULL,
  `processing` bigint(20) NOT NULL,
  `code` varchar(255) NOT NULL,
  `weight` bigint(20) NOT NULL,
  `aggregation_form` varchar(255) NOT NULL,
  `compound` tinyint(1) NOT NULL DEFAULT '0',
  `metric_name` varchar(255) NOT NULL,
  `metric_scope` varchar(255) NOT NULL,
  `metric_description` varchar(255) DEFAULT NULL,
  `metric_origin` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_METRIC_CONFIGURATION_SNAPSHOT_processing` (`processing`),
  CONSTRAINT `FK_METRIC_CONFIGURATION_SNAPSHOT_processing` FOREIGN KEY (`processing`) REFERENCES `PROCESSING` (`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `RANGE_SNAPSHOT` (
  `id` bigint(20) NOT NULL,
  `configuration_snapshot` bigint(20) NOT NULL,
  `beginning` bigint(20) NOT NULL,
  `end` bigint(20) NOT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `label` varchar(255) DEFAULT NULL,
  `grade` bigint(20) DEFAULT NULL,
  `color` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_RANGE_SNAPSHOT_configuration_snapshot` (`configuration_snapshot`),
  CONSTRAINT `FK_RANGE_SNAPSHOT_configuration_snapshot` FOREIGN KEY (`configuration_snapshot`) REFERENCES `METRIC_CONFIGURATION_SNAPSHOT` (`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `MODULE_RESULT` (
  `id` bigint(20) NOT NULL,
  `processing` bigint(20) NOT NULL,
  `module_name` varchar(767) NOT NULL,
  `module_granularity` varchar(255) NOT NULL,
  `grade` bigint(20) DEFAULT NULL,
  `parent` bigint(20) DEFAULT NULL,
  `height` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `module_name` (`processing`,`module_name`),
  KEY `module_height` (`processing`,`height`),
  KEY `FK_MODULE_RESULT_processing` (`processing`),
  KEY `FK_MODULE_RESULT_parent` (`parent`),
  CONSTRAINT `FK_MODULE_RESULT_processing` FOREIGN KEY (`processing`) REFERENCES `PROCESSING` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_MODULE_RESULT_parent` FOREIGN KEY (`parent`) REFERENCES `MODULE_RESULT` (`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `METRIC_RESULT` (
  `id` bigint(20) NOT NULL,
  `module_result` bigint(20) NOT NULL,
  `configuration` bigint(20) NOT NULL,
  `value` bigint(20) NOT NULL,
  `error` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_METRIC_RESULT_module_result` (`module_result`),
  KEY `FK_METRIC_RESULT_configuration` (`configuration`),
  KEY `FK_METRIC_RESULT_error` (`error`),
  CONSTRAINT `FK_METRIC_RESULT_module_result` FOREIGN KEY (`module_result`) REFERENCES `MODULE_RESULT` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_METRIC_RESULT_configuration` FOREIGN KEY (`configuration`) REFERENCES `METRIC_CONFIGURATION_SNAPSHOT` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_METRIC_RESULT_error` FOREIGN KEY (`error`) REFERENCES `THROWABLE` (`id`)
);

CREATE TABLE IF NOT EXISTS `DESCENDANT_RESULT` (
  `id` bigint(20) NOT NULL,
  `metric_result` bigint(20) NOT NULL,
  `value` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_DESCENDANT_RESULT_metric_result` (`metric_result`),
  CONSTRAINT `FK_DESCENDANT_RESULT_metric_result` FOREIGN KEY (`metric_result`) REFERENCES `METRIC_RESULT` (`id`) ON DELETE CASCADE
);