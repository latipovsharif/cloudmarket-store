create table cash_config_options
(
    id             TEXT not null,
    name           TEXT not null,
    description    TEXT,
    value          TEXT not null,
    cash_config_id TEXT,
    unique (id, cash_config_id) on conflict replace
);

create table cash_configs
(
    id   TEXT not null
        unique
            on conflict replace,
    name TEXT
);

create table configs
(
    id  integer
        constraint configs_pk
            primary key autoincrement,
    key text,
    val text
);

create table product_migration
(
    current_version int
);

create table products
(
    id          text not null
        constraint products_pk
            primary key,
    name        text,
    barcode     text,
    article     text,
    origin      text,
    description text,
    price       real default 0 not null,
    discount    real default 0 not null
);

create unique index products_barcode_uindex
    on products (barcode);

create unique index products_id_uindex
    on products (id);

create table sold
(
    id            integer
        primary key autoincrement,
    document_hash text not null,
    seller_id     text,
    discount_type text,
    card_paid     double default 0 not null,
    cash_paid     double default 0 not null,
    to_pay        double default 0 not null,
    remained      double default 0 not null,
    change        double default 0 not null
);

create unique index sold_document_hash_uqx_index
    on sold (document_hash);

create table sold_details
(
    id               integer not null
        constraint sold_details_pk
            primary key autoincrement,
    sold_id          text    not null,
    product_id       text    not null,
    sell_price       double  default 0 not null,
    quantity         double  default 0 not null,
    discount_percent integer default 0,
    barcode          text
);

