DROP TABLE IF EXISTS `leaf_alloc`;

CREATE TABLE `leaf_alloc`
(
    `biz_tag`     varchar(128) NOT NULL DEFAULT '',
    `max_id`      bigint(20) NOT NULL DEFAULT '1',
    `step`        int(11) NOT NULL,
    `description` varchar(256)          DEFAULT NULL,
    `update_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`biz_tag`)
) ENGINE=InnoDB;


INSERT INTO leaf_alloc (biz_tag,
                        max_id,
                        step)
SELECT sequence_name AS biz_tag, next_val AS max_id, 100
FROM ls_sequence //rename ls_sequence make sure the old way failed