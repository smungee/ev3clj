(ns ev3clj.ev3-simulator
  (:import lejos.nxt.EV3)
  (:import lejos.robotics.RegulatedMotor))

(def motors (atom {}))

(def default-motor-state {:speed 0 :tacho-count 0 :is-moving false})

(defn set-motor-state!
  "Sets the motor state to a new value"
  [motor new-state]
  (printf "set-motor-state!: Setting motor %s to %s\n" motor new-state)
  (swap! motors assoc motor new-state))

(defn create-motor!
  "Creates a new motor"
  [motor]
  (set-motor-state! motor default-motor-state)
  default-motor-state)

(defn get-motor-state
  "Given a motor, get the current motor state. If this is the first
  time this motor is being used, initialize it."
  [motor]
  (when (nil? (find @motors motor))
    (create-motor! motor))
  (get @motors motor))

(defn simulated-motor
  "Returns a mock instance of RegulatedMotor, useful for testing"
  [motor]
  (let [motor-state (get-motor-state motor)
        sim-motor (reify RegulatedMotor
                    (setSpeed [this speed]
                      (let [start-time (System/currentTimeMillis)
                            new-motor-state (assoc motor-state
                                              :speed speed)]
                        (set-motor-state! motor new-motor-state)))
                    (getSpeed [this]
                      (:speed motor-state))
                    (isMoving [this]
                      (:is-moving motor-state))
                    (forward [this]
                      (let [start-time (System/currentTimeMillis)
                            new-motor-state (assoc motor-state
                                              :start-time start-time
                                              :is-moving true)]
                        (set-motor-state! motor new-motor-state)))
                    (getTachoCount [this]
                      (:tacho-count motor-state)))]    
    sim-motor))


;;;;(defn- get-local-ev3 []
;;;;  "Gets the local ev3 class that can be used to get access to motors,
;;;;sensors, etc."
;;;;  (let [clazz (Class/forName LOCAL_EV3_CLASS)]
;;;;    (-> clazz
;;;;        (.getDeclaredMethod LOCAL_EV3_GET_LOCAL_EV3 nil)
;;;;        (.invoke nil nil))))
