(ns statistic.rest.controller.open.player-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [GET defroutes]]
            [statistic.db.tables.players :as players]))

(defn players-handler [{params :params}]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (json/write-str (players/get-all (update-in (get-in params [:space])
                                                        [:space]
                                                        ^[String] Integer/parseInt)))})

(defroutes routes
           (GET "/players" [] players-handler))