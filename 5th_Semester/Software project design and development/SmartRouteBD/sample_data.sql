DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Routes;
DROP TABLE IF EXISTS Booking;

CREATE TABLE Users(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    email TEXT UNIQUE,
    password TEXT
);

CREATE TABLE Routes(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    source TEXT,
    destination TEXT,
    vehicle TEXT,
    distance INTEGER,
    traffic INTEGER,
    time INTEGER,
    fare INTEGER,
    details TEXT
);

CREATE TABLE Booking(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER,
    route_id INTEGER,
    payment_method TEXT,
    transaction_id TEXT
);

-- Fastest Route
INSERT INTO Routes
(source,destination,vehicle,distance,traffic,time,fare,details)
VALUES
(
'Saidabad',
'Uttara',
'Bike Ride',
20,
2,
30,
400,
'Fastest route via Airport Road'
);

-- Cheapest Route
INSERT INTO Routes
(source,destination,vehicle,distance,traffic,time,fare,details)
VALUES
(
'Saidabad',
'Uttara',
'Raida Bus',
20,
5,
80,
60,
'Cheapest route using Raida bus'
);

-- Less Traffic Route
INSERT INTO Routes
(source,destination,vehicle,distance,traffic,time,fare,details)
VALUES
(
'Saidabad',
'Uttara',
'Rickshaw + Metro Rail',
20,
1,
45,
160,
'Rickshaw to Motijheel then Metro Rail to Uttara'
);