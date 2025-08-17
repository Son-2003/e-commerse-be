INSERT INTO product (
    name, price, is_best_seller, description, status, image, category, sub_category
)
VALUES (
           'Women Round Neck Cotton Top',
           100,
           false,
           'A lightweight, usually knitted, pullover shirt, close-fitting and with a round neckline and short sleeves, worn as an undershirt or outer garment.',
           'ACTIVE',
           'https://res.cloudinary.com/dpysbryyk/image/upload/v1754811583/E-commerse/bartal4n3fjkcphtn7gz.png',
           'WOMEN',
           'TOP'
       ),(
           'Men Round Neck Pure Cotton T-shirt',
           200,
           false,
           'A lightweight, usually knitted, pullover shirt, close-fitting and with a round neckline and short sleeves, worn as an undershirt or outer garment.',
           'ACTIVE',
           'https://res.cloudinary.com/dpysbryyk/image/upload/v1754811599/E-commerse/sdnyuboiucs3hok6ltnj.png,https://res.cloudinary.com/dpysbryyk/image/upload/v1754811600/E-commerse/hrs7fu3eerc9wr8pjwls.png,https://res.cloudinary.com/dpysbryyk/image/upload/v1754811600/E-commerse/ihydn4c61jwklr4cbhtj.png,https://res.cloudinary.com/dpysbryyk/image/upload/v1754811600/E-commerse/dmhnffiyfeqfip2nhzwu.png,https://res.cloudinary.com/dpysbryyk/image/upload/v1754811602/E-commerse/v3h8zuqfafh7ehfvfyor.png',
           'MEN',
           'TOP'
       ),(
           'Girls Round Neck Cotton Top',
           220,
           false,
           'A lightweight, usually knitted, pullover shirt, close-fitting and with a round neckline and short sleeves, worn as an undershirt or outer garment.',
           'ACTIVE',
           'https://res.cloudinary.com/dpysbryyk/image/upload/v1754811608/E-commerse/o92s3f3tnnqfzlupyue3.png',
           'KIDS',
           'TOP'
       ),(
           'Men Round Neck Pure Cotton T-shirt',
           110,
           false,
           'A lightweight, usually knitted, pullover shirt, close-fitting and with a round neckline and short sleeves, worn as an undershirt or outer garment.',
           'ACTIVE',
           'https://res.cloudinary.com/dpysbryyk/image/upload/v1754811608/E-commerse/btzl3ievxsdtyzbldtpg.png',
           'MEN',
           'TOP'
       ),(
           'Women Round Neck Cotton Top',
           130,
           false,
           'A lightweight, usually knitted, pullover shirt, close-fitting and with a round neckline and short sleeves, worn as an undershirt or outer garment.',
           'ACTIVE',
           'https://res.cloudinary.com/dpysbryyk/image/upload/v1754811608/E-commerse/j9vtgaodlwmieox1s89k.png',
           'WOMEN',
           'TOP'
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


INSERT INTO "user" (email, full_name, image, password, phone, status, role)
VALUES ('nguyenducson@gmail.com',  'Đức Sơn',
        'https://res.cloudinary.com/dpysbryyk/image/upload/v1717827115/Milk/UserDefault/dfzhxjcbnixmp8aybnge.jpg',
        '$2a$10$AoQP4YPkiYzqFOvwH8toyujM2Aje6p5l3dbL2/uJblcpMyWpSMJdq', '1111111111', 'ACTIVE', 'CUSTOMER')