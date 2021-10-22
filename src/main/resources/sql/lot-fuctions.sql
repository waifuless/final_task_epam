DELIMITER //

CREATE FUNCTION findCategoryId(searching_category_name VARCHAR(60)) RETURNS INT

BEGIN

    RETURN (SELECT category_id FROM category WHERE category_name = searching_category_name);

END //

DELIMITER ;




DELIMITER //

CREATE FUNCTION findAuctionTypeId(searching_type_name VARCHAR(60)) RETURNS INT

BEGIN

    RETURN (SELECT type_id FROM auction_type WHERE type_name = searching_type_name);

END //

DELIMITER ;





DELIMITER //

CREATE FUNCTION findAuctionStatusId(searching_status_name VARCHAR(60)) RETURNS INT

BEGIN

    RETURN (SELECT status_id FROM auction_status WHERE status_name = searching_status_name);

END //

DELIMITER ;






DELIMITER //

CREATE FUNCTION findProductConditionId(searching_condition_name VARCHAR(60)) RETURNS INT

BEGIN

    RETURN (SELECT product_condition_id FROM product_condition WHERE product_condition_name = searching_condition_name);

END //

DELIMITER ;