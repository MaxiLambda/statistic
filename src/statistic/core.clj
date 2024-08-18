(ns statistic.core
  (:gen-class)
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]))

(defn fps-handler [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Pew Pew!"})

(defn mail-man []
  "{\"Spongebob Narrator\": \"5 years later...\"}")

(defn mail-handler [req]
  {:status 200
   :headers {"Content-Type" "text/json"}
   :body (mail-man)})

(defn general-handler [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "All hail General Zod!"})

(defroutes app-routes
           (GET "/" [] fps-handler)
           (GET "/postoffice" [] mail-handler)
           (ANY "/anything-goes" [] general-handler)
           (route/not-found "You must be new here"))

(defn -main
  "This is the apps entry point"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (server/run-server #'app-routes {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))