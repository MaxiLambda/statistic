(ns statistic.rest.routes
  (:gen-class)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-body]]
            [ring.middleware.keyword-params :as keyword-params]
            [ring.middleware.params :as params]
            [ring.middleware.resource :as resource]
            [ring.util.response :as response]
            [statistic.authentication.admin-authentication :refer [authenticated?]]
            [statistic.authentication.basic-auth :refer [wrap-add-basic-prefix]]
            [statistic.rest.controller.admin.admin-match-controller :as admin-match-controller]
            [statistic.rest.controller.admin.admin-player-controller :as admin-player-controller]
            [statistic.rest.controller.open.match-data-controller :as match-data-controller]
            [statistic.rest.controller.open.player-controller :as player-controller]
            [statistic.rest.controller.open.wins-controller :as wins-controller]
            [statistic.authentication.wrap-ring-basic-auth :refer [wrap-basic-authentication]]))

(defn home-handler [_req]
  (response/file-response "public/index.html" {:root "resources"}))

(defroutes public-routes
           player-controller/routes
           wins-controller/routes
           match-data-controller/routes
           (GET "/" [] home-handler))

(defroutes protected-routes
           admin-player-controller/routes
           admin-match-controller/routes)

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
      wrap-add-basic-prefix
      ;;parses request :body into keyword map
      (wrap-json-body {:keywords? true})
      ;;parses request :params (headers and url) into keyword map
      keyword-params/wrap-keyword-params
      ;;adds request params from headers and url to :params key
      params/wrap-params))