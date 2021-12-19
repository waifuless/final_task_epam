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

create table category
(
    category_id   int auto_increment
        primary key,
    category_name varchar(64) not null,
    constraint category_category_name_uindex
        unique (category_name)
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

create table city_or_district
(
    city_or_district_id   int auto_increment
        primary key,
    city_or_district_name varchar(60) not null,
    region_id             int         not null,
    constraint city_or_district_region_region_id_fk
        foreign key (region_id) references region (region_id)
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
    email         varchar(254)                not null,
    password_hash char(60)                    not null,
    role_id       int                         not null,
    banned        tinyint(1)     default 0    not null,
    cash_account  decimal(20, 2) default 0.00 not null,
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
    start_datetime       datetime       not null,
    end_datetime         datetime       not null,
    initial_price        decimal(20, 2) not null,
    description          text           not null,
    auction_status_id    int            not null,
    product_condition_id int            not null,
    region_id            int            not null,
    city_or_district_id  int            not null,
    constraint lot_auction_type_type_id_fk
        foreign key (auction_type_id) references auction_type (type_id),
    constraint lot_city_or_district_city_or_district_id_fk
        foreign key (city_or_district_id) references city_or_district (city_or_district_id),
    constraint lot_fk0
        foreign key (owner_id) references app_user (user_id)
            on delete cascade,
    constraint lot_fk1
        foreign key (category_id) references category (category_id)
            on delete cascade,
    constraint lot_lot_product_condition_product_condition_id_fk
        foreign key (product_condition_id) references product_condition (product_condition_id),
    constraint lot_lot_status_status_id_fk
        foreign key (auction_status_id) references auction_status (status_id),
    constraint lot_region_region_id_fk
        foreign key (region_id) references region (region_id)
);

create table auction_participation
(
    participant_id      int            not null,
    lot_id              int            not null,
    deposit             decimal(20, 2) not null,
    deposit_is_returned tinyint(1)     not null,
    primary key (participant_id, lot_id),
    constraint ` auction_participant_app_user_user_id_fk`
        foreign key (participant_id) references app_user (user_id),
    constraint ` auction_participant_lot_lot_id_fk`
        foreign key (lot_id) references lot (lot_id)
);

create table bid
(
    bid_id  int auto_increment
        primary key,
    user_id int            not null,
    amount  decimal(20, 2) not null,
    lot_id  int            not null,
    constraint bid_fk0
        foreign key (user_id) references app_user (user_id),
    constraint bid_fk1
        foreign key (lot_id) references lot (lot_id)
);

create table lot_image
(
    image_id    int auto_increment
        primary key,
    lot_id      int                          null,
    image_path  varchar(256)                 not null,
    main_image  tinyint(1) default 0         not null,
    insert_date date       default curdate() not null,
    constraint lot_image_image_path_uindex
        unique (image_path),
    constraint lot_image_lot_lot_id_fk
        foreign key (lot_id) references lot (lot_id)
            on delete set null
);

create
    definer = epamTaskUser@localhost function findAuctionStatusId(searching_status_name varchar(60)) returns int
BEGIN

    RETURN (SELECT status_id FROM auction_status WHERE status_name = searching_status_name LIMIT 1);

END;

create
    definer = epamTaskUser@localhost function findAuctionTypeId(searching_type_name varchar(60)) returns int
BEGIN

    RETURN (SELECT type_id FROM auction_type WHERE type_name = searching_type_name LIMIT 1);

END;

create
    definer = epamTaskUser@localhost function findCategoryId(searching_category_name varchar(60)) returns int
BEGIN

    RETURN (SELECT category_id FROM category WHERE category_name = searching_category_name LIMIT 1);

END;

create
    definer = epamTaskUser@localhost function findCityOrDistrictId(searching_city_or_district_name varchar(60),
                                                                   searching_region_id int) returns int
BEGIN

    RETURN (SELECT city_or_district_id
            FROM city_or_district
            WHERE city_or_district_name =
                  searching_city_or_district_name
              AND region_id = searching_region_id LIMIT 1);

END;

create
    definer = epamTaskUser@localhost function findProductConditionId(searching_condition_name varchar(60)) returns int
BEGIN

    RETURN (SELECT product_condition_id
            FROM product_condition
            WHERE product_condition_name = searching_condition_name
            LIMIT 1);

END;

create
    definer = epamTaskUser@localhost function findRegionId(searching_region_name varchar(60)) returns int
BEGIN

    RETURN (SELECT region_id FROM region WHERE region_name = searching_region_name LIMIT 1);

END;

