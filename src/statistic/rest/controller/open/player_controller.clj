(ns statistic.rest.controller.open.player-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [GET defroutes]]
            [statistic.db.tables.players :as players]))

(defn players-handler [_req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   ;;TODO get real space
   :body    (json/write-str (players/get-all {:space 1}))})

(defroutes routes
           (GET "/players" [] players-handler))