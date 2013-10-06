(ns prohses.suhbmit.api.core
  (:gen-class))

(defn -main
  [cmd]
  (cond
   (= cmd "server") (server/-main)))
