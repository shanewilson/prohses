(ns prohses.suhbmit.api.core
  (:require [prohses.suhbmit.api.handler :refer [server]])
  (:gen-class))

(defn -main
  [cmd]
  (cond
   (= cmd "server") (server)))
