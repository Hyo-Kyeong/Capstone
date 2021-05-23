
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
def self_pay(payment, place):
    sql = "insert into PAYMENT_HISTORY (pay_date, payment, place) values (now(), %s, %s)"
    curs.execute(sql, (payment, place))
    conn.commit()
