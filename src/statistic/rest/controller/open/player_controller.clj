(ns statistic.rest.controller.open.player-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [GET defroutes]]
            [statistic.db.tables.players :as players]))

(defn players-handler [{params :params :as req}]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (json/write-str (players/get-all (update-in (select-keys params [:space])
                                                        [:space]
                                                        ^[String] Integer/parseInt)))})

(defroutes routes
           (GET "/players" [] players-handler))