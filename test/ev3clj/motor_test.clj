(ns ev3clj.motor-test
  (:require [clojure.test :refer :all]
            [ev3clj.motor :refer :all])
  (:import lejos.robotics.RegulatedMotor))

;;;(defn mock-getMotorObject
;;;  [motor]
;;;  (let [listener (reify RegulatedMotor
;;;                   (addListener [_] ()))]
;;;    listener))
;;;
;;;(deftest test
;;;  (testing "Mapping motor letter to numbers"
;;;    (is (= (getPortNumberFromString 1)))))
