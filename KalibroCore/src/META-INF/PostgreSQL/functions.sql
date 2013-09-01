CREATE OR REPLACE FUNCTION "delete_throwable"() RETURNS trigger AS $$
BEGIN
  IF TG_ARGV[0] = 'cause' THEN
    DELETE FROM "throwable" WHERE "id" = OLD."cause";
  ELSIF TG_ARGV[0] = 'error' THEN
    DELETE FROM "throwable" WHERE "id" = OLD."error";
  END IF;
  RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS "delete_throwable_cause" ON "throwable";

CREATE TRIGGER "delete_throwable_cause" AFTER DELETE ON "throwable"
    FOR EACH ROW EXECUTE PROCEDURE "delete_throwable"('cause');

DROP TRIGGER IF EXISTS "delete_processing_error" ON "processing";

CREATE TRIGGER "delete_processing_error" AFTER DELETE ON "processing"
    FOR EACH ROW EXECUTE PROCEDURE "delete_throwable"('error');

DROP TRIGGER IF EXISTS "delete_result_error" ON "metric_result";

CREATE TRIGGER "delete_result_error" AFTER DELETE ON "metric_result"
    FOR EACH ROW EXECUTE PROCEDURE "delete_throwable"('error');

﻿CREATE OR REPLACE FUNCTION nan() RETURNS bigint AS $$
BEGIN
  RETURN 9221120237041090560;
END;
$$ LANGUAGE plpgsql IMMUTABLE;

CREATE OR REPLACE FUNCTION next_id(name varchar(50)) RETURNS bigint AS $$
BEGIN
  UPDATE sequences SET sequence_count = sequence_count + 1 WHERE table_name = name;
  RETURN (SELECT sequence_count FROM sequences WHERE table_name = name);
END;
$$ LANGUAGE plpgsql STABLE;

CREATE OR REPLACE FUNCTION push_up_results(process_id bigint, h int) RETURNS void AS $$
DECLARE
  result record;
BEGIN
  FOR result IN
    SELECT mor.parent as module, mer.configuration, mer.value
    FROM module_result mor JOIN metric_result mer ON mer.module_result = mor.id
    WHERE mor.processing = process_id AND mor.height = h
  LOOP
    BEGIN
      INSERT INTO metric_result(id, module_result, configuration, value)
        VALUES (next_id('metric_result'), result.module, result.configuration, nan());
    EXCEPTION WHEN unique_violation THEN
      -- do nothing
    END;
    IF result.value <> nan() THEN
      INSERT INTO descendant_result(id, module_result, configuration, value)
        VALUES (next_id('descendant_result'), result.module, result.configuration, result.value);
    END IF;
  END LOOP;
END;
$$ LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION pull_up_descendants(process_id bigint, h int) RETURNS void AS $$
DECLARE
  result record;
BEGIN
  FOR result IN
    SELECT mor.id as module, der.configuration, der.value
    FROM module_result mor
      JOIN module_result child ON child.parent = mor.id
      JOIN descendant_result der ON der.module_result = child.id
    WHERE mor.processing = process_id AND mor.height = h
  LOOP
    INSERT INTO descendant_result(id, module_result, configuration, value)
      VALUES (next_id('descendant_result'), result.module, result.configuration, result.value);
  END LOOP;
END;
$$ LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION aggregate_results(process_id bigint) RETURNS void AS $$
DECLARE
  h integer;
BEGIN
  h := (SELECT max(height) FROM module_result WHERE processing = process_id);
  IF h is null THEN
    RAISE EXCEPTION 'Nonexistent processing --> %', process_id;
  END IF;
  WHILE h > 0 LOOP
    h := h - 1;
    PERFORM push_up_results(process_id, h + 1);
    PERFORM pull_up_descendants(process_id, h);
  END LOOP;
END;
$$ LANGUAGE plpgsql VOLATILE;