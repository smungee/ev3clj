(ns ev3clj.core)

; Have to use reflection because loading lejos.nxt.Motor does not work
; unless youre running on an EV3
(defn getMotorUsingReflection [motor]
  (let [clazz (Class/forName "lejos.nxt.Motor")]
    (.getField class motor)))

(defn left [] (getMotorUsingReflection "B"))
(defn right [] (getMotorUsingReflection "C"))  

(defn setSpeed [motor speed]
  (println (str "Set speed to " speed))
  (.setSpeed motor speed))

(defn startmotor [motor]
  (.forward motor))

(defn stopmotor [motor]
  (.stop motor))

(defn forward [speed]
;  (map #(setSpeed %1 speed) [left right])
;  (map #(setSpeed %1 speed) [left right])
  (setSpeed left speed)
  (setSpeed right speed)
  (map startmotor [left right]))

(defn stop []
  (map stopmotor [left right]))
