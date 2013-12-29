(ns ev3clj.core)

; Have to use reflection to get the Motor because initializing the
; lejos.nxt.Motor class tries to initialize the motor at compile time,
; which obviously does not work unless youre compiling this on an EV3
(defn getMotor [motor]
  (let [clazz (Class/forName "lejos.nxt.Motor")]
    (.getField clazz motor)))

;; B and C are labelled ports on the EV3
(defn left [] (getMotor "B"))
(defn right [] (getMotor "C"))

(defn setSpeed
  "Sets speed for the given motor"
  ([motor speed]
     (.setSpeed motor speed))
  ([speed]
     (.setSpeed left speed)
     (.setSpeed right speed)))


(defn forward! [motor]
  (.forward motor))

(defn backward! [motor]
  (.backward motor))

(defn stopmotor! [motor]
  (.stop motor))

(defn forward
  ([speed]
     (setSpeed left speed)
     (setSpeed right speed)
     (forward! left)
     (forward! right))
  ([left_speed right_speed]
     (setSpeed left left_speed)
     (setSpeed right right_speed)
     (forward! left)
     (forward! right)))


(defn backward [speed]
;  (map #(setSpeed %1 speed) [left right])
;  (map #(setSpeed %1 speed) [left right])
  (setSpeed left speed)
  (setSpeed right speed)
  (backward! left)
  (backward! right))

(defn stop []
  (stopmotor! left)
  (stopmotor! right))


(defn wait [millisec]
  (Thread/sleep (* millisec 1000)))

(defn turn [angle]
  (do
    (.rotate left (* angle (/ 900 90)))))

(defn goBack [speed]
 (forward speed)
 (wait 10)
 (stop)
 (turn 180)
 (forward speed)
 (wait 10)
 (stop))
