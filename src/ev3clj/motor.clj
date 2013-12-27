(ns ev3clj.motor
  (:import lejos.nxt.EV3)
  (:require [clojure.string :refer [lower-case]]))


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

(defn getLocalEV3 []
  "Gets the local ev3 class that can be used to get access to motors,
sensors, etc."
  (let [clazz (Class/forName LOCAL_EV3_CLASS)]
    (-> clazz
        (.getDeclaredMethod LOCAL_EV3_GET_LOCAL_EV3 nil)
        (.invoke nil nil))))

(defn- getMotorObject [motorName]
  "Given a motor name, returns a function that can be used to get the
low-level Java object that controls the motor"
  (fn []
    (let [ev3 (getLocalEV3)
          motor (getPortNumberFromString motorName)]
      (.getMotor ev3 motor))))

(defn makeMotor [motorName]
  "Returns a map that represents a motor"
  {:name motorName
   :controller ((getMotorObject motorName))
   :speed 0
   :acceleration 0})

(defn updateMotor! [motor]
  "Updates the speed and acceleration of the given motor"
  (let [{:keys [speed acceleration controller]} motor]
    (.setSpeed controller speed)
    (.setAcceleration controller acceleration)))

(defn forward! [motor]
  "Makes this motor move forward"
  (let [{:keys [controller]} motor]
    (.forward controller)))

(defn backward! [motor]
  "Makes this motor move backward"
  (let [{:keys [controller]} motor]
    (.backward controller)))

(defn stop! [motor]
  "Makes this motor stop"
  (let [{:keys [controller]} motor]
    (.stop controller)))

(defn flt! [motor]
  "Makes this motor float"
  (let [{:keys [controller]} motor]
    (.flt controller)))

;; some test code
(comment
  (def left (makeMotor "B"))
  (def right (makeMotor "C"))
  (updateMotor! (assoc left :speed 90 :acceleration 6000))
  (forward! left)
  (flt! left)
  (stop! left))
