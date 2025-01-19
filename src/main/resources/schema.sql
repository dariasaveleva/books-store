CREATE TABLE IF NOT EXISTS books
(
    book_id     INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title       VARCHAR(100) NOT NULL,
    author      VARCHAR(30)  NOT NULL,
    price       DECIMAL,
    isPresent   BOOLEAN      NOT NULL
);

CREATE TABLE IF NOT EXISTS customers
(
    customer_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name        VARCHAR(35) NOT NULL,
    surname     VARCHAR(50) NOT NULL,
    email       VARCHAR(50) NOT NULL,
    phone       VARCHAR(11) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders
(
    order_id    INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    customer_id INT NOT NULL,
    books       VARCHAR(255) NOT NULL,
    createdDate TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    sum         decimal
);
