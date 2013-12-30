(defproject ev3 "0.1.0-SNAPSHOT"
  :description "Idiomatic Clojure API for the LEGO Mindstorms EV3"
  :url "http://github.com/smungee/ev3clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :uberjar-name "ev3clj.jar"
  :main ev3clj.core
  :dependencies [[overtone/at-at "1.2.0"]
                 [org.clojure/clojure "1.5.1"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [org.clojars.earthlingzephyr/lejos-ev3 "0.4.0"]]
  :plugins [[lein-shell "0.3.0"]])
