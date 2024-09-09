(ns statistic.rest.routes
  (:gen-class)
  (:require [clojure.data.json :as json]
            [compojure.core :refer :all]
            [ring.middleware.basic-authentication :refer [wrap-basic-authentication]]
            [ring.middleware.params :as params]
            [ring.middleware.resource :as resource]
            [ring.util.response :as response]
            [compojure.route :as route]
            [statistic.authentication.admin-authentication :refer [authenticated?]]
            [statistic.db.tables.players :as players]
            [statistic.db.service.match :as match]))


;;TODO split in controllers
(defn players-handler [_req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (json/write-str (players/get-all))})

(defn wins-handler [_req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (json/write-str (match/get-number-wins))})

(defn home-handler [_req]
  (response/file-response "public/index.html" {:root "resources"}))

(defroutes public-routes
           (GET "/players" [] players-handler)
           (GET "/wins" [] wins-handler)
           ;;TODO should  be POST
           (GET "/players/create" [] (response/response "handle new player"))
           (GET "/" [] home-handler)
           )

(defroutes protected-routes
           (GET "/hidden" [] (response/response "moin")))

(defroutes all-routes
           ;;resources should be available publicly
           ;;add all files from "./resources" to serve them
           ;;this reserves the prefixes public/index.html and public/js/compiled
           ;;adds everything in the resources/public dir to the path with the prefix public
           (resource/wrap-resource public-routes "public")
           (context "/admin" [] (wrap-basic-authentication protected-routes authenticated?))
           (route/not-found "Error 404 - route not found"))

(def app-routes
  (-> all-routes
      params/wrap-params))