(ns statistic.rest.controller.view.player-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [GET defroutes]]
            [statistic.db.tables.players :as players]))

(defn players-handler [{params :params auth :auth}]
  ;;check if the user authentication is for the requested space
  (if (= (get auth 2) (:space params))
    {:status  200
     :headers {"Content-Type" "text/json"}
     :body    (json/write-str (players/get-all (update-in (select-keys params [:space])
                                                          [:space]
                                                          ^[String] Integer/parseInt)))}
    {:status  401
     :headers {"Content-Type" "text/html"}
     :body    "authenticated for different space"}))

(defroutes routes
           (GET "/players" [] players-handler))