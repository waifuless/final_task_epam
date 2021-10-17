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

CREATE FUNCTION insertCityIfNotExistAndSelectId(new_city_name VARCHAR(60)) RETURNS INT

BEGIN

    IF NOT EXISTS(SELECT 1 FROM city WHERE city_name = new_city_name) THEN
        INSERT INTO city(city_name) VALUE (new_city_name);
    END IF;
    RETURN (SELECT city_id FROM city WHERE city_name = new_city_name);

END //

DELIMITER ;



DELIMITER //

CREATE FUNCTION insertRegionIfNotExistAndSelectId(new_region_name VARCHAR(60)) RETURNS INT

BEGIN

    IF NOT EXISTS(SELECT 1 FROM region WHERE region_name = new_region_name) THEN
        INSERT INTO region(region_name) VALUE (new_region_name);
    END IF;
    RETURN (SELECT region_id FROM region WHERE region_name = new_region_name);

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
