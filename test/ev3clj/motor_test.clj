(ns ev3clj.motor-test
  (:require [clojure.test :refer :all]
            [ev3clj.motor :refer :all])
  (:import lejos.robotics.RegulatedMotor))

(defn mock-getMotorController
  "Returns a mock instance of RegulatedMotor, useful for testing"
  [motor]
  (let [mockMotor (reify RegulatedMotor
                   (forward [this]
                     (printf "Forward called for motor %s " motor))
                   (getTachoCount [this]
                     200))]
    mockMotor))

(deftest test-motor-ops
  (testing "Testing motor forward! and poll"
    (with-redefs [getMotorController mock-getMotorController]
      (is (= (forward! "B") nil))
      (is (= (:tachoCount (poll "B")) 200)))))
