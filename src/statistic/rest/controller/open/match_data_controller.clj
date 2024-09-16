(ns statistic.rest.controller.open.match-data-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [defroutes GET]]
            [statistic.service.match :as match]))

(defn tags-handler [_req]
        {:status  200
         :headers {"Content-Type" "text/json"}
         :body    (json/write-str (match/get-used-tags))})

(defn disciplines-handler [_req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (json/write-str (match/get-used-disciplines))})

(defroutes routes
           (GET "/data/tags" [] tags-handler)
           (GET "/data/disciplines" [] disciplines-handler))