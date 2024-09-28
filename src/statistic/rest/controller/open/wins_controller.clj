(ns statistic.rest.controller.open.wins-controller
  (:require [clojure.data.json :as json]
            [compojure.core :refer [GET defroutes]]
            [statistic.db.aggregate.match-aggregate :as match-aggregate]))

;this might belong in a different handler
(defn wins-handler [{params :params}]
  {:status  200
   :headers {"Content-Type" "text/json"}
   ;;TODO get real space
   :body    (json/write-str (match-aggregate/get-number-wins (merge {:space 1} (select-keys params [:tag :discipline]))))})

(defroutes routes
           (GET "/wins" [] wins-handler))