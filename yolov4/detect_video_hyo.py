import os
# comment out below line to enable tensorflow outputs
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
import time
import tensorflow as tf
physical_devices = tf.config.experimental.list_physical_devices('GPU')
if len(physical_devices) > 0:
    tf.config.experimental.set_memory_growth(physical_devices[0], True)
from absl import app, flags, logging
from absl.flags import FLAGS
import core.utils_bak_h as utils
from core.yolov4 import filter_boxes
from core.functions_hyo import *
from tensorflow.python.saved_model import tag_constants
from PIL import Image
import cv2
import numpy as np
from tensorflow.compat.v1 import ConfigProto
from tensorflow.compat.v1 import InteractiveSession
import pytesseract
import re

flags.DEFINE_string('framework', 'tf', '(tf, tflite, trt')
flags.DEFINE_string('weights', './checkpoints/yolov4-416',
                    'path to weights file')
flags.DEFINE_integer('size', 416, 'resize images to')
flags.DEFINE_boolean('tiny', False, 'yolo or yolo-tiny')
flags.DEFINE_string('model', 'yolov4', 'yolov3 or yolov4')
flags.DEFINE_string('video', './data/video/video.mp4', 'path to input video or set to 0 for webcam')
flags.DEFINE_string('output', None, 'path to output video')
flags.DEFINE_string('output_format', 'XVID', 'codec used in VideoWriter when saving video to file')
flags.DEFINE_float('iou', 0.45, 'iou threshold')
flags.DEFINE_float('score', 0.50, 'score threshold')
flags.DEFINE_boolean('count', False, 'count objects within video')
flags.DEFINE_boolean('dont_show', False, 'dont show video output')
flags.DEFINE_boolean('info', False, 'print info on detections')
flags.DEFINE_boolean('crop', False, 'crop detections from images')
flags.DEFINE_boolean('plate', False, 'perform license plate recognition')




class Flag:
    def __init__(self):
        self.info = False
        self.model = 'yolov4'
        self.tiny = False
        self.weights = './checkpoints/custom'
        self.size = 416
        self.video = './data/video/'
        self.plate = True
        self.iou = 0.45
        self.score = 0.50
        self.crop = False
        self.output = None
        self.output_format = 'XVID'


