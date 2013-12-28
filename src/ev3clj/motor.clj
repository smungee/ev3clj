(ns ev3clj.motor
  (:import lejos.nxt.EV3)
  (:require [clojure.string :refer [lower-case]]))

(declare getMotorController)

(defn poll [motor]
  "Gets the latest values associated with this motor. The EV3 motor is
usually identified with a string containing a single letter, like
A,B,C or D"
  (let [controller (getMotorController motor)]
    {:tachoCount (.getTachoCount controller)
     ;; :position (.getPosition controller)
     ;; :isMoving (.isMoving controller)
     ;; :acceleration (.getAcceleration controller)
     ;; :limitAngle (.getLimitAngle controller)
     ;; :speed (.getSpeed controller)
     ;; :isStalled (.isStalled controller)
     ;; :timestamp (System/currentTimeMillis)
     }))

(defn setSpeed! [motor speed]
  "Sets speed of the motor"
  (let [controller (getMotorController motor)]
    (.setSpeed controller speed)))

(defn setAcceleration! [motor acceleration]
  "Sets acceleration of the motor"
  (let [controller (getMotorController motor)]
    (.setAcceleration controller acceleration)))

(defn forward! [motor]
  "Makes this motor move forward"
  (let [controller (getMotorController motor)]
    (.forward controller)))

(defn backward! [motor]
  "Makes this motor move backward"
  (let [controller (getMotorController motor)]
        (.backward controller)))

(defn stop! [motor]
  "Makes this motor stop"
  (let [controller (getMotorController motor)]
    (.stop controller)))

(defn flt! [motor]
  "Makes this motor float"
  (let [controller (getMotorController motor)]
    (.flt controller)))

(defn- getPortNumberFromString [motorName]
  "Given a motor name like \"A\", return the port number, e.g. 0"
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
