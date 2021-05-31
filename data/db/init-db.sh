#!/bin/bash
set -e

echo "About to initialize the database with some data"

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
create table hcpcs_codes (
  HCPC varchar(255) null,
  SEQNUM varchar(255) null,
  RECID varchar(255) null,
  LONG_DESCRIPTION varchar(8000) null,
  SHORT_DESCRIPTION varchar(255) null,
  PRICE1 varchar(255) null,
  PRICE2 varchar(255) null,
  PRICE3 varchar(255) null,
  PRICE4 varchar(255) null,
  MULT_PI varchar(255) null,
  CIM1 varchar(255) null,
  CIM2 varchar(255) null,
  CIM3 varchar(255) null,
  MCM1 varchar(255) null,
  MCM2 varchar(255) null,
  MCM3 varchar(255) null,
  STATUTE varchar(255) null,
  LABCERT1 varchar(255) null,
  LABCERT2 varchar(255) null,
  LABCERT3 varchar(255) null,
  LABCERT4 varchar(255) null,
  LABCERT5 varchar(255) null,
  LABCERT6 varchar(255) null,
  LABCERT7 varchar(255) null,
  LABCERT8 varchar(255) null,
  XREF1 varchar(255) null,
  XREF2 varchar(255) null,
  XREF3 varchar(255) null,
  XREF4 varchar(255) null,
  XREF5 varchar(255) null,
  COV varchar(255) null,
  ASC_GRP varchar(255) null,
  ASC_DT varchar(255) null,
  OPPS varchar(255) null,
  OPPS_PI varchar(255) null,
  OPPS_DT varchar(255) null,
  PROCNOTE varchar(255) null,
  BETOS varchar(255) null,
  TOS1 varchar(255) null,
  TOS2 varchar(255) null,
  TOS3 varchar(255) null,
  TOS4 varchar(255) null,
  TOS5 varchar(255) null,
  ANEST_BU varchar(255) null,
  ADD_DT varchar(255) null,
  ACT_EFF_DT varchar(255) null,
  TERM_DT varchar(255) null,
  ACTION_CD varchar(255) null
);

COPY hcpcs_codes(HCPC,SEQNUM,RECID,LONG_DESCRIPTION,SHORT_DESCRIPTION,PRICE1,PRICE2,PRICE3,PRICE4,MULT_PI,CIM1,CIM2,CIM3,MCM1,MCM2,MCM3,STATUTE,LABCERT1,LABCERT2,LABCERT3,LABCERT4,LABCERT5,LABCERT6,LABCERT7,LABCERT8,XREF1,XREF2,XREF3,XREF4,XREF5,COV,ASC_GRP,ASC_DT,OPPS,OPPS_PI,OPPS_DT,PROCNOTE,BETOS,TOS1,TOS2,TOS3,TOS4,TOS5,ANEST_BU,ADD_DT,ACT_EFF_DT,TERM_DT,ACTION_CD)
FROM '/tmp/hcpcs_codes_2018.csv'
DELIMITER ','
CSV HEADER;
EOSQL

echo "Done initializing the database!"
