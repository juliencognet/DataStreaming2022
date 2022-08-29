#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE TABLE METER(
        ID_METER  varchar, METER_NAME  varchar 
    );
    INSERT INTO METER(ID_METER, METER_NAME) values ('1', 'Meter 1');
    INSERT INTO METER(ID_METER, METER_NAME) values ('2', 'Meter 2');
    INSERT INTO METER(ID_METER, METER_NAME) values ('3', 'Meter 3');
    INSERT INTO METER(ID_METER, METER_NAME) values ('4', 'Meter 4');
    INSERT INTO METER(ID_METER, METER_NAME) values ('5', 'Meter 5');
    INSERT INTO METER(ID_METER, METER_NAME) values ('6', 'Meter 6');
    INSERT INTO METER(ID_METER, METER_NAME) values ('7', 'Meter 7');
    INSERT INTO METER(ID_METER, METER_NAME) values ('8', 'Meter 8');
    INSERT INTO METER(ID_METER, METER_NAME) values ('9', 'Meter 9');
    INSERT INTO METER(ID_METER, METER_NAME) values ('10', 'Meter 10');
    
EOSQL