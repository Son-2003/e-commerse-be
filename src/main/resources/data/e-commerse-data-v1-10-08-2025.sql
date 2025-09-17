INSERT INTO product (
    name, price, is_best_seller, description, status, image, category, sub_category, stock, minimum_stock
)
VALUES (
           'Women Round Neck Cotton Top',
           100,
           false,
           'A lightweight, usually knitted, pullover shirt, close-fitting and with a round neckline and short sleeves, worn as an undershirt or outer garment.',
           'ACTIVE',
           'https://res.cloudinary.com/dpysbryyk/image/upload/v1754811583/E-commerse/products/bartal4n3fjkcphtn7gz.png',
           'WOMEN',
           'TOP',
           20,
           10
       ),(
           'Men Round Neck Pure Cotton T-shirt',
           200,
           false,
           'A lightweight, usually knitted, pullover shirt, close-fitting and with a round neckline and short sleeves, worn as an undershirt or outer garment.',
           'ACTIVE',
           'https://res.cloudinary.com/dpysbryyk/image/upload/v1754811602/E-commerse/products/v3h8zuqfafh7ehfvfyor.png,https://res.cloudinary.com/dpysbryyk/image/upload/v1754811600/E-commerse/products/dmhnffiyfeqfip2nhzwu.png,https://res.cloudinary.com/dpysbryyk/image/upload/v1754811600/E-commerse/products/ihydn4c61jwklr4cbhtj.png,https://res.cloudinary.com/dpysbryyk/image/upload/v1754811600/E-commerse/products/hrs7fu3eerc9wr8pjwls.png',
           'MEN',
           'TOP',
           20,
           10
       ),(
           'Girls Round Neck Cotton Top',
           220,
           false,
           'A lightweight, usually knitted, pullover shirt, close-fitting and with a round neckline and short sleeves, worn as an undershirt or outer garment.',
           'ACTIVE',
           'https://res.cloudinary.com/dpysbryyk/image/upload/v1754811608/E-commerse/products/btzl3ievxsdtyzbldtpg.png',
           'KIDS',
           'TOP',
           20,
           10
       ),(
           'Men Round Neck Pure Cotton T-shirt',
           110,
           false,
           'A lightweight, usually knitted, pullover shirt, close-fitting and with a round neckline and short sleeves, worn as an undershirt or outer garment.',
           'ACTIVE',
           'https://res.cloudinary.com/dpysbryyk/image/upload/v1754811608/E-commerse/products/o92s3f3tnnqfzlupyue3.png',
           'MEN',
           'TOP',
           20,
           10
       ),(
           'Women Round Neck Cotton Top',
           130,
           false,
           'A lightweight, usually knitted, pullover shirt, close-fitting and with a round neckline and short sleeves, worn as an undershirt or outer garment.',
           'ACTIVE',
           'https://res.cloudinary.com/dpysbryyk/image/upload/v1754811608/E-commerse/products/j9vtgaodlwmieox1s89k.png',
           'WOMEN',
           'TOP',
           20,
           10
       );



INSERT INTO size (
    name, quantity, product_id
)
VALUES (
           'S', 10, 1
       ),(
           'M', 12, 2
       ),(
           'L', 20, 3
       ),(
           'XL', 18, 4
       ),(
           'XS', 16, 5
       );


INSERT INTO "user" (email, full_name, image, password, phone, status, role, address)
VALUES ('nguyenducson2915@gmail.com',  'Đức Sơn',
        'https://res.cloudinary.com/dpysbryyk/image/upload/v1717827115/Milk/UserDefault/dfzhxjcbnixmp8aybnge.jpg',
        '$2a$10$AoQP4YPkiYzqFOvwH8toyujM2Aje6p5l3dbL2/uJblcpMyWpSMJdq', '1111111111', 'ACTIVE', 'ADMIN', 'abc')