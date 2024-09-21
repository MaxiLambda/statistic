(ns statistic.rest.controller.open.match-data-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [defroutes GET]]
            [statistic.service.match-service :as match]
            [statistic.json-writer.date-value-wrapper :refer [date-value-wrapper]]))

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
   ;;add support for LocalDateTime values to json/write-str
   :body    (json/write-str (match/get-all-matches-with-teams) :value-fn (date-value-wrapper))})

(defroutes routes
           (GET "/data/tags" [] tags-handler)
           (GET "/data/disciplines" [] disciplines-handler)
           (GET "/data/matches" [] matches-handler))