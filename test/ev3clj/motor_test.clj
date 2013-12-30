(ns ev3clj.motor-test
  (:require [clojure.test :refer :all]
            [ev3clj.motor :refer :all]
            [clojure.core.async :as async])
  (:import lejos.robotics.RegulatedMotor))

(defn mock-getMotorController
  "Returns a mock instance of RegulatedMotor, useful for testing"
  [motor]
  (let [counter (atom 0N)
        mockMotor (reify RegulatedMotor
                   (forward [this]
                     (printf "Forward called for motor %s " motor))
                   (getTachoCount [this]
                     (swap! counter + 200)
                     @counter))]
    mockMotor))

(deftest test-motor-ops
  (testing "Testing motor forward! and poll"
    (with-redefs [getMotorController mock-getMotorController]
      (is (= (forward! "B") nil))
      (is (= (:tachoCount (poll "B")) 200)))))

(comment
  (with-redefs [getMotorController mock-getMotorController]
    (poll "B")
    (let [channel (make-channel 1000 "B")]
      (loop []
        (println (<!! channel))
        (Thread/sleep 1000)
        (recur)))))
