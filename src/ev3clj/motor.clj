(ns ev3clj.motor
  (:import lejos.nxt.EV3)
  (:require [clojure.core.async :refer :all]
            [clojure.string :refer [lower-case]]
            [overtone.at-at :refer :all]))

(declare getMotorController)

(defn poll
  "Gets the latest values associated with this motor. The EV3 motor is
usually identified with a string containing a single letter, like
A,B,C or D"
  [motor]
  (let [controller (getMotorController motor)]
    (printf "poll called for motor %s" motor)
    {:tachoCount (.getTachoCount controller)
     ;; :position (.getPosition controller)
     ;; :isMoving (.isMoving controller)
     ;; :acceleration (.getAcceleration controller)
     ;; :limitAngle (.getLimitAngle controller)
     ;; :speed (.getSpeed controller)
     ;; :isStalled (.isStalled controller)
     :timestamp (System/currentTimeMillis)
     }))

(def motor-threadpool (mk-pool))

(defn publishToChannel
  "Poll the given motor and push the result to the given channel"
  [motor chan]
  (>!! chan (poll motor)))

(defn make-channel
  "Creates a channel that gets updated every 'period' milliseconds
  with status from the given motor"
  [period motor]
  (let [channel (chan 100)]
    (printf "Starting to poll motor %s at period %d\n" motor period)
    (every period #(publishToChannel motor channel) motor-threadpool)
    channel))

(defn setSpeed!
  "Sets speed of the motor"
  [motor speed]
  (let [controller (getMotorController motor)]
    (.setSpeed controller speed)))

(defn setAcceleration!
  "Sets acceleration of the motor"
  [motor acceleration]
  (let [controller (getMotorController motor)]
    (.setAcceleration controller acceleration)))

(defn forward! 
  "Makes this motor move forward"
  [motor]
  (let [controller (getMotorController motor)]
    (.forward controller)))

(defn backward!
  "Makes this motor move backward"
  [motor]
  (let [controller (getMotorController motor)]
        (.backward controller)))

(defn stop!
  "Makes this motor stop"
  [motor]
  (let [controller (getMotorController motor)]
    (.stop controller)))

(defn flt! 
  "Makes this motor float"
  [motor]
  (let [controller (getMotorController motor)]
    (.flt controller)))

(defn- getPortNumberFromString 
  "Given a motor name like \"A\", return the port number, e.g. 0"
  [motorName]
  (-> motorName
      (lower-case)
      (first)
      (int)
      (- (int \a))))

; Have to use reflection to get the Motor because initializing the
; lejos.nxt.Motor class tries to initialize the motor at compile time,
; which obviously does not work unless youre compiling this on an EV3

(def LOCAL_EV3_CLASS "lejos.nxt.LocalEV3")
(def LOCAL_EV3_GET_LOCAL_EV3 "getLocalEV3")

(defn- getLocalEV3 []
  "Gets the local ev3 class that can be used to get access to motors,
sensors, etc."
  (let [clazz (Class/forName LOCAL_EV3_CLASS)]
    (-> clazz
        (.getDeclaredMethod LOCAL_EV3_GET_LOCAL_EV3 nil)
        (.invoke nil nil))))

(defn getMotorController [motorName]
  "Given a motor name, returns a function that can be used to get the
low-level Java object that controls the motor"
  (fn []
    (let [ev3 (getLocalEV3)
          motor (getPortNumberFromString motorName)]
      (.getMotor ev3 motor))))

;; some test code
(comment
  (def left "B")
  (forward! left)
  (flt! left)
  (stop! left))
