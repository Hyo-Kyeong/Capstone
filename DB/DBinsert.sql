
insert into CARD(card, expiration_month, expiration_year, cvc) values('111122223333', 11, 22, 123);
insert into CARD(card, expiration_month, expiration_year, cvc) values('444455556666', 12, 21, 342);
insert into CARD(card, expiration_month, expiration_year, cvc) values('777788889999', 9, 20, 654);
insert into CARD(card, expiration_month, expiration_year, cvc) values('000022223333', 4, 25, 776);


insert into SPP_USER(id, pw, name, phone) values('root', 'root', '관리자', '01012344321');
insert into SPP_USER(id, pw, name, phone) values('nbgreen', 'hihi', '김효경', '01055554444');
insert into SPP_USER(id, pw, name, phone) values('apple', 'apple', '김사과', '01022213323');
insert into SPP_USER(id, pw, name, phone) values('banana', 'banana', '반아나', '01056789087');
insert into SPP_USER(id, pw, name, phone) values('orange', 'orange', '오렌지', '01032345465');
insert into SPP_USER(id, pw, name, phone, card, car) values('blue', 'blue', '이파랑', '01029393847', '111122223333', '88가1234');
insert into SPP_USER(id, pw, name, phone, card, car) values('capstone', 'capstone', '최스톤', '01056473829', '444455556666', '54수1255');
insert into SPP_USER(id, pw, name, phone, card, car) values('sejong', 'sejong', '박세종', '01029386654', '777788889999', '67우8988');
insert into SPP_USER(id, pw, name, phone, card, car) values('spp', 'spp', '시스템', '01055678902', '000022223333', '31나5522');

insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-05-18 12:00:11', 12000, 6, '111122223333', '88가1234', 'twosome');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-05-18 12:05:31', 8000, 6, '111122223333', '88가1234', 'sejong');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-05-19 20:00:23', 12500, 6, '111122223333', '88가1234', 'seoul');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-04-27 08:11:23', 3000, 7, '444455556666', '54수1255', 'twosome');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-05-20 12:23:34', 6500, 7, '444455556666', '54수1255', 'subway');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-05-28 23:27:18', 23000, 7, '444455556666', '54수1255', 'gongcha');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-04-18 17:18:48', 19500, 8, '777788889999', '67우8988', 'game');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-05-02 07:07:55', 2800, 8, '777788889999', '67우8988', 'busan');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-05-07 14:55:22', 52100, 8, '777788889999', '67우8988', 'samsung');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-06-11 12:27:26', 2500, 9, '000022223333', '31나5522', 'apple');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-07-12 12:51:37', 62100, 9, '000022223333', '31나5522', 'lg');
insert into PAYMENT_HISTORY(pay_date, payment, user_id, card, car, place) values('2021-07-20 13:44:52', 5500, 9, '000022223333', '31나5522', 'bugerking');



