(ns ev3clj.motor
  (:import lejos.nxt.EV3)
  (:require [clojure.core.async :refer [<!! >!! chan]]
            [clojure.string :refer [lower-case]]
            [overtone.at-at :refer :all]
            [ev3clj.ev3-simulator :refer :all]))

(declare get-motor-controller)

(defn poll
  "Gets the latest values associated with this motor. The EV3 motor is
usually identified with a string containing a single letter, like
A,B,C or D"
  [motor]
  (let [controller (get-motor-controller motor)]
    (printf "motor.poll: called for motor %s\n" motor)
    {:tacho-count (.getTachoCount controller)
     ;; :position (.getPosition controller)
     :isMoving (.isMoving controller)
     ;; :acceleration (.getAcceleration controller)
     ;; :limitAngle (.getLimitAngle controller)
     :speed (.getSpeed controller)
     ;; :isStalled (.isStalled controller)
     :timestamp (System/currentTimeMillis)
     }))

(def motor-threadpool (mk-pool))

(defn publish-to-channel
  "Poll the given motor and push the result to the given channel"
  [motor chan]
  (>!! chan (poll motor)))

(defn make-channel
  "Creates a channel that gets updated every 'period' milliseconds
  with status from the given motor"
  [period motor]
  (let [channel (chan 100)]
    (printf "Starting to poll motor %s at period %d\n" motor period)
    (every period #(publish-to-channel motor channel) motor-threadpool)
    channel))

(defn setSpeed!
  "Sets speed of the motor"
  [motor speed]
  (let [controller (get-motor-controller motor)]
    (.setSpeed controller speed)))

(defn setAcceleration!
  "Sets acceleration of the motor"
  [motor acceleration]
  (let [controller (get-motor-controller motor)]
    (.setAcceleration controller acceleration)))

(defn forward!
  "Makes this motor move forward"
  [motor]
  (let [controller (get-motor-controller motor)]
    (.forward controller)))

(defn backward!
  "Makes this motor move backward"
  [motor]
  (let [controller (get-motor-controller motor)]
    (.backward controller)))

(defn stop!
  "Makes this motor stop"
  [motor]
  (let [controller (get-motor-controller motor)]
    (.stop controller)))

(defn flt!
  "Makes this motor float"
  [motor]
  (let [controller (get-motor-controller motor)]
    (.flt controller)))

(defn- get-port-number-from-string
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

(defn- get-local-ev3 []
  "Gets the local ev3 class that can be used to get access to motors,
sensors, etc."
  (let [clazz (Class/forName LOCAL_EV3_CLASS)]
    (-> clazz
        (.getDeclaredMethod LOCAL_EV3_GET_LOCAL_EV3 nil)
        (.invoke nil nil))))

(defn get-motor-controller [motorName]
  "Given a motor name, returns a function that can be used to get the
low-level Java object that controls the motor"
  (let [motor (get-port-number-from-string motorName)]
    (try
      (let [ev3 (get-local-ev3)
            ]
        (.getMotor ev3 motor))
      (catch Throwable t
        (printf "Warning: Caught exception: \"%s\", returning simulated motor\n" (.getMessage t))
        (simulated-motor motor)))))

;; some test code
(comment
  (def left "B")
  (forward! left)
  (setSpeed! left 400)
  (poll left)
  (flt! left)
  (stop! left)
  (make-channel 1000 left))
