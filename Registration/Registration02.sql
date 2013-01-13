DROP TABLE phk_kit;
DROP TABLE phk_volkit;

CREATE TABLE phk_kit (
	id INT PRIMARY KEY auto_increment,
	name VARCHAR(50),
    stock int,
    eventid int,
	preparedby VARCHAR(50),
	timecreated TIMESTAMP null,
	timeupdated TIMESTAMP null,
	active VARCHAR(1),
	FOREIGN KEY (eventid) REFERENCES phk_event(id)
);

CREATE TABLE phk_volkit (
	id INT PRIMARY KEY auto_increment,
	kitid int,
	volloginid int,
    kitcount int,
    kitsgiven int,
	preparedby VARCHAR(50),
	timecreated TIMESTAMP null,
	timeupdated TIMESTAMP null,
	active VARCHAR(1),
	FOREIGN KEY (kitid) REFERENCES phk_kit(id),
	FOREIGN KEY (volloginid) REFERENCES phk_vollogin(id)
);
