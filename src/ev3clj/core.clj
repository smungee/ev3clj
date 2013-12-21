(ns ev3clj.core)

; Have to use reflection because loading lejos.nxt.Motor does not work
; unless youre running on an EV3
(defn getMotorUsingReflection [motor]
  (let [clazz (Class/forName "lejos.nxt.Motor")]
    (.getField clazz motor)))

(def left lejos.nxt.Motor/B)
(def right lejos.nxt.Motor/C)  

(defn setSpeed
  ([motor speed]
     (println (str "Set speed to " speed))
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
 

 
 
 
