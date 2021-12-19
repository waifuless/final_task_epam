USE racingSite;

DELIMITER //

CREATE FUNCTION findRegionId(searching_region_name VARCHAR(60)) RETURNS INT

BEGIN

    RETURN (SELECT region_id FROM region WHERE region_name = searching_region_name LIMIT 1);

END //

DELIMITER ;


DELIMITER //

CREATE FUNCTION findCityOrDistrictId(searching_city_or_district_name VARCHAR(60),
                                     searching_region_id INT(11)) RETURNS INT

BEGIN

    RETURN (SELECT city_or_district_id
            FROM city_or_district
            WHERE city_or_district_name =
                  searching_city_or_district_name
              AND region_id = searching_region_id LIMIT 1);

END //

DELIMITER ;


