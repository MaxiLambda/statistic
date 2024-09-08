(ns statistic-web.views
  (:require
    [re-frame.core :as re-frame]
    [reagent-mui.material.button :refer [button]]
    [statistic-web.subs :as subs]))

;;create views - STEP 5

(def views {:home (let [name (re-frame/subscribe [::subs/name])]
                    [:div
                     [:h1
                      "Hello from " @name "!?"]
                     [button
                      {:variant "contained"
                       :color   "primary"}
                      "Click me"
                      ]
                     ])})

(defn main-panel []
  (let [routing (re-frame/subscribe [::subs/routing])]
    ((:path @routing) views)))
