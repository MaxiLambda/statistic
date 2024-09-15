(ns statistic.rest.controller.open.player-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [defroutes GET]]
            [statistic.db.tables.players :as players]))

(defn players-handler [_req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (json/write-str (players/get-all))})

(defroutes routes
           (GET "/players" [] players-handler))