(ns statistic.rest.controller.open.match-data-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [GET defroutes]]
            [statistic.db.aggregate.match-aggregate :as match-aggregate]
            [statistic.db.tables.matches :as matches]
            [statistic.json-writer.date-value-wrapper :refer [date-value-wrapper]]))

(defn tags-handler [req]
  (let [discipline (get-in req [:params :discipline])]
    {:status  200
     :headers {"Content-Type" "text/json"}
     :body    (json/write-str (if (nil? discipline)
                                (matches/get-used-tags)
                                (matches/get-used-tags {:discipline discipline})))}))

(defn disciplines-handler [_req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (json/write-str (matches/get-used-disciplines))})

(defn matches-handler [_req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   ;;add support for LocalDateTime values to json/write-str
   :body    (json/write-str (match-aggregate/get-all-matches-with-teams) :value-fn (date-value-wrapper))})

(defroutes routes
           (GET "/data/tags" [] tags-handler)
           (GET "/data/disciplines" [] disciplines-handler)
           (GET "/data/matches" [] matches-handler))