def detectPlate(path):

    prevCarPlateNum = ''
    carPlateNum = ''
    cnt = 0
    maxCnt = 0


    config = ConfigProto()
    config.gpu_options.allow_growth = True
    session = InteractiveSession(config=config)
    flag = Flag()
    STRIDES, ANCHORS, NUM_CLASS, XYSCALE = utils.load_config()
    input_size = flag.size
    video_path = flag.video + path
    # get video name by using split method
    video_name = video_path.split('/')[-1]
    video_name = video_name.split('.')[0]

    saved_model_loaded = tf.saved_model.load(flag.weights, tags=[tag_constants.SERVING])
    infer = saved_model_loaded.signatures['serving_default']

    # begin video capture
    try:
        vid = cv2.VideoCapture(int(video_path))
    except:
        vid = cv2.VideoCapture(video_path)

    out = None
    #
    # if flag.output:
    #     print("cap start")
    #     # by default VideoCapture returns float instead of int
    #     width = int(vid.get(cv2.CAP_PROP_FRAME_WIDTH))
    #     height = int(vid.get(cv2.CAP_PROP_FRAME_HEIGHT))
    #     fps = int(vid.get(cv2.CAP_PROP_FPS))
    #     codec = cv2.VideoWriter_fourcc(*flag.output_format)
    #     flag.output = flag.output + video_name
    #     out = cv2.VideoWriter(flag.output, codec, fps, (width, height))
    #     print("cap end")

    frame_num = 0
    while True:
        return_value, frame = vid.read()
        if return_value:
            frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
            frame_num += 1
            image = Image.fromarray(frame)
        else:
            print('Video has ended or failed, try a different video format!')
            break
    
        frame_size = frame.shape[:2]
        image_data = cv2.resize(frame, (input_size, input_size))
        image_data = image_data / 255.
        image_data = image_data[np.newaxis, ...].astype(np.float32)
        start_time = time.time()

        batch_data = tf.constant(image_data)
        pred_bbox = infer(batch_data)
        for key, value in pred_bbox.items():
            boxes = value[:, :, 0:4]
            pred_conf = value[:, :, 4:]

        boxes, scores, classes, valid_detections = tf.image.combined_non_max_suppression(
            boxes=tf.reshape(boxes, (tf.shape(boxes)[0], -1, 1, 4)),
            scores=tf.reshape(
                pred_conf, (tf.shape(pred_conf)[0], -1, tf.shape(pred_conf)[-1])),
            max_output_size_per_class=50,
            max_total_size=50,
            iou_threshold=flag.iou,
            score_threshold=flag.score
        )

        # format bounding boxes from normalized ymin, xmin, ymax, xmax ---> xmin, ymin, xmax, ymax
        original_h, original_w, _ = frame.shape
        bboxes = utils.format_boxes(boxes.numpy()[0], original_h, original_w)

        pred_bbox = [bboxes, scores.numpy()[0], classes.numpy()[0], valid_detections.numpy()[0]]

        # read in all class names from config
        class_names = utils.read_class_names(cfg.YOLO.CLASSES)

        # by default allow all classes in .names file
        allowed_classes = list(class_names.values())
        
        # custom allowed classes (uncomment line below to allow detections for only people)
        #allowed_classes = ['person']

        # if crop flag is enabled, crop each detection and save it as new image
        if flag.crop:
            crop_rate = 150 # capture images every so many frames (ex. crop photos every 150 frames)
            crop_path = os.path.join(os.getcwd(), 'detections', 'crop', video_name)
            try:
                os.mkdir(crop_path)
            except FileExistsError:
                pass
            if frame_num % crop_rate == 0:
                final_path = os.path.join(crop_path, 'frame_' + str(frame_num))
                try:
                    os.mkdir(final_path)
                except FileExistsError:
                    pass
                crop_image = crop_objects(cv2.cvtColor(frame, cv2.COLOR_BGR2RGB), pred_bbox, final_path, allowed_classes)
                print(OCR(crop_image))
            else:
                pass


        image = utils.draw_bbox(frame, pred_bbox, flag.info, allowed_classes=allowed_classes, read_plate=flag.plate)
        
        fps = 1.0 / (time.time() - start_time)
        result = np.asarray(image)
        cv2.namedWindow("result", cv2.WINDOW_AUTOSIZE)
        result = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)
        
        cv2.imshow("result", result)
        crop_image = crop_objects(cv2.cvtColor(frame, cv2.COLOR_BGR2RGB), pred_bbox, 'path', allowed_classes)


        plate_num = OCR(crop_image)

        if plate_num == prevCarPlateNum:
            cnt = cnt + 1
            if cnt > maxCnt:
                maxCnt = cnt;
                carPlateNum = plate_num
        else:
            cnt = 0

        prevCarPlateNum = plate_num

        print('max = ', maxCnt, 'carPlateNum = ', carPlateNum)
        if cv2.waitKey(1) & 0xFF == ord('q'): break
    cv2.destroyAllWindows()


