(ns statistic.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [org.httpkit.server :as server]
            [statistic.db.tables.players :as players]
            [statistic.db.setup :as setup]))

(defn fps-handler [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (json/write-str (players/get-all))})

;;TODO figure out how to handle request parameters
;;TODO figure out how to create sessions (hint: ring?)
(defn login-handler [req]
  {:status  200
   :headers {"Content-Type" "text/plain"}
   :body    "hallo"})

(defroutes app-routes
           (GET "/" [] fps-handler)
           (GET "/players" [] fps-handler)
           (POST "/login" [] login-handler)
           (route/not-found "Error 404 - route not found"))

(defn -main
  "This is the apps entry point"
  [& args]
  ;; move to setup function in separate namespace
  (setup/db-setup)
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (server/run-server #'app-routes {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))