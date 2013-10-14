CREATE TABLE IF NOT EXISTS "reading_group" (
  "id" BIGINT NOT NULL PRIMARY KEY,
  "name" VARCHAR(255) NOT NULL UNIQUE,
  "description" TEXT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS "reading" (
  "id" BIGINT NOT NULL PRIMARY KEY,
  "group" BIGINT NOT NULL REFERENCES "reading_group"("id") ON DELETE CASCADE,
  "label" VARCHAR(255) NOT NULL,
  "grade" BIGINT NOT NULL,
  "color" INT NOT NULL,
  UNIQUE ("group","label")
);

CREATE TABLE IF NOT EXISTS "configuration" (
  "id" BIGINT NOT NULL PRIMARY KEY,
  "name" VARCHAR(255) NOT NULL UNIQUE,
  "description" TEXT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS "metric_configuration" (
  "id" BIGINT NOT NULL PRIMARY KEY,
  "configuration" BIGINT NOT NULL REFERENCES "configuration"("id") ON DELETE CASCADE,
  "code" VARCHAR(255) NOT NULL,
  "weight" BIGINT NOT NULL,
  "aggregation_form" VARCHAR(255) NOT NULL,
  "compound" BOOLEAN NOT NULL DEFAULT FALSE,
  "metric_name" VARCHAR(255) NOT NULL,
  "metric_scope" VARCHAR(255) NOT NULL,
  "metric_description" TEXT DEFAULT NULL,
  "metric_origin" VARCHAR(255) NOT NULL,
  "reading_group" BIGINT DEFAULT NULL REFERENCES "reading_group"("id") ON DELETE RESTRICT,
  UNIQUE ("configuration","code")
);

CREATE TABLE IF NOT EXISTS "range" (
  "id" BIGINT NOT NULL PRIMARY KEY,
  "configuration" BIGINT NOT NULL REFERENCES "metric_configuration"("id") ON DELETE CASCADE,
  "beginning" BIGINT NOT NULL,
  "end" BIGINT NOT NULL,
  "reading" BIGINT DEFAULT NULL REFERENCES "reading"("id") ON DELETE RESTRICT,
  "comments" TEXT DEFAULT NULL,
  UNIQUE ("configuration","beginning")
);

CREATE TABLE IF NOT EXISTS "project" (
  "id" BIGINT NOT NULL PRIMARY KEY,
  "name" VARCHAR(255) NOT NULL UNIQUE,
  "description" TEXT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS "repository" (
  "id" BIGINT NOT NULL PRIMARY KEY,
  "project" BIGINT NOT NULL REFERENCES "project"("id") ON DELETE CASCADE,
  "name" VARCHAR(255) NOT NULL,
  "type" VARCHAR(255) NOT NULL,
  "address" VARCHAR(255) NOT NULL,
  "description" TEXT DEFAULT NULL,
  "license" VARCHAR(255) DEFAULT NULL,
  "process_period" INT DEFAULT NULL,
  "configuration" BIGINT NOT NULL REFERENCES "configuration"("id") ON DELETE RESTRICT,
  UNIQUE ("project","name")
);

CREATE TABLE IF NOT EXISTS "repository_subscriber" (
  "id" BIGINT NOT NULL PRIMARY KEY,
  "repository" BIGINT NOT NULL REFERENCES "repository"("id") ON DELETE CASCADE,
  "name" VARCHAR(255) NOT NULL,
  "email" VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS "throwable" (
  "id" BIGINT NOT NULL PRIMARY KEY,
  "target_string" TEXT NOT NULL,
  "message" TEXT DEFAULT NULL,
  "cause" BIGINT DEFAULT NULL REFERENCES "throwable"("id") ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS "stack_trace_element" (
  "throwable" BIGINT NOT NULL REFERENCES "throwable"("id") ON DELETE CASCADE,
  "index" INT NOT NULL,
  "declaring_class" VARCHAR(255) NOT NULL,
  "method_name" VARCHAR(255) NOT NULL,
  "file_name" VARCHAR(255) DEFAULT NULL,
  "line_number" INT DEFAULT NULL,
  PRIMARY KEY ("throwable","index")
);

CREATE TABLE IF NOT EXISTS "processing" (
  "id" BIGINT NOT NULL PRIMARY KEY,
  "repository" BIGINT NOT NULL REFERENCES "repository"("id") ON DELETE CASCADE,
  "date" BIGINT NOT NULL,
  "state" VARCHAR(255) NOT NULL,
  "error" BIGINT DEFAULT NULL REFERENCES "throwable"("id") ON DELETE RESTRICT,
  "results_root" BIGINT DEFAULT NULL,
  UNIQUE ("repository","date")
);

CREATE TABLE IF NOT EXISTS "processing_time" (
  "processing" BIGINT NOT NULL REFERENCES "processing"("id") ON DELETE CASCADE,
  "state" VARCHAR(255) NOT NULL,
  "time" BIGINT NOT NULL,
  PRIMARY KEY ("processing","state")
);

CREATE TABLE IF NOT EXISTS "metric_configuration_snapshot" (
  "id" BIGINT NOT NULL PRIMARY KEY,
  "processing" BIGINT NOT NULL REFERENCES "processing"("id") ON DELETE CASCADE,
  "code" VARCHAR(255) NOT NULL,
  "weight" BIGINT NOT NULL,
  "aggregation_form" VARCHAR(255) NOT NULL,
  "compound" BOOLEAN NOT NULL DEFAULT FALSE,
  "metric_name" VARCHAR(255) NOT NULL,
  "metric_scope" VARCHAR(255) NOT NULL,
  "metric_description" TEXT DEFAULT NULL,
  "metric_origin" VARCHAR(255) NOT NULL,
  UNIQUE ("processing","code")
);

CREATE TABLE IF NOT EXISTS "range_snapshot" (
  "id" BIGINT NOT NULL PRIMARY KEY,
  "configuration_snapshot" BIGINT NOT NULL REFERENCES "metric_configuration_snapshot"("id") ON DELETE CASCADE,
  "beginning" BIGINT NOT NULL,
  "end" BIGINT NOT NULL,
  "comments" TEXT DEFAULT NULL,
  "label" VARCHAR(255) DEFAULT NULL,
  "grade" BIGINT DEFAULT NULL,
  "color" INT DEFAULT NULL,
  UNIQUE ("configuration_snapshot","beginning")
);

CREATE TABLE IF NOT EXISTS "module_result" (
  "id" BIGINT NOT NULL PRIMARY KEY,
  "processing" BIGINT NOT NULL REFERENCES "processing"("id") ON DELETE CASCADE,
  "module_name" TEXT NOT NULL,
  "module_granularity" VARCHAR(255) NOT NULL,
  "grade" BIGINT DEFAULT NULL,
  "parent" BIGINT DEFAULT NULL REFERENCES "module_result"("id") ON DELETE CASCADE,
  "height" INT NOT NULL DEFAULT 0
);

DROP INDEX IF EXISTS "module_name";

CREATE INDEX "module_name" ON "module_result"("processing","module_name");

DROP INDEX IF EXISTS "height";

CREATE INDEX "height" ON "module_result"("processing","height");

ALTER TABLE "processing" DROP CONSTRAINT IF EXISTS "processing_root";

ALTER TABLE "processing" ADD CONSTRAINT "processing_root"
  FOREIGN KEY ("results_root") REFERENCES "module_result"("id") ON DELETE SET NULL;

CREATE TABLE IF NOT EXISTS "metric_result" (
  "id" BIGINT NOT NULL PRIMARY KEY,
  "module_result" BIGINT NOT NULL REFERENCES "module_result"("id") ON DELETE CASCADE,
  "configuration" BIGINT NOT NULL REFERENCES "metric_configuration_snapshot"("id") ON DELETE CASCADE,
  "value" BIGINT NOT NULL,
  "error" BIGINT DEFAULT NULL REFERENCES "throwable"("id"),
  UNIQUE ("module_result","configuration")
);

CREATE TABLE IF NOT EXISTS "descendant_result" (
  "id" BIGINT NOT NULL PRIMARY KEY,
  "module_result" BIGINT NOT NULL REFERENCES "module_result"("id") ON DELETE CASCADE,
  "configuration" BIGINT NOT NULL REFERENCES "metric_configuration_snapshot"("id") ON DELETE CASCADE,
  "value" BIGINT NOT NULL
);

DROP INDEX IF EXISTS "descendants";

CREATE INDEX "descendants" ON "descendant_result"("module_result","configuration");

CREATE TABLE IF NOT EXISTS sequences (
  table_name VARCHAR(50) NOT NULL PRIMARY KEY,
  sequence_count BIGINT
);

INSERT INTO sequences SELECT * FROM
  (VALUES
    ('reading_group', 0),
    ('reading', 0),
    ('configuration', 0),
    ('metric_configuration', 0),
    ('range', 0),
    ('project', 0),
    ('repository', 0),
    ('repository_subscriber', 0),
    ('processing', 0),
    ('throwable', 0),
    ('metric_configuration_snapshot', 0),
    ('range_snapshot', 0),
    ('module_result', 0),
    ('metric_result', 0),
    ('descendant_result', 0)
  ) AS tmp (table_name, sequence_count)
WHERE NOT EXISTS (SELECT 1 FROM sequences WHERE table_name = tmp.table_name);