def OCR(image):
    box = image

    # grayscale region within bounding box
    gray = cv2.cvtColor(box, cv2.COLOR_RGB2GRAY)
    # resize image to three times as large as original for better readability
    gray = cv2.resize(gray, None, fx=3, fy=3, interpolation=cv2.INTER_CUBIC)
    # perform gaussian blur to smoothen image
    blur = cv2.GaussianBlur(gray, (5, 5), 0)
    # cv2.imshow("Gray", gray)
    # cv2.waitKey(0)
    # threshold the image using Otsus method to preprocess for tesseract
    ret, thresh = cv2.threshold(gray, 0, 255, cv2.THRESH_OTSU | cv2.THRESH_BINARY_INV)
    # cv2.imshow("Otsu Threshold", thresh)
    # cv2.waitKey(0)
    # create rectangular kernel for dilation
    rect_kern = cv2.getStructuringElement(cv2.MORPH_RECT, (5, 5))
    # apply dilation to make regions more clear
    dilation = cv2.dilate(thresh, rect_kern, iterations=1)
    # cv2.imshow("Dilation", dilation)
    # cv2.waitKey(0)
    # find contours of regions of interest within license plate
    # cv2.imshow('plate', dilation)


    try:
        contours, hierarchy = cv2.findContours(dilation, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    except:
        ret_img, contours, hierarchy = cv2.findContours(dilation, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    # sort contours left-to-right
    sorted_contours = sorted(contours, key=lambda ctr: cv2.boundingRect(ctr)[0])
    # create copy of gray image
    im2 = gray.copy()
    # perfrom bitwise not to flip image to black text on white background
    roi = cv2.bitwise_not(dilation)
    # perform another blur on character region
    roi = cv2.medianBlur(roi, 5)
    cv2.imshow('roi', roi)
    cv2.waitKey(0)
    plate_num = ""
    try:
        text = pytesseract.image_to_string(roi, lang='kor', config='--psm 8 --oem 3')
        # clean tesseract text by removing any unwanted blank spaces
        clean_text = re.sub('[\W_]+', '', text)
        clean_text = re.sub('[-=+,#/\?:^$.@*\"※~&%ㆍ!』\\‘|\(\)\[\]\<\>`\'…》]', '', clean_text)
        plate_num += clean_text
    except:
        text = None


    '''
    try:
        contours, hierarchy = cv2.findContours(dilation, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    except:
        ret_img, contours, hierarchy = cv2.findContours(dilation, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
        # sort contours left-to-right
    sorted_contours = sorted(contours, key=lambda ctr: cv2.boundingRect(ctr)[0])
    # create copy of gray image
    im2 = gray.copy()
    # create blank string to hold license plate number
    plate_num = ""
    # loop through contours and find individual letters and numbers in license plate
    for cnt in sorted_contours:
        x, y, w, h = cv2.boundingRect(cnt)
        height, width = im2.shape
        print(height/float(h))
        # if height of box is not tall enough relative to total height then skip
        # if height / float(h) > 6: continue
        # ratio = w / float(h)
        # # if height to width ratio is less than 1.5 skip
        # if ratio < 1.5: continue
        # # if width is not wide enough relative to total width then skip
        # if width / float(w) > 15: continue
        #
        # area = h * w
        # # if area is less than 100 pixels skip
        # if area < 100: continue
        # draw the rectangle
        rect = cv2.rectangle(im2, (x, y), (x + w, y + h), (0, 255, 0), 2)
        # grab character region of image
        roi = thresh[y - 5:y + h + 5, x - 5:x + w + 5]
        # perfrom bitwise not to flip image to black text on white background
        roi = cv2.bitwise_not(roi)
        cv2.imshow('roi', roi)
        cv2.waitKey(0)
        # perform another blur on character region
        roi = cv2.medianBlur(roi, 5)
        try:
            text = pytesseract.image_to_string(roi, lang='kor', config='--psm 8 --oem 3')
            # clean tesseract text by removing any unwanted blank spaces
            clean_text = re.sub('[\W_]+', '', text)
            print(clean_text)
            plate_num += clean_text
        except:
            text = None
    '''

    if plate_num != None:
        print("License Plate #: ", plate_num)

    if plate_num[-1] < '0' or plate_num[-1] > '9':
        plate_num = plate_num[:-1]

    return plate_num





if __name__ == '__main__':
    detectPlate('test_car_Trim.mp4')
    # try:
    #     app.run(main)
    # except SystemExit:
    #     pass
