DROP TABLE IF EXISTS phk_seat;
DROP TABLE IF EXISTS phk_history;
DROP TABLE IF EXISTS phk_vollogin;
DROP TABLE IF EXISTS phk_volunteer;
DROP TABLE IF EXISTS phk_eventpmt;
DROP TABLE IF EXISTS phk_eventregstrn;
DROP TABLE IF EXISTS phk_eventfee;
DROP TABLE IF EXISTS phk_event;
DROP TABLE IF EXISTS phk_participant;
DROP TABLE IF EXISTS phk_reference;

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
	timecreated TIMESTAMP null,
	timeupdated TIMESTAMP null,
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
	timecreated TIMESTAMP null,
	timeupdated TIMESTAMP null,
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
	timecreated TIMESTAMP null,
	timeupdated TIMESTAMP null,
	active VARCHAR(1)
);

CREATE TABLE phk_reference (
	id INT PRIMARY KEY auto_increment,
	uniquename VARCHAR(100) not null,
	email VARCHAR(50),
	mobile VARCHAR(15),
	remarks VARCHAR(300),
	preparedby VARCHAR(50),
	timecreated TIMESTAMP null,
	timeupdated TIMESTAMP null,
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
	pendingpdc int,
	reference VARCHAR(100),
	review VARCHAR(1),
	foodcoupon varchar(1),
	eventkit varchar(1),
	application varchar(1),
	certificates varchar(1),
	status varchar(25),
	registrationdate TIMESTAMP null,
	level varchar(10),
	preparedby VARCHAR(50),
	timecreated TIMESTAMP null,
	timeupdated TIMESTAMP null,
	active VARCHAR(1),
    FOREIGN KEY (eventid) REFERENCES phk_event(id),
	FOREIGN KEY (participantid) REFERENCES phk_participant(id)
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
	pdcnotclear VARCHAR(1),
	preparedby VARCHAR(50),
	timecreated TIMESTAMP null,
	timeupdated TIMESTAMP null,
	active VARCHAR(1),
	FOREIGN KEY (eventregstrnid) REFERENCES phk_eventregstrn(id)
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
	timecreated TIMESTAMP null,
	timeupdated TIMESTAMP null,
	CONSTRAINT email UNIQUE (email)
);


CREATE TABLE phk_history
(
    id INT PRIMARY KEY auto_increment,
	objectid int,
	objecttype varchar(75),
	comment varchar(500),
	preparedby VARCHAR(50),
	timecreated TIMESTAMP null
);

CREATE TABLE phk_seat
(
    id INT PRIMARY KEY auto_increment,
	eventregstrnid int,
	eventid int,
	level varchar(10),
	seat int,
	FOREIGN KEY (eventid) REFERENCES phk_event(id),
	FOREIGN KEY (eventregstrnid) REFERENCES phk_eventregstrn(id)
);

CREATE TABLE phk_vollogin (
	id INT PRIMARY KEY auto_increment,
	volunteerid INT,
	counter VARCHAR(5),
	loggedin TIMESTAMP null,
	loggedout TIMESTAMP null,
	FOREIGN KEY (volunteerid) REFERENCES phk_volunteer(id)
);

INSERT INTO phk_volunteer(name,   email,   password,   mobile,   activity,   permission,   preparedby,   timecreated,   timeupdated)
VALUES('Admin',   'admin@yvphk.com',   'admin',   '9999999999',   'admin',   'admin',   'system',   now(),   now());

