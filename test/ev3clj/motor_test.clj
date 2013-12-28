(ns ev3clj.motor-test
  (:require [clojure.test :refer :all]
            [ev3clj.motor :refer :all])
  (:import lejos.robotics.RegulatedMotor))

(defn mock-getMotorController
  "Returns a mock instance of RegulatedMotor, useful for testing"
  [motor]
  (let [mockMotor (reify RegulatedMotor
                   (forward [this]
                     (printf "Forward called for motor %s " motor)))]
    mockMotor))

(deftest test-forward
  (testing "Testing motor forward!"
    (with-redefs [getMotorController mock-getMotorController]
      (is (= (forward! "B") nil)))))
