
insert into CARD(card, expiration_month, expiration_year, cvc) values('1111222233334444', 11, 22, 123);
insert into CARD(card, expiration_month, expiration_year, cvc) values('4444555566667777', 12, 21, 342);
insert into CARD(card, expiration_month, expiration_year, cvc) values('7777888899990000', 9, 20, 654);
insert into CARD(card, expiration_month, expiration_year, cvc) values('0000222233335555', 4, 25, 776);
insert into CARD(card, expiration_month, expiration_year, cvc) values('6109333276534435', 10, 24, 283);
insert into CARD(card, expiration_month, expiration_year, cvc) values('3382717355942234', 5, 22, 109);
insert into CARD(card, expiration_month, expiration_year, cvc) values('3002844967507736', 8, 28, 442);

insert into SPP_USER(id, pw, name, phone, birth) values('root', 'root', '관리자', '01012344321', 19701201);
insert into SPP_USER(id, pw, name, phone, birth) values('nbgreen', 'hihi', '김효경', '01055554444', 19990614);
insert into SPP_USER(id, pw, name, phone, birth) values('apple', 'apple', '김사과', '01022213323', 19980223);
insert into SPP_USER(id, pw, name, phone, birth) values('banana', 'banana', '반아나', '01056789087', 20010809);
insert into SPP_USER(id, pw, name, phone, birth) values('orange', 'orange', '오렌지', '01032345465', 20001018);
insert into SPP_USER(id, pw, name, phone, birth, card, car) values('blue', 'blue', '이파랑', '01029393847', 20210417, '1111222233334444', '88가1234');
insert into SPP_USER(id, pw, name, phone, birth, card, car) values('capstone', 'capstone', '최스톤', '01056473829', 19970904, '4444555566667777', '54수1255');
insert into SPP_USER(id, pw, name, phone, birth, card, car) values('sejong', 'sejong', '박세종', '01029386654', 19961214, '7777888899990000', '67우8988');
insert into SPP_USER(id, pw, name, phone, birth, card, car) values('spp', 'spp', '시스템', '01055678902', 19950529, '0000222233335555', '31나5522');
insert into SPP_USER(id, pw, name, phone, birth, card, car) values('sejongCar', 'sejongCar', '정세카', 19880723, '19990707', '6109333276534435', '14루3012');
insert into SPP_USER(id, pw, name, phone, birth, card, car) values('sonnim', 'sonnim', '최손님', '01029934423', 19990502, '3382717355942234', '54구7399');
insert into SPP_USER(id, pw, name, phone, birth, card, car) values('gogack', 'gogcak', '나고객', '01033258907', 19780910, '3002844967507736', '24허0161');


insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-05-18 12:00:11', 12000, 6, '1111222233334444', '88가1234', 'twosome');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-05-18 12:05:31', 8000, 6, '1111222233334444', '88가1234', 'sejong');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-05-19 20:00:23', 12500, 6, '1111222233334444', '88가1234', 'seoul');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-04-27 08:11:23', 3000, 7, '4444555566667777', '54수1255', 'twosome');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-05-20 12:23:34', 6500, 7, '4444555566667777', '54수1255', 'subway');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-05-28 23:27:18', 23000, 7, '4444555566667777', '54수1255', 'gongcha');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-04-18 17:18:48', 19500, 8, '7777888899990000', '67우8988', 'game');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-05-02 07:07:55', 2800, 8, '7777888899990000', '67우8988', 'busan');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-05-07 14:55:22', 52100, 8, '7777888899990000', '67우8988', 'samsung');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-06-11 12:27:26', 2500, 9, '0000222233335555', '31나5522', 'apple');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-07-12 12:51:37', 62100, 9, '0000222233335555', '31나5522', 'lg');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-07-20 13:44:52', 5500, 9, '0000222233335555', '31나5522', 'bugerking');

insert into PAYMENT_HISTORY(pay_date, payment, car, place) values('2020-05-23 15:03:01', 22000, null, 'Sejong Cafe');
insert into PAYMENT_HISTORY(pay_date, payment, car, place) values(now(), 5500, '123가3333', 'Sejong Cafe');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-05-18 12:00:11', 12000, 6, '1111222233334444', '88가1234', 'Sejong Cafe');


