(ns statistic.rest.controller.open.match-data-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [defroutes GET]]
            [statistic.service.match-service :as match]))

(defn tags-handler [_req]
        {:status  200
         :headers {"Content-Type" "text/json"}
         :body    (json/write-str (match/get-used-tags))})

(defn disciplines-handler [_req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (json/write-str (match/get-used-disciplines))})

(defn matches-handler [_req]
  {:status 200
   :headers {"Content-Type" "text/json"}
   :body    (json/write-str (match/get-all-matches-with-teams))})

(defroutes routes
           (GET "/data/tags" [] tags-handler)
           (GET "/data/disciplines" [] disciplines-handler)
           (GET "/data/matches" [] matches-handler))