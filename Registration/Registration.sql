DROP TABLE phk_seat;
DROP TABLE phk_history;
DROP TABLE phk_vollogin;
DROP TABLE phk_volunteer;
DROP TABLE phk_eventpmt;
DROP TABLE phk_eventregstrn;
DROP TABLE phk_eventfee;
DROP TABLE phk_event;
DROP TABLE phk_participant;
DROP TABLE phk_reference;

CREATE TABLE phk_event (
	id INT PRIMARY KEY auto_increment,
	name VARCHAR(100),
	venue VARCHAR(100),
	city VARCHAR(50),
	state VARCHAR(50),
	eligibilitylevel VARCHAR(10),
	startdate TIMESTAMP null,
	enddate TIMESTAMP null,
	isseatperlvl VARCHAR(1),
	preparedby VARCHAR(50),
	timecreated TIMESTAMP,
	timeupdated TIMESTAMP,
	active VARCHAR(1)
);

CREATE TABLE phk_eventfee (
	id INT PRIMARY KEY auto_increment,
	eventid int,
	name VARCHAR(100),
	amount int,
	cutoffdate date null,
	review VARCHAR(1),
	level VARCHAR(10),
	preparedby VARCHAR(50),
	timecreated TIMESTAMP,
	timeupdated TIMESTAMP,
	active VARCHAR(1),
	FOREIGN KEY (eventid) REFERENCES phk_event(id)
                          ON DELETE CASCADE
);

CREATE TABLE phk_participant (
	id INT PRIMARY KEY auto_increment,
	name VARCHAR(300),
	mobile VARCHAR(15),
	home VARCHAR(15),
	email VARCHAR(50),
	foundation VARCHAR(100),
	vip VARCHAR(1),
	vipdesc VARCHAR(300),
	address VARCHAR(300),
	city VARCHAR(50),
	state VARCHAR(50),
	country VARCHAR(50),
	zipcode VARCHAR(15),
	preparedby VARCHAR(50),
	timecreated TIMESTAMP,
	timeupdated TIMESTAMP,
	active VARCHAR(1)
);

CREATE TABLE phk_reference (
	id INT PRIMARY KEY auto_increment,
	uniquename VARCHAR(100) not null,
	email VARCHAR(50),
	mobile VARCHAR(15),
	remarks VARCHAR(300),
	preparedby VARCHAR(50),
	timecreated TIMESTAMP,
	timeupdated TIMESTAMP,
	active VARCHAR(1),
	CONSTRAINT uniquename UNIQUE (uniquename)
);

CREATE TABLE phk_eventregstrn (
	id INT PRIMARY KEY auto_increment,
	eventid int,
	participantid int,
	amountpayable int,
	totalamountpaid int,
	amountdue int,
	reference VARCHAR(100),
	review VARCHAR(1),
	foodcoupon varchar(1),
	eventkit varchar(1),
	application varchar(1),
	certificates varchar(1),
	level varchar(10),
	preparedby VARCHAR(50),
	timecreated TIMESTAMP,
	timeupdated TIMESTAMP,
	active VARCHAR(1),
    FOREIGN KEY (eventid) REFERENCES phk_event(id)
                                  ON DELETE CASCADE,
	FOREIGN KEY (participantid) REFERENCES phk_participant(id)
                                  ON DELETE CASCADE
);

CREATE TABLE phk_eventpmt (
	id INT PRIMARY KEY auto_increment,
	eventregstrnid int,
	amountpaid int,
	pmtmode varchar(15),
	receiptinfo varchar(100),
	receiptdate TIMESTAMP null,
	remarks VARCHAR(300),
	postdtchq VARCHAR(25),
	postdtchqdate TIMESTAMP null,
	preparedby VARCHAR(50),
	timecreated TIMESTAMP,
	timeupdated TIMESTAMP,
	active VARCHAR(1),
	FOREIGN KEY (eventregstrnid) REFERENCES phk_eventregstrn(id)
                              ON DELETE CASCADE
);

CREATE TABLE phk_volunteer (
	id INT PRIMARY KEY auto_increment,
	name VARCHAR(300),
	email VARCHAR(50),
	password VARCHAR(15),
	mobile VARCHAR(15),
	activity VARCHAR(50),
	permission varchar(30),
	preparedby VARCHAR(50),
	timecreated TIMESTAMP,
	timeupdated TIMESTAMP,
	CONSTRAINT email UNIQUE (email)
);


CREATE TABLE phk_history
(
    id INT PRIMARY KEY auto_increment,
	objectid int,
	objecttype varchar(75),
	comment varchar(500),
	preparedby VARCHAR(50),
	timecreated TIMESTAMP
);

CREATE TABLE phk_seat
(
    id INT PRIMARY KEY auto_increment,
	eventregstrnid int,
	eventid int,
	level varchar(10),
	seat int,
	FOREIGN KEY (eventid) REFERENCES phk_event(id)
                              ON DELETE CASCADE,
	FOREIGN KEY (eventregstrnid) REFERENCES phk_eventregstrn(id)
                      ON DELETE CASCADE
);

CREATE TABLE phk_vollogin (
	id INT PRIMARY KEY auto_increment,
	volunteerid INT,
	counter VARCHAR(5),
	loggedin TIMESTAMP null,
	loggedout TIMESTAMP null,
	FOREIGN KEY (volunteerid) REFERENCES phk_volunteer(id)
                      ON DELETE CASCADE
);

INSERT INTO phk_volunteer(name,   email,   password,   mobile,   activity,   permission,   preparedby,   timecreated,   timeupdated)
VALUES('Admin',   'admin@yvphk.com',   'admin',   '9999999999',   'admin',   'admin',   'system',   now(),   now());
