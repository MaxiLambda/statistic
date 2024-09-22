(ns statistic.rest.controller.open.wins-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [GET defroutes]]
            [statistic.db.aggregate.match-aggregate :as match-aggregate]))

;this might belong in a different handler
(defn wins-handler [_req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (json/write-str (match-aggregate/get-number-wins))})

(defroutes routes
           (GET "/wins" [] wins-handler))