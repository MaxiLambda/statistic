(ns statistic.rest.routes
  (:gen-class)
  (:require [clojure.data.json :as json]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as response]
            [statistic.db.tables.players :as players]
            [ring.middleware.basic-authentication :refer [wrap-basic-authentication]]))

;;TODO change
(defn authenticated? [user password]
  (and (= "admin" user)
       (= "admin" password)))

(defn fps-handler [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (json/write-str (players/get-all))})

(defn login-handler [req]
  (response/file-response "index.html" {:root "resources"}))

(defroutes public-routes
           (GET "/" [] login-handler)
           (GET "/login" [] login-handler))

(defroutes protected-routes
           (GET "/players" [] fps-handler)
           (GET "/hidden" [] (response/response "moin")))

(defroutes app-routes
           public-routes
           (wrap-basic-authentication protected-routes authenticated? )
           (route/not-found "Error 404 - route not found"))
