(ns ev3clj.motor-test
  (:require [clojure.test :refer :all]
            [ev3clj.motor :refer :all]
            [clojure.core.async :as async]))

(deftest test-motor-ops
  (testing "Testing motor forward! and poll"
    (is (= (forward! "B") nil))
    (is (>= (:tacho-count (poll "B")) 0))))

