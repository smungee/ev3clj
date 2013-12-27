(ns ev3clj.nav
  (:import lejos.nxt.EV3)
  (:import lejos.robotics.RegulatedMotorListener)
  (:require [clojure.string :refer [lower-case]]))

; adding a listener to a motor, so we can get called back when the
; motor moves, and we can keep a track of where the robot is.
(defn add-motor-listener
  [motor & args]
  (let [listener (reify RegulatedMotorListener
                   (rotationStarted [this motor tachoCount stalled timestamp]
                     (printf "Started motor at %10d, tachocount = %d" timestamp tachoCount))
                   (rotationStopped [this motor tachoCount stalled timestamp]
                     (printf "Stopped motor at %10d, tachocount = %d" timestamp tachoCount)))
        controller (:controller motor)]
    (.addListener controller listener)
    listener))

(defn remove-motor-listener
  [motor & args]
  (let [controller (:controller motor)]
    (.removeListener controller)))

(defn distance-to-degrees [distance diameter]
  "Calculate number of degrees of rotation required to travel the
given distance, given the circumference of a wheel. Distance and
circumference must be in the same unit, return is in number of
degrees."
  (let [circumference (* Math/PI diameter)
        degrees-per-cm (/ 360 circumference)]
    (* cm degrees-per-cm)))
