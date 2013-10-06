(ns prohses.suhbmit.api.resources
  (:require [liberator.core :refer [defresource request-method-in]]
            [prohses.suhbmit.api.services :as services]))

(defresource projects
  :available-media-types ["application/json"]
  :method-allowed? (request-method-in :get )
  :handle-ok (fn [context]
               {:status "success"
                :data (services/get-projects)
                }))

(defresource project
  :available-media-types ["application/json"]
  :method-allowed? (request-method-in :get )
  :handle-ok (fn [context]
               {:status "success"
                :data "project"
                }))
