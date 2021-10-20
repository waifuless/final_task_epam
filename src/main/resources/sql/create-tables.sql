create or replace table address
(
    address_id int auto_increment
        primary key,
    address_name varchar(128) not null,
    constraint address_address_value_uindex
        unique (address_name)
);

create or replace table auction_status
(
    status_id int auto_increment
        primary key,
    status_name varchar(20) not null,
    constraint lot_status_status_value_uindex
        unique (status_name)
);

create or replace table auction_type
(
    type_id int auto_increment
        primary key,
    type_name varchar(20) not null,
    constraint auction_type_type_value_uindex
        unique (type_name)
);

create or replace table cart_item_status
(
    status_id int auto_increment
        primary key,
    status_name varchar(20) not null,
    constraint cart_item_status_status_value_uindex
        unique (status_name)
);

create or replace table category
(
    category_id int auto_increment
        primary key,
    category_name varchar(64) not null
);

create or replace table city_or_district
(
    city_or_district_id int auto_increment
        primary key,
    city_or_district_name varchar(60) not null,
    constraint city_city_name_uindex
        unique (city_or_district_name)
);

create or replace table postal_code
(
    postal_code_id int auto_increment
        primary key,
    postal_code_name varchar(16) not null,
    constraint postal_code_postal_code_name_uindex
        unique (postal_code_name)
);

create or replace table product_condition
(
    product_condition_id int auto_increment
        primary key,
    product_condition_name varchar(20) not null,
    constraint lot_product_condition_product_condition_value_uindex
        unique (product_condition_name)
);

create or replace table region
(
    region_id int auto_increment
        primary key,
    region_name varchar(60) not null,
    constraint region_region_name_uindex
        unique (region_name)
);

create or replace table role
(
    role_id int auto_increment
        primary key,
    role_name varchar(20) not null,
    constraint role_role_name_uindex
        unique (role_name)
);

create or replace table app_user
(
    user_id int auto_increment
        primary key,
    email varchar(254) not null,
    password_hash char(60) not null,
    role_id int not null,
    constraint email
        unique (email),
    constraint password_hash
        unique (password_hash),
    constraint app_user_role_role_id_fk
        foreign key (role_id) references role (role_id)
);

create or replace table lot
(
    lot_id int auto_increment
        primary key,
    owner_id int not null,
    category_id int not null,
    auction_type_id int not null,
    title varchar(256) not null,
    start_date date not null,
    end_date date not null,
    initial_price decimal(20,2) not null,
    origin_place varchar(128) not null,
    description text not null,
    auction_status_id int not null,
    product_condition_id int not null,
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

create or replace table bid
(
    bid_id int auto_increment
        primary key,
    user_id int not null,
    amount decimal(20,2) not null,
    lot_id int not null,
    datetime datetime not null,
    constraint bid_fk0
        foreign key (user_id) references app_user (user_id),
    constraint bid_fk1
        foreign key (lot_id) references lot (lot_id)
);

create or replace table cart_item
(
    bid_id int not null
        primary key,
    origin_lot_title varchar(256) not null,
    status_id int not null,
    constraint cart_item_cart_item_status_status_id_fk
        foreign key (status_id) references cart_item_status (status_id),
    constraint cart_item_fk0
        foreign key (bid_id) references bid (bid_id)
);

create or replace table lot_image
(
    image_id int auto_increment
        primary key,
    lot_id int not null,
    image_value blob not null,
    main_image tinyint(1) default 0 not null,
    constraint lot_image_fk0
        foreign key (lot_id) references lot (lot_id)
);

create or replace table user_info
(
    user_id int auto_increment
        primary key,
    phone_number varchar(15) not null,
    first_name varchar(64) not null,
    last_name varchar(64) not null,
    address_id int not null,
    city_or_district_id int not null,
    region_id int not null,
    postal_code_id int not null,
    constraint city_fk
        foreign key (city_or_district_id) references city_or_district (city_or_district_id),
    constraint user_info_address_address_id_fk
        foreign key (address_id) references address (address_id),
    constraint user_info_fk0
        foreign key (user_id) references app_user (user_id),
    constraint user_info_postal_code_postal_code_id_fk
        foreign key (postal_code_id) references postal_code (postal_code_id),
    constraint user_info_region_region_id_fk
        foreign key (region_id) references region (region_id)
);

create or replace definer = epamTaskUser@localhost function findCityOrDistrictId(searching_city_or_district_name varchar(60)) returns int
BEGIN

    RETURN (SELECT city_or_district_id FROM city_or_district WHERE city_or_district_name =
                                                                   searching_city_or_district_name);

END;

create or replace definer = epamTaskUser@localhost function findRegionId(searching_region_name varchar(60)) returns int
BEGIN

    RETURN (SELECT region_id FROM region WHERE region_name = searching_region_name);

END;

create or replace definer = epamTaskUser@localhost function insertAddressIfNotExistAndSelectId(new_address_name varchar(128)) returns int
BEGIN

    IF NOT EXISTS(SELECT 1 FROM address WHERE address_name = new_address_name) THEN
        INSERT INTO address(address_name) VALUE (new_address_name);
    END IF;
    RETURN (SELECT address_id FROM address WHERE address_name = new_address_name);

END;

create or replace definer = epamTaskUser@localhost function insertPostalCodeIfNotExistAndSelectId(new_postal_code_name varchar(16)) returns int
BEGIN

    IF NOT EXISTS(SELECT 1 FROM postal_code WHERE postal_code_name = new_postal_code_name) THEN
        INSERT INTO postal_code(postal_code_name) VALUE (new_postal_code_name);
    END IF;
    RETURN (SELECT postal_code_id FROM postal_code WHERE postal_code_name = new_postal_code_name);

END;

