create table address
(
    address_id   int auto_increment
        primary key,
    address_name varchar(128) not null,
    constraint address_address_value_uindex
        unique (address_name)
);

create table auction_status
(
    status_id   int auto_increment
        primary key,
    status_name varchar(20) not null,
    constraint lot_status_status_value_uindex
        unique (status_name)
);

create table auction_type
(
    type_id   int auto_increment
        primary key,
    type_name varchar(20) not null,
    constraint auction_type_type_value_uindex
        unique (type_name)
);

create table cart_item_status
(
    status_id   int auto_increment
        primary key,
    status_name varchar(20) not null,
    constraint cart_item_status_status_value_uindex
        unique (status_name)
);

create table category
(
    category_id        int auto_increment
        primary key,
    parent_category_id int         null,
    category_name      varchar(64) not null,
    constraint category_fk0
        foreign key (parent_category_id) references category (category_id)
);

create table city
(
    city_id   int auto_increment
        primary key,
    city_name varchar(60) not null,
    constraint city_city_name_uindex
        unique (city_name)
);

create table extra_attr_name
(
    attr_id     int auto_increment
        primary key,
    category_id int                                        not null,
    attr_name   varchar(64)                                not null,
    attr_type   enum ('bool', 'int', 'decimal', 'varchar') not null,
    constraint extra_attr_name_fk0
        foreign key (category_id) references category (category_id)
);

create table postal_code
(
    postal_code_id   int auto_increment
        primary key,
    postal_code_name varchar(16) not null,
    constraint postal_code_postal_code_name_uindex
        unique (postal_code_name)
);

create table product_condition
(
    product_condition_id   int auto_increment
        primary key,
    product_condition_name varchar(20) not null,
    constraint lot_product_condition_product_condition_value_uindex
        unique (product_condition_name)
);

create table region
(
    region_id   int auto_increment
        primary key,
    region_name varchar(60) not null,
    constraint region_region_name_uindex
        unique (region_name)
);

create table role
(
    role_id   int auto_increment
        primary key,
    role_name varchar(20) not null,
    constraint role_role_name_uindex
        unique (role_name)
);

create table app_user
(
    user_id       int auto_increment
        primary key,
    email         varchar(254) not null,
    password_hash char(60)     not null,
    role_id       int          not null,
    constraint email
        unique (email),
    constraint password_hash
        unique (password_hash),
    constraint app_user_role_role_id_fk
        foreign key (role_id) references role (role_id)
);

create table lot
(
    lot_id               int auto_increment
        primary key,
    owner_id             int            not null,
    category_id          int            not null,
    auction_type_id      int            not null,
    title                varchar(256)   not null,
    start_date           date           not null,
    end_date             date           not null,
    initial_price        decimal(20, 2) not null,
    origin_place         varchar(128)   not null,
    description          varchar(1024)  not null,
    auction_status_id    int            not null,
    product_condition_id int            not null,
    constraint lot_auction_type_type_id_fk
        foreign key (auction_type_id) references auction_type (type_id),
    constraint lot_fk0
        foreign key (owner_id) references app_user (user_id),
    constraint lot_fk1
        foreign key (category_id) references category (category_id),
    constraint lot_lot_product_condition_product_condition_id_fk
        foreign key (product_condition_id) references product_condition (product_condition_id),
    constraint lot_lot_status_status_id_fk
        foreign key (auction_status_id) references auction_status (status_id)
);

create table bid
(
    bid_id   int auto_increment
        primary key,
    user_id  int            not null,
    amount   decimal(20, 2) not null,
    lot_id   int            not null,
    datetime datetime       not null,
    constraint bid_fk0
        foreign key (user_id) references app_user (user_id),
    constraint bid_fk1
        foreign key (lot_id) references lot (lot_id)
);

create table cart_item
(
    bid_id           int          not null
        primary key,
    origin_lot_title varchar(256) not null,
    status_id        int          not null,
    constraint cart_item_cart_item_status_status_id_fk
        foreign key (status_id) references cart_item_status (status_id),
    constraint cart_item_fk0
        foreign key (bid_id) references bid (bid_id)
);

create table extra_attr_value
(
    lot_id      int          not null,
    attr_id     int          not null,
    bool_val    tinyint(1)   null,
    int_val     int          null,
    decimal_val decimal      null,
    varchar_val varchar(128) null,
    primary key (lot_id, attr_id),
    constraint extra_attr_value_fk0
        foreign key (lot_id) references lot (lot_id),
    constraint extra_attr_value_fk1
        foreign key (attr_id) references extra_attr_name (attr_id)
);

create table lot_image
(
    image_id      int auto_increment
        primary key,
    lot_id        int          not null,
    path_to_image varchar(128) not null,
    constraint lot_image_fk0
        foreign key (lot_id) references lot (lot_id)
);

create table user_info
(
    user_id        int auto_increment
        primary key,
    phone_number   varchar(15) not null,
    first_name     varchar(64) not null,
    last_name      varchar(64) not null,
    address_id     int         not null,
    city_id        int         not null,
    region_id      int         not null,
    postal_code_id int         not null,
    constraint city_fk
        foreign key (city_id) references city (city_id),
    constraint user_info_address_address_id_fk
        foreign key (address_id) references address (address_id),
    constraint user_info_fk0
        foreign key (user_id) references app_user (user_id),
    constraint user_info_postal_code_postal_code_id_fk
        foreign key (postal_code_id) references postal_code (postal_code_id),
    constraint user_info_region_region_id_fk
        foreign key (region_id) references region (region_id)
);