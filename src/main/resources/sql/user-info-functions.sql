USE racingSite;

DELIMITER //

CREATE FUNCTION insertAddressIfNotExistAndSelectId(new_address_name VARCHAR(128)) RETURNS INT

BEGIN

    IF NOT EXISTS(SELECT 1 FROM address WHERE address_name = new_address_name) THEN
        INSERT INTO address(address_name) VALUE (new_address_name);
    END IF;
    RETURN (SELECT address_id FROM address WHERE address_name = new_address_name);

END //

DELIMITER ;






DELIMITER //

CREATE FUNCTION findRegionId(searching_region_name VARCHAR(60)) RETURNS INT

BEGIN

    RETURN (SELECT region_id FROM region WHERE region_name = searching_region_name);

END //

DELIMITER ;







DELIMITER //

CREATE FUNCTION findCityOrDistrictId(searching_city_or_district_name VARCHAR(60)) RETURNS INT

BEGIN

    RETURN (SELECT city_or_district_id FROM city_or_district WHERE city_or_district_name =
                                                                   searching_city_or_district_name);

END //

DELIMITER ;









DELIMITER //

CREATE FUNCTION insertPostalCodeIfNotExistAndSelectId(new_postal_code_name VARCHAR(16)) RETURNS INT

BEGIN

    IF NOT EXISTS(SELECT 1 FROM postal_code WHERE postal_code_name = new_postal_code_name) THEN
        INSERT INTO postal_code(postal_code_name) VALUE (new_postal_code_name);
    END IF;
    RETURN (SELECT postal_code_id FROM postal_code WHERE postal_code_name = new_postal_code_name);

END //

DELIMITER ;
