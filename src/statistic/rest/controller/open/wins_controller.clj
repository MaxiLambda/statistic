(ns statistic.rest.controller.open.wins-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [defroutes GET]]
            [statistic.db.service.match :as match]))

;this might belong in a different handler
(defn wins-handler [_req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (json/write-str (match/get-number-wins))})

(defroutes routes
           (GET "/wins" [] wins-handler))