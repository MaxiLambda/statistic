(ns statistic.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [org.httpkit.server :as server]
            [statistic.db.tables.players :as players]))

(defn fps-handler [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (json/write-str (players/get-players))})

(defroutes app-routes
           (GET "/" [] fps-handler)
           (GET "/players" [] fps-handler)
           (route/not-found "Error 404 - route not found"))

(defn -main
  "This is the apps entry point"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (server/run-server #'app-routes {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))