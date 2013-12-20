(ns ev3clj.core)

;(defn left [] (lejos.nxt.Motor/B))
;(defn right [] (lejos.nxt.Motor/C))

(declare left)
(declare right)
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
