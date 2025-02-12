DROP SCHEMA IF EXISTS apartment CASCADE;
CREATE SCHEMA apartment;
SET SCHEMA 'apartment';

CREATE DOMAIN "status" AS VARCHAR(12) CHECK (VALUE IN ('Open', 'Pending', 'In-Progress', 'Resolved', 'Closed'));

DROP TABLE IF EXISTS APARTMENT CASCADE;
DROP TABLE IF EXISTS "owner" CASCADE;
DROP TABLE IF EXISTS TENANT CASCADE;
DROP TABLE IF EXISTS PREVIOUS_ADDRESS CASCADE;
DROP TABLE IF EXISTS LEASE CASCADE;
DROP TABLE IF EXISTS MAINTENANCE_REQUEST CASCADE;
DROP TABLE IF EXISTS SIGNS CASCADE;
DROP TABLE IF EXISTS SUBMITS CASCADE;

CREATE TABLE APARTMENT (
    street VARCHAR(50),
    city VARCHAR(50),
    zipcode CHAR(5),
    aptNumber INTEGER NOT NULL,
    inspectionPermitNo INTEGER NOT NULL,
    floor INTEGER,
    numBeds INTEGER,
    numBaths INTEGER,
    UNIQUE inspectionPermitNo,
    PRIMARY KEY (aptNumber)
);

CREATE TABLE "owner" (
    "Name" VARCHAR(50) NOT NULL,
    aptNumber INTEGER,
    street VARCHAR(50),
    city VARCHAR(50),
    zipcode CHAR(5),
    PRIMARY KEY ("Name", aptNumber),
    FOREIGN KEY (aptNumber) REFERENCES APARTMENT(aptNumber)
);

CREATE TABLE TENANT (
    tenantID INTEGER NOT NULL,
    occupation VARCHAR(50),
    income DEC(8, 2),
    cellno VARCHAR(20) NOT NULL,
    UNIQUE (cellno),
    PRIMARY KEY (tenantID)
);

CREATE TABLE PREVIOUS_ADDRESS (
    street VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    zipcode CHAR(5) NOT NULL,
    tenantID INTEGER NOT NULL,
    PRIMARY KEY (street, city, zipcode, tenantID),
    FOREIGN KEY (tenantID) REFERENCES TENANT(tenantID)
);

CREATE TABLE LEASE (
    leaseNo INTEGER NOT NULL,
    startDate DATE,
    endDate DATE,
    payment DEC(8, 2),
    aptNumber INTEGER NOT NULL,
    PRIMARY KEY (leaseNo),
    FOREIGN KEY (aptNumber) REFERENCES APARTMENT(aptNumber)
);

CREATE TABLE MAINTENANCE_REQUEST (
    maintenanceID INTEGER NOT NULL,
    requestDate DATE,
    problem VARCHAR(50),
    description TEXT,
    status "status",
    PRIMARY KEY (maintenanceID)
);

CREATE TABLE SIGNS (
    leaseNo INTEGER NOT NULL,
    tenantID INTEGER NOT NULL,
    renewalDate DATE,
    PRIMARY KEY (leaseNo, tenantID),
    FOREIGN KEY (leaseNo) REFERENCES LEASE(leaseNo),
    FOREIGN KEY (tenantID) REFERENCES TENANT(tenantID)
);

CREATE TABLE SUBMITS (
    maintenanceID INTEGER NOT NULL,
    tenantID INTEGER NOT NULL,
    aptNumber INTEGER NOT NULL,
    PRIMARY KEY (maintenanceID),
    FOREIGN KEY (aptNumber) REFERENCES APARTMENT(aptNumber),
    FOREIGN KEY (maintenanceID) REFERENCES MAINTENANCE_REQUEST(maintenanceID)
);

