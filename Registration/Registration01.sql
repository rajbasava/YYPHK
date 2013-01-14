DROP TABLE IF EXISTS phk_rowmeta;
DROP TABLE IF EXISTS phk_foundation;

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
	active VARCHAR(1)
);

ALTER TABLE phk_event ADD COLUMN seatingtype VARCHAR(5);
ALTER TABLE phk_event ADD COLUMN seatallocated VARCHAR(1);
ALTER TABLE phk_event ADD COLUMN rowmetaname varchar(50);
ALTER TABLE phk_seat ADD COLUMN alpha VARCHAR(5);
ALTER TABLE phk_seat ADD COLUMN suffix VARCHAR(5);
ALTER TABLE phk_seat ADD COLUMN custom VARCHAR(1);
ALTER TABLE phk_eventregstrn ADD COLUMN reforder int DEFAULT 1;
ALTER TABLE phk_rowmeta ADD unique (name, rowname);

CREATE TABLE phk_foundation (
	id INT PRIMARY KEY auto_increment,
	name varchar(300),
	city VARCHAR(50),
	state VARCHAR(50),
	country VARCHAR(50),
	preparedby VARCHAR(50),
	timecreated TIMESTAMP null,
	timeupdated TIMESTAMP null,
	active VARCHAR(1)
);

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Yoga Vidya Pranic Healing Foundation Calicut', 'Calicut', 'Kerala', 'India', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Yoga Vidya Pranic Healing Foundation Cochin', 'Cochin', 'Kerala', 'India', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Yoga Vidya Pranic Healing Foundation Trust Mumbai', 'Mumbai', 'Maharashtra', 'India', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Pranic Healing Gyan Prachar Kendra Maharashtra', 'Mumbai', 'Maharashtra', 'India', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Pranic Healing Foundation Rajasthan', 'Jaipur', 'Rajasthan', 'India', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Yoga Vidya Pranic Healing Foundation of Karnataka', 'Bangalore', 'Karnataka', 'India', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Pranic Healing Trust Tamilnadu', 'Chennai','Tamilnadu', 'India', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Pranic Healing Home Chennai', 'Chennai', 'Tamilnadu', 'India', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('MCKS Yoga Vidya Pranic Healing Trust of UP Lucknow', 'Lucknow', 'UttarPradesh', 'India', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Yoga Vidya Pranic Healing Foundation Trust Uttar Pradesh Varanasi', 'Varanasi', 'UttarPradesh', 'India', 'system', now(), now(),'1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Yoga Vidya Pranic Healing Foundation of West Bengal', 'Kolkota', 'WestBengal', 'India', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('MCKS Pranic Healing Trust West Bengal', 'Kolkota', 'WestBengal', 'India', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Pranic Healing Foundation Secunderabad', 'Secunderabad', 'AndhraPradesh', 'India', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('MCKS Yoga Vidya Pranic Healing Trust Delhi', 'Delhi', 'Delhi', 'India', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Pranic Healing Foundation Punjab', 'Punjab', 'Punjab', 'India', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Pranic Healing Foundation Singapore', 'Singapore', 'Singapore', 'Singapore', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Pranic Healing Foundation Kenya', 'Kenya', 'Kenya', 'Kenya', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Pranic Healing Foundation Malaysia', 'Malaysia', 'Malaysia', 'Malaysia', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Pranic Healing Foundation Mauritius', 'Mauritius', 'Mauritius', 'Mauritius', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Pranic Healing Foundation HongKong', 'HongKong', 'HongKong', 'HongKong', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Pranic Healing Foundation Macau', 'Macau', 'Macau', 'Macau', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Pranic Healing Foundation Dubai', 'Dubai', 'Dubai', 'Dubai', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Pranic Healing Foundation Oman', 'Muscat', 'Muscat', 'Muscat', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Pranic Healing Foundation Uruguay', 'Uruguay', 'Uruguay', 'Uruguay', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Pranic Healing Foundation Thailand', 'Bangkok', 'Bangkok', 'Thailand', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Pranic Healing Foundation USA', 'USA', 'USA', 'USA', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Pranic Healing Foundation Sri Lanka', 'Colombo', 'Colombo', 'Sri Lanka', 'system', now(), now(), '1');

insert into phk_foundation (name, city, state, country, preparedby, timecreated, timeupdated, active)
values ('Pranic Healing Foundation Australia', 'Melbourne', 'Melbourne', 'Australia', 'system', now(), now(), '1');

