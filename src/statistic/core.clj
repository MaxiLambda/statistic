(ns statistic.core
  (:gen-class)
  (:require [org.httpkit.server :as server]
            [statistic.db.setup :as setup]
            [statistic.rest.routes :as routes]))



(defn -main
  "This is the apps entry point"
  [& args]
  ;; move to setup function in separate namespace
  (setup/db-setup)
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (server/run-server #'routes/app-routes {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))