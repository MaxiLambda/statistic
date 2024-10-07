(ns statistic.rest.controller.view.match-data-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [GET defroutes]]
            [statistic.db.aggregate.match-aggregate :as match-aggregate]
            [statistic.db.tables.matches :as matches]
            [statistic.json-writer.date-value-wrapper :refer [date-value-wrapper]]))

(defn tags-handler [{params :params auth :auth}]
  ;;check if the user authentication is for the requested space
  (if (= (get auth 2) (:space params))
    (let [discipline (:discipline params)
          space (Integer/parseInt (:space params))]
      {:status  200
       :headers {"Content-Type" "text/json"}
       :body    (json/write-str (if (nil? discipline)
                                  (matches/get-used-tags {:space space})
                                  (matches/get-used-tags {:discipline discipline :space space})))})
    {:status  401
     :headers {"Content-Type" "text/html"}
     :body    "authenticated for different space"}))

(defn disciplines-handler [{params :params auth :auth}]
  ;;check if the user authentication is for the requested space
  (if (= (get auth 2) (:space params))
    {:status  200
     :headers {"Content-Type" "text/json"}
     :body    (json/write-str (matches/get-used-disciplines {:space (-> params :space Integer/parseInt)}))}
    {:status  401
     :headers {"Content-Type" "text/html"}
     :body    "authenticated for different space"}))

(defn matches-handler [{params :params auth :auth}]
  ;;check if the user authentication is for the requested space
  (if (= (get auth 2) (:space params))
    {:status  200
     :headers {"Content-Type" "text/json"}
     :body    (json/write-str (match-aggregate/get-all-matches-with-teams {:space (-> params :space Integer/parseInt)})
                              :value-fn (date-value-wrapper))}
    {:status  401
     :headers {"Content-Type" "text/html"}
     :body    "authenticated for different space"}))

(defroutes routes
           (GET "/data/tags" [] tags-handler)
           (GET "/data/disciplines" [] disciplines-handler)
           (GET "/data/matches" [] matches-handler))