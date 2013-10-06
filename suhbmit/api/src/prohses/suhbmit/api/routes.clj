(ns prohses.suhbmit.api.routes
  (:require [compojure.route :as route]
            [compojure.core :refer [GET OPTIONS POST PUT context defroutes]]
            [prohses.suhbmit.api.resources :as resources]))

(defroutes projects
  (context "/projects" []
           (GET "/" {} resources/projects)
    (context "/:project" [project]
      (GET "/" {} resources/project))))

(defroutes api
  (context "/api" []
    (OPTIONS "/" {} "API Options")
    (GET "/" {} "API Base")
    projects
    (route/not-found "404 Try Again")))
