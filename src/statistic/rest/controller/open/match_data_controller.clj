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
                                ;;TODO get real space
                                (matches/get-used-tags {:space 1})
                                ;;TODO get real space
                                (matches/get-used-tags {:discipline discipline :space 1})))}))

(defn disciplines-handler [_req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   ;;TODO get real space
   :body    (json/write-str (matches/get-used-disciplines {:space 1}))})

(defn matches-handler [_req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   ;;TODO get real space
   :body    (json/write-str (match-aggregate/get-all-matches-with-teams {:space 1}) :value-fn (date-value-wrapper))})

(defroutes routes
           (GET "/data/tags" [] tags-handler)
           (GET "/data/disciplines" [] disciplines-handler)
           (GET "/data/matches" [] matches-handler))