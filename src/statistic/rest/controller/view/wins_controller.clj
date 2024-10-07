(ns statistic.rest.controller.view.wins-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [GET defroutes]]
            [statistic.db.aggregate.match-aggregate :as match-aggregate]))

;this might belong in a different handler
(defn wins-handler [{params :params auth :auth}]
  ;;check if the user authentication is for the requested space
  (if (= (get auth 2) (:space params))
    {:status  200
     :headers {"Content-Type" "text/json"}
     :body    (json/write-str (match-aggregate/get-number-wins
                                (update-in (select-keys params [:tag :discipline :space])
                                           [:space]
                                           ^[String] Integer/parseInt)))}
    {:status  401
     :headers {"Content-Type" "text/html"}
     :body    "authenticated for different space"}))

(defroutes routes
           (GET "/wins" [] wins-handler))