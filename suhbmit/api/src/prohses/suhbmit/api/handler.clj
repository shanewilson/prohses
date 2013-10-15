(ns prohses.suhbmit.api.handler
  (:require [ring.middleware.json :refer [wrap-json-response]]
            [ring.adapter.jetty :refer [run-jetty]]
            [compojure.handler :as handler]
            [prohses.suhbmit.api.routes :as routes]))

(def app
  (-> routes/api
      handler/api))

(defn server
  ""
  []
  (run-jetty #'app {:port 3000}))
