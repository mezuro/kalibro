DROP TRIGGER IF EXISTS `delete_throwable_cause`;

CREATE TRIGGER `delete_throwable_cause` AFTER DELETE ON `throwable`
FOR EACH ROW BEGIN
  DELETE FROM `throwable` WHERE `id` = OLD.`cause`;
END;

DROP TRIGGER IF EXISTS `delete_processing_error`;

CREATE TRIGGER `delete_processing_error` AFTER DELETE ON `processing`
FOR EACH ROW BEGIN
  DELETE FROM `throwable` WHERE `id` = OLD.`error`;
END;

DROP TRIGGER IF EXISTS `delete_result_error`;

CREATE TRIGGER `delete_result_error` AFTER DELETE ON `metric_result`
FOR EACH ROW BEGIN
  DELETE FROM `throwable` WHERE `id` = OLD.`error`;
END;

DROP FUNCTION IF EXISTS nan;

CREATE FUNCTION nan() RETURNS BIGINT DETERMINISTIC
  RETURN 9221120237041090560;

DROP FUNCTION IF EXISTS next_id;

CREATE FUNCTION next_id(name VARCHAR(50)) RETURNS BIGINT
BEGIN
  UPDATE sequences SET sequence_count = sequence_count + 1 WHERE table_name = name;
  RETURN (SELECT sequence_count FROM sequences WHERE table_name = name);
END;

DROP PROCEDURE IF EXISTS push_up_results;

CREATE PROCEDURE push_up_results(process_id BIGINT, h INT)
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE module, configuration, value BIGINT;
  DECLARE result CURSOR FOR
    SELECT mor.parent, mer.configuration, mer.value
    FROM module_result mor JOIN metric_result mer ON mer.module_result = mor.id
    WHERE mor.processing = process_id AND mor.height = h;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
  OPEN result;
  FETCH result INTO module, configuration, value;
  WHILE NOT done DO
    INSERT IGNORE INTO metric_result(id, module_result, configuration, value)
      VALUES (next_id('metric_result'), module, configuration, nan());
    IF value <> nan() THEN
      INSERT INTO descendant_result(id, module_result, configuration, value)
        VALUES (next_id('descendant_result'), module, configuration, value);
    END IF;
    FETCH result INTO module, configuration, value;
  END WHILE;
  CLOSE result;
END;

DROP PROCEDURE IF EXISTS pull_up_descendants;

CREATE PROCEDURE pull_up_descendants(process_id BIGINT, h INT)
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE module, configuration, value BIGINT;
  DECLARE result CURSOR FOR
    SELECT mor.id, der.configuration, der.value
    FROM module_result mor
      JOIN module_result child ON child.parent = mor.id
      JOIN descendant_result der ON der.module_result = child.id
    WHERE mor.processing = process_id AND mor.height = h;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
  OPEN result;
  FETCH result INTO module, configuration, value;
  WHILE NOT done DO
    INSERT INTO descendant_result(id, module_result, configuration, value)
      VALUES (next_id('descendant_result'), result.module, result.configuration, result.value);
  END WHILE;
END;

DROP PROCEDURE IF EXISTS aggregate_results;

CREATE PROCEDURE aggregate_results(process_id BIGINT)
BEGIN
  DECLARE h integer;
  SET h = (SELECT max(height) FROM module_result WHERE processing = process_id);
  IF h is null THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = concat('Nonexistent processing --> ', process_id);
  END IF;
  WHILE h > 0 DO
    h := h - 1;
    CALL push_up_results(process_id, h + 1);
    CALL pull_up_descendants(process_id, h);
  END WHILE;
END;