#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE TABLE METER(
        ID_METER  varchar, METER_NAME  varchar, LAST_UPDATE timestamp 
    );
    INSERT INTO METER(ID_METER, METER_NAME, LAST_UPDATE) values ('1', 'Meter 1',now());
    INSERT INTO METER(ID_METER, METER_NAME, LAST_UPDATE) values ('2', 'Meter 2',now());
    INSERT INTO METER(ID_METER, METER_NAME, LAST_UPDATE) values ('3', 'Meter 3',now());
    INSERT INTO METER(ID_METER, METER_NAME, LAST_UPDATE) values ('4', 'Meter 4',now());
    INSERT INTO METER(ID_METER, METER_NAME, LAST_UPDATE) values ('5', 'Meter 5',now());
    INSERT INTO METER(ID_METER, METER_NAME, LAST_UPDATE) values ('6', 'Meter 6',now());
    INSERT INTO METER(ID_METER, METER_NAME, LAST_UPDATE) values ('7', 'Meter 7',now());
    INSERT INTO METER(ID_METER, METER_NAME, LAST_UPDATE) values ('8', 'Meter 8',now());
    INSERT INTO METER(ID_METER, METER_NAME, LAST_UPDATE) values ('9', 'Meter 9',now());
    INSERT INTO METER(ID_METER, METER_NAME, LAST_UPDATE) values ('10', 'Meter 10',now());
    
EOSQL