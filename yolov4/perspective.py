import cv2

src = cv2.imread("./yolov4/data/images/car6(2).png")
dst = src.copy()
cv2.imshow("src",src)
gray = cv2.cvtColor(src, cv2.COLOR_RGB2GRAY)
corners = cv2.goodFeaturesToTrack(gray, 100, 0.01, 5, blockSize=3, useHarrisDetector=True, k=0.03)

for i in corners:
    cv2.circle(dst, tuple(i[0]), 3, (0, 0, 255), 2)


cv2.imshow("dst", dst)
cv2.waitKey(0)
cv2.destroyAllWindows()