(ns statistic-web.views
  (:require
    [re-frame.core :as re-frame]
    [reagent-mui.material.button :refer [button]]
    [statistic-web.subs :as subs]))

;;create views - STEP 5

(defn home [] (let [name (re-frame/subscribe [::subs/name])]
                [:div
                 [:h1
                  "Hello from " @name "!?"]
                 [button
                  {:variant "contained"
                   :color   "primary"}
                  "Click me"
                  ]
                 ]))

(defn views [key]
  (key {:home (home)}))

(defn main-panel []
  (let [view (re-frame/subscribe [::subs/view])]
    (views @view)))
