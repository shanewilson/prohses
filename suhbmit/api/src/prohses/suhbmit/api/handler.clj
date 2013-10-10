(ns prohses.suhbmit.api.handler
  (:require [ring.middleware.json :refer [wrap-json-response]]
            [org.httpkit.server :refer [run-server]]
            [compojure.handler :as handler]
            [prohses.suhbmit.api.routes :as routes]))

(def app
  (-> routes/api
      handler/api
      wrap-json-response))

(defn server
  [& args]
  (run-server app {:port 8080}))
