import os
# comment out below line to enable tensorflow outputs
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
import tensorflow as tf
physical_devices = tf.config.experimental.list_physical_devices('GPU')
if len(physical_devices) > 0:
    tf.config.experimental.set_memory_growth(physical_devices[0], True)
from absl import app, flags, logging
from absl.flags import FLAGS
import core.utils as utils
from core.yolov4 import filter_boxes
from core.functions import *
from tensorflow.python.saved_model import tag_constants
from PIL import Image
import cv2
import numpy as np
from tensorflow.compat.v1 import ConfigProto
from tensorflow.compat.v1 import InteractiveSession
import pytesseract
import re

flags.DEFINE_string('framework', 'tf', '(tf, tflite, trt')
flags.DEFINE_string('weights', '/Users/sangmin/Desktop/캡스톤/Capstone/yolov4/checkpoints/custom/saved_model.pb',
                    'path to weights file')
flags.DEFINE_integer('size', 416, 'resize images to')
flags.DEFINE_boolean('tiny', False, 'yolo or yolo-tiny')
flags.DEFINE_string('model', 'yolov4', 'yolov3 or yolov4')
flags.DEFINE_list('images', './data/images/', 'path to input image')
flags.DEFINE_string('output', './detections/', 'path to output folder')
flags.DEFINE_float('iou', 0.45, 'iou threshold')
flags.DEFINE_float('score', 0.50, 'score threshold')
flags.DEFINE_boolean('count', False, 'count objects within images')
flags.DEFINE_boolean('dont_show', False, 'dont show image output')
flags.DEFINE_boolean('info', False, 'print info on detections')
flags.DEFINE_boolean('crop', True, 'crop detections from images')
flags.DEFINE_boolean('ocr', False, 'perform generic OCR on detection regions')
flags.DEFINE_boolean('plate', True, 'perform license plate recognition')

class Flag:
    def __init__(self):
        self.model = 'yolov4'
        self.tiny = False
        self.weights = './checkpoints/custom'
        self.size = 416
        self.images = './data/images/'
        self.plate = True
        self.iou = 0.45
        self.score = 0.50
        self.crop = True
    
def detectPlate(path):
    config = ConfigProto()
    config.gpu_options.allow_growth = True
    session = InteractiveSession(config=config)
    flag = Flag()
    STRIDES, ANCHORS, NUM_CLASS, XYSCALE = utils.load_config()
    input_size = flag.size
    images = flag.images
    image_path = images + path
    # load model
    saved_model_loaded = tf.saved_model.load(flag.weights, tags=[tag_constants.SERVING])

    # loop through images in list and run Yolov4 model on each
    original_image = cv2.imread(image_path)
    cv2.imshow('image', original_image)
    cv2.waitKey(0)
    original_image = cv2.cvtColor(original_image, cv2.COLOR_BGR2RGB)

    image_data = cv2.resize(original_image, (input_size, input_size))
    image_data = image_data / 255.

    # get image name by using split method
    image_name = image_path.split('/')[-1]
    image_name = image_name.split('.')[0]

    images_data = []
    for i in range(1):
        images_data.append(image_data)
    images_data = np.asarray(images_data).astype(np.float32)

    infer = saved_model_loaded.signatures['serving_default']
    batch_data = tf.constant(images_data)
    pred_bbox = infer(batch_data)
    for key, value in pred_bbox.items():
        boxes = value[:, :, 0:4]
        pred_conf = value[:, :, 4:]

    # run non max suppression on detections
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
    original_h, original_w, _ = original_image.shape
    bboxes = utils.format_boxes(boxes.numpy()[0], original_h, original_w)

    # hold all detection data in one variable
    pred_bbox = [bboxes, scores.numpy()[0], classes.numpy()[0], valid_detections.numpy()[0]]

    # read in all class names from config
    class_names = utils.read_class_names(cfg.YOLO.CLASSES)

    # by default allow all classes in .names file
    allowed_classes = list(class_names.values())

    # custom allowed classes (uncomment line below to allow detections for only people)
    #allowed_classes = ['person']

    # if crop flag is enabled, crop each detection and save it as new image
    if flag.crop:
        crop_path = os.path.join(os.getcwd(), 'detections', 'crop', image_name)
        try:
            os.mkdir(crop_path)
        except FileExistsError:
            pass
        image = crop_objects(cv2.cvtColor(original_image, cv2.COLOR_BGR2RGB), pred_bbox, crop_path, allowed_classes)
        print(OCR(image))

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
    cv2.imshow('plate', dilation)


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
    plate_num = ""
    cv2.waitKey(0)
    try:
        text = pytesseract.image_to_string(roi, lang='kor', config='--psm 8 --oem 3')
        # clean tesseract text by removing any unwanted blank spaces
        clean_text = re.sub('[\W_]+', '', text)
        plate_num += clean_text
    except:
        text = None


    '''
    # *******************************************8
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

        # # if height of box is not tall enough relative to total height then skip
        # if height / float(h) > 6: continue

        #ratio = w / float(h)
        # if height to width ratio is less than 1.5 skip
        #if ratio < 2.0: continue

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

    #**********************************
    '''

    if plate_num != None:
        print("License Plate #: ", plate_num)

    return plate_num


if __name__ == '__main__':
    # image=cv2.imread('./data/images/car6(2).png')
    # OCR(image)
    detectPlate('car6.png')
