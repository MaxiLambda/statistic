(ns statistic.rest.routes
  (:gen-class)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.cookies :as cookies]
            [ring.middleware.json :refer [wrap-json-body]]
            [ring.middleware.keyword-params :as keyword-params]
            [ring.middleware.params :as params]
            [ring.middleware.resource :as resource]
            [ring.util.response :as response]
            [statistic.authentication.authentication-check :refer [edit-authenticated? view-authenticated?]]
            [statistic.authentication.wrap-auth :refer [wrap-auth]]
            [statistic.rest.controller.edit.admin-match-controller :as admin-match-controller]
            [statistic.rest.controller.edit.admin-player-controller :as admin-player-controller]
            [statistic.rest.controller.edit.admin-space-controller :as admin-space-controller]
            [statistic.rest.controller.login-controller :as login-controller]
            [statistic.rest.controller.view.match-data-controller :as match-data-controller]
            [statistic.rest.controller.view.player-controller :as player-controller]
            [statistic.rest.controller.view.space-controller :as space-controller]
            [statistic.rest.controller.view.wins-controller :as wins-controller]))

(defn home-handler [_req]
  (response/file-response "public/index.html" {:root "resources"}))

;;routes used to view data from spaces
(defroutes view-routes
           player-controller/routes
           wins-controller/routes
           space-controller/routes
           match-data-controller/routes)

(defroutes public-routes
           (GET "/" [] home-handler))

;;routes used to edit space data
(defroutes edit-routes
           admin-player-controller/routes
           admin-space-controller/routes
           admin-match-controller/routes)

(defroutes all-routes
           ;;resources should be available publicly
           ;;add all files from "./resources" to serve them
           ;;this reserves the prefixes public/index.html and public/js/compiled
           ;;adds everything in the resources/public dir to the path with the prefix public
           login-controller/routes
           (resource/wrap-resource public-routes "public")
           (context "/view" [] (wrap-auth view-routes view-authenticated?))
           (context "/edit" [] (wrap-auth edit-routes edit-authenticated?))
           (route/not-found "Error 404 - route not found"))

(def app-routes
  (-> all-routes
      cookies/wrap-cookies
      ;;parses request :body into keyword map
      (wrap-json-body {:keywords? true})
      ;;parses request :params (headers and url) into keyword map
      keyword-params/wrap-keyword-params
      ;;adds request params from headers and url to :params key
      params/wrap-params))