(ns ev3clj.motor-test
  (:require [clojure.test :refer :all]
            [ev3clj.motor :refer :all]
            [clojure.core.async :as async])
  (:import lejos.robotics.RegulatedMotor))

(def mock-tacho-counters (atom {}))

(defn get-tacho-counter
  "Given a motor, get the current tacho count"
  [motor]
  (def new-tacho-value 0)
  (when-let [current-tacho-count (find @mock-tacho-counters motor)]
    (def new-tacho-value (+ (val current-tacho-count) 100)))
  (swap! mock-tacho-counters assoc motor new-tacho-value)
  (get @mock-tacho-counters motor))

(defn mock-get-motor-controller
  "Returns a mock instance of RegulatedMotor, useful for testing"
  [motor]
  (let [mockMotor (reify RegulatedMotor
                    (forward [this]
                      (printf "Forward called for motor %s " motor))
                    (getTachoCount [this]
                      (get-tacho-counter motor)))]
    mockMotor))

(deftest test-motor-ops
  (testing "Testing motor forward! and poll"
    (with-redefs [get-motor-controller mock-get-motor-controller]
      (is (= (forward! "B") nil))
      (is (>= (:tacho-count (poll "B")) 0)))))

(comment
  (with-redefs [get-motor-controller mock-get-motor-controller]
    (poll "B")
    (let [channel (make-channel 1000 "B")]
      (loop []
        (println (async/<!! channel))
        (Thread/sleep 1000)
        (recur)))))
