DROP TABLE IF EXISTS phk_rowmeta;

CREATE TABLE phk_rowmeta (
	id INT PRIMARY KEY auto_increment,
	name varchar(50),
	sortorder int,
	rowname VARCHAR(3),
	rowmax int,
	rowfull varchar(1),
	preparedby VARCHAR(50),
	timecreated TIMESTAMP null,
	timeupdated TIMESTAMP null,
	active VARCHAR(1),
	CONSTRAINT rowname UNIQUE (rowname)
);

ALTER TABLE phk_event ADD COLUMN seatingtype VARCHAR(5);
ALTER TABLE phk_event ADD COLUMN seatallocated VARCHAR(1);
ALTER TABLE phk_event ADD COLUMN rowmetaname varchar(50);
ALTER TABLE phk_seat ADD COLUMN alpha VARCHAR(5);
ALTER TABLE phk_seat ADD COLUMN suffix VARCHAR(5);
ALTER TABLE phk_seat ADD COLUMN custom VARCHAR(1);
