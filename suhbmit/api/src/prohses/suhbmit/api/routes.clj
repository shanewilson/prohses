(ns prohses.suhbmit.api.routes
  (:require [compojure.route :as route]
            [compojure.core :refer [GET OPTIONS POST PUT context defroutes]]
            [prohses.suhbmit.api.resources :as resources]))

(defroutes dictionary
  (context "/dictionaries" []
           (GET "/" {} resources/dictionaries)
           (context "/:version" []
                    (GET "/" {} resources/dictionary)
                    (GET "/releases" {} resources/dictionary))))

(defroutes projects
  (context "/projects" []
           (GET "/" {} resources/projects)
           (context "/:project" [project]
                    (GET "/" {} resources/project)
                    (GET "/releases" {} resources/project)
                    (GET "/releases/:release" {} resources/submission))))

(defroutes releases
  (context "/releases" []
           (GET "/" {} resources/releases)
           (context "/:release" [release]
                    (GET "/" {} resources/release)
                    (GET "/dictionary" {} resources/release)
                    (context "/projects" []
                             (GET "/" {} resources/submissions)
                             (context "/:project" [project]
                                      (GET "/" {} resources/submission))))))

(defroutes api
  (context "/api" []
           (OPTIONS "/" {} "API Options")
           (GET "/" {} "API Base")
           dictionary
           projects
           releases
           (route/not-found "404 Try Again")))
