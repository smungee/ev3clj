(ns ev3clj.ev3-simulator
  (:import lejos.nxt.EV3)
  (:import lejos.robotics.RegulatedMotor))

(def motors (atom {}))

(defn get-motor-state
  "Given a motor, get the current motor state"
  [motor]
  (def new-motor-value {:speed 0 :tacho-count 0})
  (when-let [current-motor (find @motors motor)]
    (def new-motor-value
      (assoc new-motor-value :tacho-count  (+ (:tacho-count (val current-motor)) 100))))
  (swap! motors assoc motor new-motor-value)
  (get @motors motor))

(defn simulated-motor
  "Returns a mock instance of RegulatedMotor, useful for testing"
  [motor]
  (let [sim-motor (reify RegulatedMotor
                    (forward [this]
                      (printf "Forward called for motor %s " motor))
                    (getTachoCount [this]
                      (:tacho-count (get-motor-state motor))))]
    sim-motor))


;;;;(defn- get-local-ev3 []
;;;;  "Gets the local ev3 class that can be used to get access to motors,
;;;;sensors, etc."
;;;;  (let [clazz (Class/forName LOCAL_EV3_CLASS)]
;;;;    (-> clazz
;;;;        (.getDeclaredMethod LOCAL_EV3_GET_LOCAL_EV3 nil)
;;;;        (.invoke nil nil))))
