(ns prohses.suhbmit.api.resources
  (:require [liberator.core :refer [defresource request-method-in]]
            [prohses.suhbmit.api.services :as services]))

(defresource dictionaries
  :available-media-types ["application/json"]
  :method-allowed? (request-method-in :get )
  :handle-ok (fn [context]
               {:status "success"
                :data (services/get-dictionaries)}))

(defresource dictionary
  :available-media-types ["application/json"]
  :method-allowed? (request-method-in :get )
  :handle-ok (fn [context]
               (case (-> context :request :path-info )
                 "/" {:status "success"
                      :data (services/get-dictionary (-> context :request :params :version read-string))}
                 "/releases" {:status "success"
                              :data (services/get-releases-by-dictionary (-> context :request :params :version read-string))})))

(defresource projects
  :available-media-types ["application/json"]
  :method-allowed? (request-method-in :get )
  :handle-ok (fn [context]
               {:status "success"
                :data (services/get-projects)}))

(defresource project
  :available-media-types ["application/json"]
  :method-allowed? (request-method-in :get )
  :handle-ok (fn [context]
               (case (-> context :request :path-info)
                 "/" {:status "success"
                      :data (services/get-project (-> context :request :route-params :project))}
                 "/releases" {:status "success"
                              :data (services/get-submissions-for-project (-> context :request :route-params :project))})))

(defresource releases
  :available-media-types ["application/json"]
  :method-allowed? (request-method-in :get )
  :handle-ok (fn [context]
               {:status "success"
                :data (services/get-releases)}))

(defresource release
  :available-media-types ["application/json"]
  :method-allowed? (request-method-in :get )
  :handle-ok (fn [context]
               (case (-> context :request :path-info)
                 "/" {:status "success"
                      :data (services/get-release (-> context :request :route-params :release read-string))}
                 "/dictionary" {:status "success"
                                :data (services/get-dictionary-for-release
                                       (-> context :request :route-params :release read-string))})))

(defresource submissions
  :available-media-types ["application/json"]
  :method-allowed? (request-method-in :get)
  :handle-ok (fn [context]
               (case (-> context :request :path-info)
                 "/" {:status "success"
                      :data (services/get-submissions-for-release (-> context :request :route-params :release read-string))})))

(defresource submission
  :available-media-types ["application/json"]
  :method-allowed? (request-method-in :get)
  :handle-ok (fn [context]
               {:status "success"
                :data (services/get-submission (-> context :request :route-params :release read-string)
                                               (-> context :request :route-params :project))}))
