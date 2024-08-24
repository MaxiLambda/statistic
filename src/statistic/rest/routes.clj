(ns statistic.rest.routes
  (:gen-class)
  (:require [clojure.data.json :as json]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.basic-authentication :refer [wrap-basic-authentication]]
            [ring.util.response :as response]
            [ring.middleware.params :as params]
            [statistic.db.tables.players :as players]
            [statistic.authentication.admin-authentication :refer [authenticated?]]))



(defn players-handler [req]
  (println req)
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (json/write-str (players/get-all))})

(defn home-handler [req]
  (response/file-response "index.html" {:root "resources"}))

(defroutes public-routes
           (GET "/players" [] players-handler)
           (GET "/players/create" [] (response/response "handle new player"))
           (GET "/" [] home-handler))

(defroutes protected-routes
           (GET "/hidden" [] (response/response "moin")))

(defroutes app-routes
           public-routes
           ;;TODO wrap protected routes in admin prefix
           (-> protected-routes
               params/wrap-params
               (wrap-basic-authentication authenticated?))
           (route/not-found "Error 404 - route not found"))
