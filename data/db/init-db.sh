#!/bin/bash
set -e

echo "About to initialize the database with some data"

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  create table public.customers (
    customer_id BIGSERIAL,
    name varchar(255) not null,
    address varchar(8000) not null,
    shipping_address varchar(8000) not null
  );

  insert into public.customers(name, address, shipping_address)
  select 'Loves Nature Inc', '123 Main Street, Arlington, VA 22204', '123 Main Street, Arlington, VA 22204' union all
  select 'Hates Coal LLC', '716 Hunter Place, Folsom, CA 95630', '716 Hunter Place, Folsom, CA 95630' union all
  select 'Greenest of Energies', '2000 K Street NW, 12th Floor, Washington, DC, 20006', '55 Bishopsgate, 2nd Floor, London, England EC2N 3AS';

  create table public.product_categories (
    product_category_id BIGSERIAL,
    name varchar(255) not null,
    quality_tier int not null
  );

  insert into public.product_categories(name, quality_tier)
  select 'Bronze', 1 union all
  select 'Silver', 2 union all
  select 'Gold', 3;
EOSQL

echo "Done initializing the database!"
