
import numpy as np
import pymysql
from datetime import datetime
import cv2

# mysql 연결
conn = pymysql.connect(
    host = 'spp.cvpwanalnvoc.ap-northeast-2.rds.amazonaws.com',
    user = 'admin',
    passwd = 'capstone',
    db = 'SPP',
    port = 3306,
    charset = 'utf8'
)

# DB에 참조하기 위한 객체
curs = conn.cursor()

#1. 사업장-결제
# (결제시간, 금액, 유저아이디(pk), 카드, 자동차, 장소)
#(1-1) 수동결제
def self_pay(payment, car, place):
    if car == "":
        sql = "insert into PAYMENT_HISTORY (pay_date, payment, place) values (now(), %s, %s)"
        curs.execute(sql, (payment, place))
    else:
        sql = "select user_id, card from SPP_USER where car=%s"
        curs.execute(sql, car)
        conn.commit()
        userInfo = curs.fetchall()
        print(userInfo)

        if len(userInfo) == 0:
            print("not user")
            sql = "insert into PAYMENT_HISTORY (pay_date, payment, car, place) values (now(), %s, %s, %s)"
            curs.execute(sql, (payment, car, place))
        elif userInfo[0][1] == None:
            print("no card")
            return False
        else:
            print("user")
            sql = "insert into PAYMENT_HISTORY (pay_date, payment, user_id, card, car, place) values (now(), %s, %s, %s, %s, %s)"
            curs.execute(sql, (payment, userInfo[0][0], userInfo[0][1], car, place))
    conn.commit()
    return True

#2. 사업장-결제내역 가져오기
def load_company_payment_history(company_name):
    sql = "select pay_date, payment, car, place from PAYMENT_HISTORY where place=%s"
    curs.execute(sql, company_name)
    conn.commit()
    return curs.fetchall()
