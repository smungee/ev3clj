(ns ev3clj.nav
  (:import lejos.nxt.EV3)
  (:import lejos.robotics.RegulatedMotorListener)
  (:require [clojure.string :refer [lower-case]]))

;; adding a mouse pressed callback to a Swing component:

(defn add-motor-listener
  [motor & args]
  (let [listener (proxy [RegulatedMotorListener] []
                     (rotationStarted [motor tachoCount stalled timestamp]
                       (printf "Started motor at %10d, tachocount = %d" timestamp tachoCount))
                     (rotationStopped [motor tachoCount stalled timestamp]
                       (println "Stopped motor at %10d, tachocount = %d" timestamp tachoCount)))
        controller (:controller motor)]
    (.addListener controller listener)
    listener))
