(ns statistic.rest.controller.space-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [GET defroutes]]
            [statistic.db.tables.spaces :as spaces]))

(defn spaces-handler [_req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (json/write-str (spaces/get-all))})

(defroutes routes
           (GET "/spaces" [] spaces-handler))